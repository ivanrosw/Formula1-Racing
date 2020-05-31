package ua.com.foxminded.formula1racing.timer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ua.com.foxminded.formula1racing.exceptions.CalculateException;
import ua.com.foxminded.formula1racing.exceptions.FileProcessingException;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class RacersTimerTest {

    @Test
    void TestGetLeaderboard_ShouldPrintLeaderboard_WhenInputCorrectFiles() {
        ClassLoader classLoader = getClass().getClassLoader();

        File abbreviationsFile = new File(classLoader.getResource("abbreviations.txt").getFile());
        File endLogFile = new File(classLoader.getResource("end.log").getFile());
        File startLogFile = new File(classLoader.getResource("start.log").getFile());

        RacersTimer racersTimer = new RacersTimer();

        String expected = "1. Sebastian Vettel   | FERRARI                       | 01:04.415\n" +
                "2. Daniel Ricciardo   | RED BULL RACING TAG HEUER     | 01:12.013\n" +
                "3. Valtteri Bottas    | MERCEDES                      | 01:12.434\n" +
                "4. Lewis Hamilton     | MERCEDES                      | 01:12.460\n" +
                "5. Stoffel Vandoorne  | MCLAREN RENAULT               | 01:12.463\n" +
                "6. Kimi Raikkonen     | FERRARI                       | 01:12.639\n" +
                "7. Fernando Alonso    | MCLAREN RENAULT               | 01:12.657\n" +
                "8. Sergey Sirotkin    | WILLIAMS MERCEDES             | 01:12.706\n" +
                "9. Charles Leclerc    | SAUBER FERRARI                | 01:12.829\n" +
                "10. Sergio Perez      | FORCE INDIA MERCEDES          | 01:12.848\n" +
                "11. Romain Grosjean   | HAAS FERRARI                  | 01:12.930\n" +
                "12. Pierre Gasly      | SCUDERIA TORO ROSSO HONDA     | 01:12.941\n" +
                "13. Carlos Sainz      | RENAULT                       | 01:12.950\n" +
                "14. Esteban Ocon      | FORCE INDIA MERCEDES          | 01:13.028\n" +
                "15. Nico Hulkenberg   | RENAULT                       | 01:13.065\n" +
                "-----------------------------------------------------------------\n" +
                "16. Brendon Hartley   | SCUDERIA TORO ROSSO HONDA     | 01:13.179\n" +
                "17. Marcus Ericsson   | SAUBER FERRARI                | 01:13.265\n" +
                "18. Lance Stroll      | WILLIAMS MERCEDES             | 01:13.323\n" +
                "19. Kevin Magnussen   | HAAS FERRARI                  | 01:13.393\n";
        String actual = racersTimer.getLeaderboard(abbreviationsFile, startLogFile, endLogFile);
        assertEquals(expected, actual);
    }

    @Test
    void TestGetLeaderboard_ShouldPrintLeaderboard_WhenInputCorrectLogFilesSwitched() {
        ClassLoader classLoader = getClass().getClassLoader();

        File abbreviationsFile = new File(classLoader.getResource("abbreviations.txt").getFile());
        File endLogFile = new File(classLoader.getResource("end.log").getFile());
        File startLogFile = new File(classLoader.getResource("start.log").getFile());

        RacersTimer racersTimer = new RacersTimer();

        String expected = "1. Sebastian Vettel   | FERRARI                       | 01:04.415\n" +
                "2. Daniel Ricciardo   | RED BULL RACING TAG HEUER     | 01:12.013\n" +
                "3. Valtteri Bottas    | MERCEDES                      | 01:12.434\n" +
                "4. Lewis Hamilton     | MERCEDES                      | 01:12.460\n" +
                "5. Stoffel Vandoorne  | MCLAREN RENAULT               | 01:12.463\n" +
                "6. Kimi Raikkonen     | FERRARI                       | 01:12.639\n" +
                "7. Fernando Alonso    | MCLAREN RENAULT               | 01:12.657\n" +
                "8. Sergey Sirotkin    | WILLIAMS MERCEDES             | 01:12.706\n" +
                "9. Charles Leclerc    | SAUBER FERRARI                | 01:12.829\n" +
                "10. Sergio Perez      | FORCE INDIA MERCEDES          | 01:12.848\n" +
                "11. Romain Grosjean   | HAAS FERRARI                  | 01:12.930\n" +
                "12. Pierre Gasly      | SCUDERIA TORO ROSSO HONDA     | 01:12.941\n" +
                "13. Carlos Sainz      | RENAULT                       | 01:12.950\n" +
                "14. Esteban Ocon      | FORCE INDIA MERCEDES          | 01:13.028\n" +
                "15. Nico Hulkenberg   | RENAULT                       | 01:13.065\n" +
                "-----------------------------------------------------------------\n" +
                "16. Brendon Hartley   | SCUDERIA TORO ROSSO HONDA     | 01:13.179\n" +
                "17. Marcus Ericsson   | SAUBER FERRARI                | 01:13.265\n" +
                "18. Lance Stroll      | WILLIAMS MERCEDES             | 01:13.323\n" +
                "19. Kevin Magnussen   | HAAS FERRARI                  | 01:13.393\n";
        String actual = racersTimer.getLeaderboard(abbreviationsFile, endLogFile, startLogFile);
        assertEquals(expected, actual);
    }

    @Test
    void TestGetLeaderboard_ShouldThrowsFileProcessingException_WhenInputAbbreviationsFilesDontExists() {
        ClassLoader classLoader = getClass().getClassLoader();
        String incorrectFileName = "dfgdfgsh";

        File abbreviationsFile = new File(incorrectFileName);
        File endLogFile = new File(classLoader.getResource("end.log").getFile());
        File startLogFile = new File(classLoader.getResource("start.log").getFile());

        RacersTimer racersTimer = new RacersTimer();
        Assertions.assertThrows(FileProcessingException.class, () -> {
            racersTimer.getLeaderboard(abbreviationsFile, startLogFile, endLogFile);
        });
    }

    @Test
    void TestGetLeaderboard_ShouldThrowsFileProcessingException_WhenInputStartLogFilesDontExists() {
        ClassLoader classLoader = getClass().getClassLoader();
        String incorrectFileName = "dfgdfgsh";

        File abbreviationsFile = new File(classLoader.getResource("abbreviations.txt").getFile());
        File startLogFile = new File(incorrectFileName);
        File endLogFile = new File(classLoader.getResource("end.log").getFile());

        RacersTimer racersTimer = new RacersTimer();
        Assertions.assertThrows(FileProcessingException.class, () -> {
            racersTimer.getLeaderboard(abbreviationsFile, startLogFile, endLogFile);
        });
    }

    @Test
    void TestGetLeaderboard_ShouldThrowsFileProcessingException_WhenInputEndLogFilesDontExists() {
        ClassLoader classLoader = getClass().getClassLoader();
        String incorrectFileName = "dfgdfgsh";

        File abbreviationsFile = new File(classLoader.getResource("abbreviations.txt").getFile());
        File startLogFile = new File(classLoader.getResource("start.log").getFile());
        File endLogFile = new File(incorrectFileName);

        RacersTimer racersTimer = new RacersTimer();
        Assertions.assertThrows(FileProcessingException.class, () -> {
            racersTimer.getLeaderboard(abbreviationsFile, startLogFile, endLogFile);
        });
    }

    @Test
    void TestGetLeaderboard_ShouldThrowsFileProcessingException_WhenInputFilesHaveIncorrectData() {
        ClassLoader classLoader = getClass().getClassLoader();

        File abbreviationsFile = new File(classLoader.getResource("abbreviations.txt").getFile());
        File startLogFile = new File(classLoader.getResource("start.log").getFile());
        File incorrectLogFile = new File(classLoader.getResource("incorrectDate.log").getFile());

        RacersTimer racersTimer = new RacersTimer();
        Assertions.assertThrows(FileProcessingException.class, () -> {
            racersTimer.getLeaderboard(abbreviationsFile, startLogFile, incorrectLogFile);
        });
    }

    @Test
    void TestGetLeaderboard_ShouldThrowsFileProcessingException_WhenInputFilesSwitched() {
        ClassLoader classLoader = getClass().getClassLoader();

        File abbreviationsFile = new File(classLoader.getResource("abbreviations.txt").getFile());
        File startLogFile = new File(classLoader.getResource("start.log").getFile());
        File endLogFile = new File(classLoader.getResource("incorrectDate.log").getFile());

        RacersTimer racersTimer = new RacersTimer();
        Assertions.assertThrows(FileProcessingException.class, () -> {
            racersTimer.getLeaderboard(endLogFile, startLogFile, abbreviationsFile);
        });
    }

    @Test
    void TestGetLeaderboard_ShouldThrowsCalculateException_WhenInputLogsHaveDifferentLength() {
        ClassLoader classLoader = getClass().getClassLoader();

        File abbreviationsFile = new File(classLoader.getResource("abbreviations.txt").getFile());
        File startLogFile = new File(classLoader.getResource("start.log").getFile());
        File incorrectLogFile = new File(classLoader.getResource("incorrectLength.log").getFile());

        RacersTimer racersTimer = new RacersTimer();
        Assertions.assertThrows(CalculateException.class, () -> {
            racersTimer.getLeaderboard(abbreviationsFile, startLogFile, incorrectLogFile);
        });
    }

    @Test
    void TestGetLeaderboard_ShouldThrowsFileProcessingException_WhenInputLogFilesHaveIncorrectAbbreviations() {
        ClassLoader classLoader = getClass().getClassLoader();

        File abbreviationsFile = new File(classLoader.getResource("abbreviations.txt").getFile());
        File startLogFile = new File(classLoader.getResource("start.log").getFile());
        File incorrectLogFile = new File(classLoader.getResource("incorrectData.log").getFile());

        RacersTimer racersTimer = new RacersTimer();
        Assertions.assertThrows(FileProcessingException.class, () -> {
            racersTimer.getLeaderboard(abbreviationsFile, startLogFile, incorrectLogFile);
        });
    }

    @Test
    void TestGetLeaderboard_ShouldThrowsFileProcessingException_WhenInputLogFilesHaveEmptyStrings() {
        ClassLoader classLoader = getClass().getClassLoader();

        File abbreviationsFile = new File(classLoader.getResource("abbreviations.txt").getFile());
        File startLogFile = new File(classLoader.getResource("start.log").getFile());
        File incorrectLogFile = new File(classLoader.getResource("wasteEmptyStrings.log").getFile());

        RacersTimer racersTimer = new RacersTimer();
        Assertions.assertThrows(FileProcessingException.class, () -> {
            racersTimer.getLeaderboard(abbreviationsFile, startLogFile, incorrectLogFile);
        });
    }

}
