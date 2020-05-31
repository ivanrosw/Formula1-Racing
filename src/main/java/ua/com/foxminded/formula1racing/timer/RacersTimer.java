package ua.com.foxminded.formula1racing.timer;

import ua.com.foxminded.formula1racing.exceptions.*;
import ua.com.foxminded.formula1racing.raceobjects.Racer;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.stream.Collectors.*;

public class RacersTimer {

    private static final String TIMESTAMP_PATTERN = "yyyy-MM-dd_HH:mm:ss.SSS";
    private static final String TIME_PATTERN = "mm:ss.SSS";
    private static final String NEXT_STRING_PATTERN = "\n";
    private static final String LETTERS_REGEX = "[A-Za-z\\s]+";
    private static final int TOP_LEADERS_SIZE = 15;
    private static final int ABBREVIATIONS_PARTS = 3;

    public String getLeaderboard(File abbreviationsFile, File startLogFile, File endLogFile) {
        try {
            validateFileExistanse(abbreviationsFile);
            validateFileExistanse(startLogFile);
            validateFileExistanse(endLogFile);
        } catch (FileNotFoundException e) {
            throw new FileProcessingException("Check files failed", e);
        }

        Map<String, Date> startTimeLog = getLogTime(startLogFile);
        Map<String, Date> endTimeLog = getLogTime(endLogFile);
        Map<String, Racer> racersAbbreviations = getAbbreviationsMap(abbreviationsFile);

        Map<String, Long> raceTime = calculateRacersTime(startTimeLog, endTimeLog);
        raceTime = sortRaceMap(raceTime);

        Map<String, Date> resultRaceTime = convertLongToDateMap(raceTime);

        return printLeaderboard(resultRaceTime, racersAbbreviations);
    }

    private void validateFileExistanse(File workFile) throws FileNotFoundException {
        if (!workFile.exists()) {
            throw new FileNotFoundException(workFile.getName() + " file not found");
        }
    }

    private Map<String, Racer> getAbbreviationsMap(File abbreviationsFile) {
        try (BufferedReader abbreviationsReader = new BufferedReader(new FileReader(abbreviationsFile))) {
            Map<String, Racer> result = new HashMap<>();
            abbreviationsReader.lines().forEach(line -> {
                String[] abbreviation = line.split("_");
                if (abbreviation.length != ABBREVIATIONS_PARTS) {
                    throw new FileProcessingException("Get abbreviation data failed, wrong file");
                }
                result.merge(abbreviation[0], new Racer(abbreviation[1], abbreviation[2]), (oldValue, newValue) -> oldValue);
            });
            return result;
        } catch (IOException e) {
            throw new FileProcessingException("Internal error", e);
        }
    }

    private Map<String, Date> getLogTime(File timeLogFile) {
        Map<String, Date> result = new HashMap<>();
        try (BufferedReader timeLogReader = new BufferedReader(new FileReader(timeLogFile))) {
            timeLogReader.lines().forEach(line -> {
                result.putAll(getTimeFromLine(line));
            });
            return result;
        } catch (IOException e) {
            throw new FileProcessingException("Internal error", e);
        }
    }

    private Map<String, Long> calculateRacersTime(Map<String, Date> startTimeLog, Map<String, Date> endTimeLog) {
        if (startTimeLog.size() != endTimeLog.size()) {
            throw new CalculateException("Start log and End log have different size");
        }
        checkMapsAbbreviations(startTimeLog, endTimeLog);

        Map<String, Long> result = new HashMap<>();
        startTimeLog.forEach((abbreviation, dateBegin) -> {
            result.putAll(calculateTimeDifferents(abbreviation, dateBegin, endTimeLog));
        });
        return result;
    }

