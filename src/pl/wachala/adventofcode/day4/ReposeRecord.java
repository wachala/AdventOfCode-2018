package pl.wachala.adventofcode.day4;

import pl.wachala.adventofcode.AdventOfCodeSolution;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class ReposeRecord implements AdventOfCodeSolution<Long, Long> {
    private enum EventType {
        START, SLEEP, WAKE_UP
    }

    class Event {
        LocalDate date;
        EventType type;
        int minutesAfterMidnight;
        int guardId;
    }

    private Map<Integer, Long> guardIdToTimeSlept = new HashMap<>();
    private Map<Integer, int[]> guardIdToMinutesFrequency = new HashMap<>();

    @Override
    public Long partOne(String input) {
        List<Event> parse = parse(input);

        return calculateTimeAsleep(parse);
    }

    @Override
    public Long partTwo(String input) {
        List<Event> parse = parse(input);

        return calculateMostFrequentSleepMinute(parse);
    }

    private Long calculateTimeAsleep(List<Event> events) {
        Long maxTimeAsleep = -1L;
        Integer currentGuardId = null;
        Integer idOfGuardWithMostMinutesOfSleep = -1;
        Integer sleepStart = null;

        for (Event e : events) {
            if (e.type == EventType.START) {
                currentGuardId = e.guardId;
            } else if (e.type == EventType.SLEEP) {
                sleepStart = e.minutesAfterMidnight;
            } else {
                if (currentGuardId == null || sleepStart == null) {
                    continue;
                }

                int sleepTime = e.minutesAfterMidnight - sleepStart;
                long guardTotalSleep = guardIdToTimeSlept.getOrDefault(currentGuardId, 0L) + sleepTime;
                guardIdToTimeSlept.put(currentGuardId, guardTotalSleep);

                if (guardTotalSleep > maxTimeAsleep) {
                    maxTimeAsleep = guardTotalSleep;
                    idOfGuardWithMostMinutesOfSleep = currentGuardId;
                }

                if (guardIdToMinutesFrequency.get(currentGuardId) == null)
                    guardIdToMinutesFrequency.put(currentGuardId, new int[60]);

                int[] minutes = guardIdToMinutesFrequency.get(currentGuardId);

                for (int i = sleepStart; i < e.minutesAfterMidnight; i++)
                    minutes[i]++;

                sleepStart = null;
            }
        }

        int[] ints = guardIdToMinutesFrequency.get(idOfGuardWithMostMinutesOfSleep);

        long maxVal = -1;
        long maxId = 0;

        for (int i = 0; i < ints.length; i++) {
            if (ints[i] > maxVal) {
                maxVal = ints[i];
                maxId = i;
            }
        }

        return maxId * idOfGuardWithMostMinutesOfSleep;
    }

    private Long calculateMostFrequentSleepMinute(List<Event> events) {
        Integer currentGuardId = null;
        long maxFrequency = 0;
        long idOfGuardWithMostFrequentMinuteAsleep = -1;
        long mostFrequentMinute = -1;

        Integer sleepStart = null;

        for (Event e : events) {
            if (e.type == EventType.START) {
                currentGuardId = e.guardId;
            } else if (e.type == EventType.SLEEP) {
                sleepStart = e.minutesAfterMidnight;
            } else {
                if (currentGuardId == null || sleepStart == null)
                    continue;

                if (guardIdToMinutesFrequency.get(currentGuardId) == null)
                    guardIdToMinutesFrequency.put(currentGuardId, new int[60]);
                int[] minutes = guardIdToMinutesFrequency.get(currentGuardId);

                for (int i = sleepStart; i < e.minutesAfterMidnight; i++) {
                    minutes[i]++;

                    if (minutes[i] > maxFrequency) {
                        maxFrequency = minutes[i];
                        idOfGuardWithMostFrequentMinuteAsleep = currentGuardId;
                        mostFrequentMinute = i;
                    }
                }

                sleepStart = null;
            }
        }

        return mostFrequentMinute * idOfGuardWithMostFrequentMinuteAsleep;
    }

    private List<Event> parse(String input) {
        return Arrays.stream(input.split("\n"))
                .sorted()
                .map(this::toEvent)
                .collect(Collectors.toList());
    }

    private Event toEvent(String s) {
        Event e = new Event();

        String[] split = s.split("]");
        String[] dateTime = split[0].split(" ");

        LocalDate date = LocalDate.parse(dateTime[0].replaceAll("\\[", ""));
        int time = Integer.parseInt(dateTime[1].split(":")[1]);

        e.date = date;
        e.minutesAfterMidnight = time;

        if (split[1].contains("Guard")) {
            e.type = EventType.START;
            e.guardId = Integer.parseInt(split[1].split("#")[1].split(" ")[0]);
        } else if (split[1].contains("wakes")) {
            e.type = EventType.WAKE_UP;
        } else {
            e.type = EventType.SLEEP;
        }

        return e;
    }

}