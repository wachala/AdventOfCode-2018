package pl.wachala.adventofcode.day7;

import pl.wachala.adventofcode.AdventOfCodeSolution;

import java.util.*;

public class TheSumOfItsParts implements AdventOfCodeSolution<String, Integer> {
    private final int NO_WORKERS = 5;
    private final int NO_TASK_ASSIGNED = -1;
    private final int ALPHABET_SIZE = 26;
    private boolean taskPresent[];
    private int workerToTask[];
    private int timeRequiredForTask[];
    private int totalTimeRequired;
    private Map<Character, Set<Character>> taskToDependencies;

    public String partOne(String input) {
        boolean[] visited = new boolean[ALPHABET_SIZE];
        taskPresent = new boolean[ALPHABET_SIZE];
        StringBuilder builder = new StringBuilder();
        taskToDependencies = parseInput(input);

        int processed = 0;
        int toProcess = getNoTaskToProcess(taskPresent);

        while (processed != toProcess) {
            for (int i = 0; i < ALPHABET_SIZE; i++) {
                if (taskPresent[i] && !visited[i] && hasNoDependencies(i, taskToDependencies, visited)) {
                    visited[i] = true;
                    builder.append((char) ('A' + i));
                    processed++;
                    break;
                }
            }
        }

        return builder.toString();
    }

    public Integer partTwo(String input) {
        String taskOrder = partOne(input);
        timeRequiredForTask = new int[ALPHABET_SIZE];
        workerToTask = new int[5];

        Queue<Character> queue = new PriorityQueue<>();
        for (char c : taskOrder.toCharArray()) {
            queue.offer(c);
        }

        initWorkers(workerToTask);
        initTimeRequirements(timeRequiredForTask);

        while (!queue.isEmpty()) {
            List<Character> toBeAddedToQueue = new LinkedList<>();
            boolean addedToQueue = false;

            while (!queue.isEmpty()) {
                Character newTask = queue.poll();

                if (allDependenciesDone(newTask)) {
                    Integer availableWorkerId = workerAvailable();

                    if (availableWorkerId != null) {
                        workerToTask[availableWorkerId] = newTask - 'A';
                        addedToQueue = true;
                    } else
                        toBeAddedToQueue.add(newTask);
                } else {
                    toBeAddedToQueue.add(newTask);
                }
            }

            if (!addedToQueue)
                finishTaskInProgress();

            queue.addAll(toBeAddedToQueue);
        }

        return totalTimeRequired + getMaxTimeRemaining(timeRequiredForTask);
    }

    private int getNoTaskToProcess(boolean taskPresent[]) {
        int toProcess = 0;

        for (int i = 0; i < ALPHABET_SIZE; i++)
            if (taskPresent[i]) toProcess++;

        return toProcess;
    }

    private void initTimeRequirements(int timeRequired[]) {
        for (int i = 0; i < ALPHABET_SIZE; i++)
            if (taskPresent[i]) timeRequired[i] = 60 + i + 1;
    }

    private void initWorkers(int workerToTask[]) {
        for (int i = 0; i < NO_WORKERS; i++)
            workerToTask[i] = NO_TASK_ASSIGNED;
    }

    private int getMaxTimeRemaining(int timeRequiredForTask[]) {
        int maxTimeRemaining = 0;

        for (int time : timeRequiredForTask)
            maxTimeRemaining = Math.max(time, maxTimeRemaining);

        return maxTimeRemaining;
    }

    private void finishTaskInProgress() {
        int minTime = Integer.MAX_VALUE;

        for (int i = 0; i < NO_WORKERS; i++) {
            int taskId = workerToTask[i];
            if (taskId > NO_TASK_ASSIGNED)
                minTime = Math.min(timeRequiredForTask[taskId], minTime);
        }

        for (int i = 0; i < NO_WORKERS; i++) {
            int taskId = workerToTask[i];

            if (taskId > NO_TASK_ASSIGNED) {
                timeRequiredForTask[taskId] -= minTime;

                if (timeRequiredForTask[taskId] == 0)
                    workerToTask[i] = NO_TASK_ASSIGNED;
            }
        }

        totalTimeRequired += minTime;
    }

    private boolean allDependenciesDone(char taskId) {
        Set<Character> taskDependencies = taskToDependencies.get(taskId);

        if (taskDependencies == null)
            return true;

        for (Character d : taskDependencies)
            if (timeRequiredForTask[d - 'A'] > 0)
                return false;

        return true;
    }

    private Integer workerAvailable() {
        for (int i = 0; i < NO_WORKERS; i++) {
            if (workerToTask[i] == NO_TASK_ASSIGNED)
                return i;
        }
        return null;
    }

    private boolean hasNoDependencies(int i, Map<Character, Set<Character>> taskToDependencies, boolean[] visited) {
        char c = (char) ('A' + i);
        Set<Character> dependencies = taskToDependencies.get(c);

        if (dependencies == null) {
            return true;
        }
        for (char d : dependencies) {
            if (!visited[d - 'A'])
                return false;
        }

        return true;
    }

    private Map<Character, Set<Character>> parseInput(String input) {
        Map<Character, Set<Character>> result = new HashMap<>();

        for (String line : input.split("\n")) {
            String lineSplited[] = line.split(" ");
            Character parent = lineSplited[1].charAt(0);
            Character child = lineSplited[7].charAt(0);

            Set<Character> dependencies = result.getOrDefault(child, new TreeSet<>());

            taskPresent[child - 'A'] = true;
            taskPresent[parent - 'A'] = true;

            dependencies.add(parent);
            result.put(child, dependencies);
        }

        return result;
    }
}
