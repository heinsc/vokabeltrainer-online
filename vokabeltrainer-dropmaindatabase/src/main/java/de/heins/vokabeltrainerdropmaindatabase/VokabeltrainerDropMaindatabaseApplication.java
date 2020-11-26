package de.heins.vokabeltrainerdropmaindatabase;

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

import de.heins.vokabeltrainerdropmaindatabase.entity.AppUser;
import de.heins.vokabeltrainerdropmaindatabase.entity.BehaviourIfPoolWithWrongAnswersIsFull;
import de.heins.vokabeltrainerdropmaindatabase.entity.BehaviourIfWrong;
import de.heins.vokabeltrainerdropmaindatabase.entity.FaultTolerance;
import de.heins.vokabeltrainerdropmaindatabase.entity.IndexBox;
import de.heins.vokabeltrainerdropmaindatabase.entity.LearningStrategy;
import de.heins.vokabeltrainerdropmaindatabase.entity.QuestionWithAnswer;
import de.heins.vokabeltrainerdropmaindatabase.entity.SuccessStep;
import de.heins.vokabeltrainerdropmaindatabase.repository.AppUserRepository;
import de.heins.vokabeltrainerdropmaindatabase.repository.IndexBoxRepository;
import de.heins.vokabeltrainerdropmaindatabase.repository.LearningStrategyRepository;
import de.heins.vokabeltrainerdropmaindatabase.repository.QuestionWithAnswerRepository;
import de.heins.vokabeltrainerdropmaindatabase.repository.SuccessStepRepository;

@SpringBootApplication
public class VokabeltrainerDropMaindatabaseApplication implements CommandLineRunner {
	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired
	private SuccessStepRepository successStepRepository;
	@Autowired
	private LearningStrategyRepository learningStrategyRepository;
	@Autowired
	private IndexBoxRepository indexBoxRepository;
	@Autowired
	private QuestionWithAnswerRepository questionWithAnswerRepository;

	public static void main(String[] args) {
		SpringApplication.run(VokabeltrainerDropMaindatabaseApplication.class, args);
	}
	@Override
	public void run(String... args) {
		if (amIAllowedToStart("Backup", "DropMainDataBase")) {
			AppUser appUserDummy = saveDummyAppUserInstance();
			SuccessStep successStepDummy = saveDummySuccessStepInstance(appUserDummy);
			LearningStrategy learningStrategy = saveDummyLearningStrategyInstance(appUserDummy);
			IndexBox indexBoxDummy = saveDummyIndexBoxInstance(appUserDummy);
			QuestionWithAnswer questionWithAnswerDummy = saveDummyQuestionWithAnswerInstance(appUserDummy, indexBoxDummy, learningStrategy);
			questionWithAnswerRepository.delete(questionWithAnswerDummy);
			indexBoxRepository.delete(indexBoxDummy);
			learningStrategyRepository.delete(learningStrategy);
			successStepRepository.delete(successStepDummy);
			appUserRepository.delete(appUserDummy);
			setLastAction("DropMainDataBase");
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
	
	private QuestionWithAnswer saveDummyQuestionWithAnswerInstance(//
			AppUser appUserDummy//
			, IndexBox indexBox//
			, LearningStrategy learningStrategy//
	) {
		QuestionWithAnswer questionWithAnswer = new QuestionWithAnswer(//
				10L//
				, indexBox//
				, null//
				, "DummyQuestion"//
				, "DummyAnswer"//
				, appUserDummy//
		);
		questionWithAnswer.setActualSuccessStep(null);
		questionWithAnswer.setNextAppearance(Calendar.getInstance().getTime());
		return questionWithAnswerRepository.save(questionWithAnswer);
	}
	private IndexBox saveDummyIndexBoxInstance(AppUser appUserDummy) {
		IndexBox indexBox = new IndexBox(//
				5L//
				, "DummyIndexBoxName"//
				, "DummyIndexBoxSubject"//
				, appUserDummy//
		);
		indexBox.setActualInUse(false);
		return indexBoxRepository.save(indexBox);
	}
	private LearningStrategy saveDummyLearningStrategyInstance(AppUser appUserDummy) {
		LearningStrategy learningStrategy = new LearningStrategy(//
				4L//
				, "DummyLearningStrategy"//
				, appUserDummy//
		);
		return learningStrategyRepository.save(learningStrategy); 
	}
	/*
	 * By this method, all columns are created
	 */
	private AppUser saveDummyAppUserInstance() {
		AppUser dummyEntry = new AppUser(//
				1L//
				, "a@aa.aa"//
				, "Pin12345"//
				, FaultTolerance.NO_TOLERANCE//
				, 5//
				, BehaviourIfPoolWithWrongAnswersIsFull.EMPTY_POOL_UNTIL_ALL_QUESTIONS_CORRECT//
				, Calendar.getInstance().getTime()//
		);
		AppUser result = appUserRepository.save(dummyEntry);
		return result;
	}
	/*
	 * By this method, all columns are created
	 */
	private SuccessStep saveDummySuccessStepInstance(AppUser appUserDummy) {
		SuccessStep dummyEntry = new SuccessStep(//
				2L//
				, "Erste Erfolgsstufe"//
				, 5//
				, BehaviourIfWrong.PREVIOUS_SUCCESSSTEP_NEXTDAY_SUCCESSSTEP_DURATION//
				, appUserDummy//
		);
		return successStepRepository.save(dummyEntry);
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
