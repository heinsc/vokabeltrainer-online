package de.heins.vokabeltrainerrestorebackup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class VokabeltrainerRestoreBackupApplication implements CommandLineRunner {
	@Autowired
	private VokabeltrainerRestoreBackupAppUserHandler appUserTableHandler;
	@Autowired
	private VokabeltrainerRestoreBackupSuccessStepHandler successStepTableHandler;
	@Autowired
	private VokabeltrainerRestoreBackupLearningStrategyHandler learningStrategyTableHandler;
	@Autowired
	private VokabeltrainerRestoreBackupIndexBoxHandler indexBoxTableHandler;
	@Autowired
	private VokabeltrainerRestoreBackupQuestionWithAnswerHandler questionWithAnswerTableHandler;

	public static void main(String[] args) {

		SpringApplication.run(VokabeltrainerRestoreBackupApplication.class, args);
	}

	@Override
	public void run(String... args) {
		if (amIAllowedToStart("DropMainDataBase")) {
			appUserTableHandler.handleTable();
			successStepTableHandler.handleTable();
			learningStrategyTableHandler.handleTable();
			indexBoxTableHandler.handleTable();
			questionWithAnswerTableHandler.handleTable();
			setLastAction("RestoreFromBackup");
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
