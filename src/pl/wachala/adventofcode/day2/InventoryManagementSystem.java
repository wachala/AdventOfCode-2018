package pl.wachala.adventofcode.day2;

import pl.wachala.adventofcode.AdventOfCodeSolution;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryManagementSystem implements AdventOfCodeSolution<Long, String> {

    private class Result {
        boolean twoSameCharacters;
        boolean threeSameCharacters;

        Result(boolean twoSameCharacters, boolean threeSameCharacters) {
            this.twoSameCharacters = twoSameCharacters;
            this.threeSameCharacters = threeSameCharacters;
        }
    }

    @Override
    public Long partOne(String input) {
        List<String> inputParsed = parseInput(input);

        long twice = 0;
        long threeTimes = 0;

        for (String s : inputParsed) {
            Result result = processId(s);
            if (result.twoSameCharacters) twice++;
            if (result.threeSameCharacters) threeTimes++;
        }

        return twice * threeTimes;
    }

    @Override
    public String partTwo(String input) {
        List<String> inputParsed = parseInput(input);

        for (int i = 0; i < inputParsed.size() - 1; i++) {
            String first = inputParsed.get(i);

            for (int j = i + 1; j < inputParsed.size(); j++) {
                StringBuilder builder = new StringBuilder();
                String second = inputParsed.get(j);
                int differentChars = 0;

                for (int k = 0; k < first.length(); k++) {
                    if (first.charAt(k) != second.charAt(k)) {
                        differentChars++;

                        if (differentChars > 1)
                            break;
                    } else
                        builder.append(first.charAt(k));
                }

                if (differentChars == 1)
                    return builder.toString();
            }
        }

        return null;
    }

    private Result processId(String id) {
        Map<Character, Integer> occurrences = new HashMap<>();
        int occurringTwice = 0;
        int occurringThreeTimes = 0;

        for (char c : id.toCharArray()) {
            int currentCount = occurrences.getOrDefault(c, 0) + 1;

            if (currentCount == 2) {
                occurringTwice++;
            } else if (currentCount == 3) {
                occurringTwice--;
                occurringThreeTimes++;
            } else if (currentCount == 4) {
                occurringThreeTimes--;
            }

            occurrences.put(c, currentCount);
        }

        return new Result(occurringTwice > 0, occurringThreeTimes > 0);
    }

    private List<String> parseInput(String input) {
        return Arrays.asList(input.replaceAll(" ", "")
                .split("\n"));
    }

}
