package de.heins.vokabeltraineronline;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VokabeltrainerOnlineApplication {

	public static void main(String[] args) {
		if (amIAllowedToStart("RestoreFromBackup", "ActualRunning, Backup", "ActualRunning, DropBackup")) {
			setLastAction("ActualRunning, Backup");
			SpringApplication.run(VokabeltrainerOnlineApplication.class, args);
		} else {
			System.out.println("Not allowed to start.");
		}
	}

	private static boolean amIAllowedToStart(String... possibleLastActions ) {
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

	private static void setLastAction(String string) {
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

}