    private Map<String, Long> sortRaceMap(Map<String, Long> raceMap) {
        return raceMap
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (value1, value2) -> value2, LinkedHashMap::new));
    }

    private Map<String, Date> convertLongToDateMap(Map<String, Long> raceTime) {
        Map<String, Date> result = new LinkedHashMap<>();
        raceTime.forEach((abbreviation, longTime) -> {
            result.put(abbreviation, calculateLongToDate(longTime));
        });
        return result;
    }

    private String printLeaderboard(Map<String, Date> resultRaceTime, Map<String, Racer> racersAbbreviations) {
        StringBuilder result = new StringBuilder();
        List<String> abbreviationsTop = new LinkedList<>(resultRaceTime.keySet());
        resultRaceTime.forEach((abbreviation, time) -> {
            result.append(buildRacerResult(abbreviation, time, abbreviationsTop, racersAbbreviations));
        });
        return result.toString();
    }

    private String pad(String symbol, int amount) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < amount; i++) {
            result.append(symbol);
        }
        return result.toString();
    }

    private Map<String, Date> getTimeFromLine(String line) {
        if (line.isEmpty()) {
            throw new FileProcessingException("Log file have empty strings");
        }

        Map<String, Date> result = new HashMap<>();
        String lineDate = line.replaceAll(LETTERS_REGEX, "");
        String firstDigit = lineDate.substring(0, 1);
        String abbreviation = line.substring(0, line.indexOf(firstDigit));

        try {
            Date time = new SimpleDateFormat(TIMESTAMP_PATTERN).parse(lineDate);
            result.put(abbreviation, time);
        } catch (ParseException e) {
            throw new FileProcessingException("Log file parsing error", e);
        }
        return result;
    }

    private void checkMapsAbbreviations(Map<String, Date> startTimeLog, Map<String, Date> endTimeLog) {
        startTimeLog.forEach((abbreviation, time) -> {
            if (!endTimeLog.containsKey(abbreviation)) {
                throw new FileProcessingException("Problem with abbreviations in log files");
            }
        });
    }

    private Map<String, Long> calculateTimeDifferents(String abbreviation, Date dateBegin, Map<String, Date> endTimeLog) {
        Map<String, Long> result = new HashMap<>();
        Long timeDifferents;

        if (dateBegin.getTime() > endTimeLog.get(abbreviation).getTime()) {
            timeDifferents = dateBegin.getTime() - endTimeLog.get(abbreviation).getTime();
        } else {
            timeDifferents = endTimeLog.get(abbreviation).getTime() - dateBegin.getTime();
        }

        result.put(abbreviation, timeDifferents);
        return result;
    }

    private Date calculateLongToDate(Long longTime) {
        Long minutes = longTime / (1000 * 60);
        Long seconds = (longTime / 1000) % 60;
        int milliseconds = (int) (longTime - minutes * 1000 * 60 - seconds * 1000);
        String resultTime = minutes.intValue() + ":" + seconds.intValue() + "." + milliseconds;
        try {
            return new SimpleDateFormat(TIME_PATTERN).parse(resultTime);
        } catch (ParseException e) {
            throw new CalculateException("Convert Long time to Date time is failed. Internal error", e);
        }
    }

    private String buildRacerResult(String abbreviation, Date time, List<String> abbreviationsTop, Map<String, Racer> racersAbbreviations) {
        StringBuilder result = new StringBuilder();
        int topNumber = abbreviationsTop.indexOf(abbreviation) + 1;
        result.append(String.format("%-22s| ", topNumber + ". " + racersAbbreviations.get(abbreviation).getName()));
        result.append(String.format("%-30s| ", racersAbbreviations.get(abbreviation).getCarName()));
        result.append(new SimpleDateFormat(TIME_PATTERN).format(time) + NEXT_STRING_PATTERN);
        if (topNumber == TOP_LEADERS_SIZE) {
            int stringLength = result.substring(0, result.indexOf(NEXT_STRING_PATTERN)).length();
            result.append(pad("-", stringLength));
            result.append(NEXT_STRING_PATTERN);
        }
        return result.toString();
    }

}
