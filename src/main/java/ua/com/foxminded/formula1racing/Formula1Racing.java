package ua.com.foxminded.formula1racing;

import ua.com.foxminded.formula1racing.timer.RacersTimer;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class Formula1Racing {

    public static void main(String[] args) {

        RacersTimer racersTimer = new RacersTimer();

        File abbreviationsFile;
        File startLogFile;
        File endLogFile;

        boolean fileExists = false;
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        while (!fileExists) {
            String projectPath = System.getProperty("user.dir");
            System.out.println("Put your formula1 files in " + projectPath);
            System.out.println("With names abbreviations.txt, start.log and end.log");
            System.out.println("Enter key to continue or \"0\" to exit...");

            try {
                String userAnswer = consoleReader.readLine();
                if (userAnswer.equals("0")) {
                    System.exit(0);
                }
                abbreviationsFile = new File(projectPath + "\\abbreviations.txt");
                startLogFile = new File(projectPath + "\\start.log");
                endLogFile = new File(projectPath + "\\end.log");
                Thread.sleep(100);
                System.out.println(racersTimer.getLeaderboard(abbreviationsFile, startLogFile, endLogFile));
                fileExists = true;
            } catch (Exception e) {
                System.out.println("Something wrong. Exception:");
                e.printStackTrace();
            }
        }
    }
}
