package de.heins.vokabeltrainerdropbackup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.heins.vokabeltrainerdropbackup.business.entity.backup.AppUserBackup;
import de.heins.vokabeltrainerdropbackup.business.entity.backup.BehaviourIfPoolWithWrongAnswersIsFullBackup;
import de.heins.vokabeltrainerdropbackup.business.entity.backup.BehaviourIfWrongBackup;
import de.heins.vokabeltrainerdropbackup.business.entity.backup.FaultToleranceBackup;
import de.heins.vokabeltrainerdropbackup.business.entity.backup.IndexBoxBackup;
import de.heins.vokabeltrainerdropbackup.business.entity.backup.LearningStrategyBackup;
import de.heins.vokabeltrainerdropbackup.business.entity.backup.QuestionWithAnswerBackup;
import de.heins.vokabeltrainerdropbackup.business.entity.backup.SuccessStepBackup;
import de.heins.vokabeltrainerdropbackup.business.repository.backup.AppUserBackupRepository;
import de.heins.vokabeltrainerdropbackup.business.repository.backup.IndexBoxBackupRepository;
import de.heins.vokabeltrainerdropbackup.business.repository.backup.LearningStrategyBackupRepository;
import de.heins.vokabeltrainerdropbackup.business.repository.backup.QuestionWithAnswerBackupRepository;
import de.heins.vokabeltrainerdropbackup.business.repository.backup.SuccessStepBackupRepository;

@SpringBootApplication
public class VokabeltrainerDropBackupApplication implements CommandLineRunner {
	@Autowired
	private AppUserBackupRepository appUserBackupRepository;
	@Autowired
	private SuccessStepBackupRepository successStepBackupRepository;
	@Autowired
	private LearningStrategyBackupRepository learningStrategyBackupRepository;
	@Autowired
	private IndexBoxBackupRepository indexBoxBackupRepository;
	@Autowired
	private QuestionWithAnswerBackupRepository questionWithAnswerBackupRepository;

	public static void main(String[] args) {
		SpringApplication.run(VokabeltrainerDropBackupApplication.class, args);
	}
	@Override
	public void run(String... args) {
		if (amIAllowedToStart("DropBackup", "RestoreFromBackup", "ActualRunning")) {
			AppUserBackup appUserBackupDummy = saveDummyAppUserBackupInstance();
			SuccessStepBackup successStepBackupDummy = saveDummySuccessStepBackupInstance(appUserBackupDummy);
			LearningStrategyBackup learningStrategyBackup = saveDummyLearningStrategyBackupInstance(appUserBackupDummy);
			IndexBoxBackup indexBoxBackupDummy = saveDummyIndexBoxBackupInstance(appUserBackupDummy);
			QuestionWithAnswerBackup questionWithAnswerBackupDummy = saveDummyQuestionWithAnswerBackupInstance(appUserBackupDummy, indexBoxBackupDummy, learningStrategyBackup);
			questionWithAnswerBackupRepository.delete(questionWithAnswerBackupDummy);
			indexBoxBackupRepository.delete(indexBoxBackupDummy);
			learningStrategyBackupRepository.delete(learningStrategyBackup);
			successStepBackupRepository.delete(successStepBackupDummy);
			appUserBackupRepository.delete(appUserBackupDummy);
			setLastAction("DropBackup");
		} else {
			String exceptionText = "Not allowed to start. Look in the file to see what's next allowed to start!";
			throw new RuntimeException(exceptionText);
		}
	}

	private void setLastAction(String string) {
	    try {
	        File myObj = new File(".." + System.getProperty("file.separator") + "LastAction.txt");
	        if (myObj.createNewFile()) {
	          System.out.println("File created: " + myObj.getName());
	          throw new RuntimeException("The File must exist.");
	        } else {
	          System.out.println("File exists - O.K.");
	        }
	        FileWriter myWriter = new FileWriter(".." + System.getProperty("file.separator") + "LastAction.txt");
	        myWriter.write(string);
	        myWriter.close();
	      } catch (IOException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
	      }
	}
	
	private QuestionWithAnswerBackup saveDummyQuestionWithAnswerBackupInstance(//
			AppUserBackup appUserBackupDummy//
			, IndexBoxBackup indexBoxBackup//
			, LearningStrategyBackup learningStrategyBackup//
	) {
		QuestionWithAnswerBackup questionWithAnswerBackup = new QuestionWithAnswerBackup(//
				10L//
				, indexBoxBackup//
				, null//
				, "DummyQuestion"//
				, "DummyAnswer"//
				, appUserBackupDummy//
		);
		questionWithAnswerBackup.setActualSuccessStepBackup(null);
		questionWithAnswerBackup.setNextAppearance(Calendar.getInstance().getTime());
		return questionWithAnswerBackupRepository.save(questionWithAnswerBackup);
	}
	private IndexBoxBackup saveDummyIndexBoxBackupInstance(AppUserBackup appUserBackupDummy) {
		IndexBoxBackup indexBoxBackup = new IndexBoxBackup(//
				5L//
				, "DummyIndexBoxName"//
				, "DummyIndexBoxSubject"//
				, appUserBackupDummy//
		);
		indexBoxBackup.setActualInUse(false);
		return indexBoxBackupRepository.save(indexBoxBackup);
	}
	private LearningStrategyBackup saveDummyLearningStrategyBackupInstance(AppUserBackup appUserBackupDummy) {
		LearningStrategyBackup learningStrategyBackup = new LearningStrategyBackup(//
				4L//
				, "DummyLearningStrategy"//
				, appUserBackupDummy//
		);
		return learningStrategyBackupRepository.save(learningStrategyBackup); 
	}
	/*
	 * By this method, all columns are created
	 */
	private AppUserBackup saveDummyAppUserBackupInstance() {
		AppUserBackup dummyEntry = new AppUserBackup(//
				1L//
				, "a@aa.aa"//
				, "Pin12345"//
				, FaultToleranceBackup.NO_TOLERANCE//
				, 5//
				, BehaviourIfPoolWithWrongAnswersIsFullBackup.EMPTY_POOL_UNTIL_ALL_QUESTIONS_CORRECT//
				, Calendar.getInstance().getTime()//
		);
		AppUserBackup result = appUserBackupRepository.save(dummyEntry);
		return result;
	}
	/*
	 * By this method, all columns are created
	 */
	private SuccessStepBackup saveDummySuccessStepBackupInstance(AppUserBackup appUserBackupDummy) {
		SuccessStepBackup dummyEntry = new SuccessStepBackup(//
				2L//
				, "Erste Erfolgsstufe"//
				, 5//
				, BehaviourIfWrongBackup.PREVIOUS_SUCCESSSTEP_NEXTDAY_SUCCESSSTEP_DURATION//
				, appUserBackupDummy//
		);
		return successStepBackupRepository.save(dummyEntry);
	}
	private boolean amIAllowedToStart(String... possibleLastActions ) {
	    StringBuffer content = new StringBuffer();
		try {
			File aFile = new File(".." + System.getProperty("file.separator") + "LastAction.txt");
			Scanner myReader = new Scanner(aFile);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				content.append(data);
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
			return false;
		}
		for (String currentEntry : possibleLastActions) {
			if (currentEntry.equals(content.toString())){
				return true;
			}
		}
		return false;
	}
}
