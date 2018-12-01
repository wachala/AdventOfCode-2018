package pl.wachala.adventofcode.day1;

import pl.wachala.adventofcode.AdventOfCodeSolution;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ChronalCalibration implements AdventOfCodeSolution<Long> {

    public Long partOne(String input) {
        return parseInput(input).stream()
                .mapToLong(a -> a)
                .sum();
    }

    public Long partTwo(String input) {
        List<Long> tokens = parseInput(input);
        return findFirstDuplicate(tokens);
    }

    private List<Long> parseInput(String input) {
        return Arrays.stream(input.replaceAll(" ", "")
                .replace("+", "")
                .split("\n"))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    private Long findFirstDuplicate(List<Long> tokens) {
        long currentValue = 0;
        Set<Long> pastResults = new HashSet<>();

        while (true) {
            for (Long token : tokens) {
                currentValue += token;

                if (pastResults.contains(currentValue))
                    return currentValue;

                pastResults.add(currentValue);
            }
        }
    }
}
