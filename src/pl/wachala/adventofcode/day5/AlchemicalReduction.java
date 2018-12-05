package pl.wachala.adventofcode.day5;

import pl.wachala.adventofcode.AdventOfCodeSolution;

import java.util.Stack;

public class AlchemicalReduction implements AdventOfCodeSolution<Integer, Integer> {
    private static int REACTING_POLYMERS_DIFFERENCE = 'a' - 'A';

    public Integer partOne(String input) {
        return calculateLengthAfterFullReaction(input);
    }

    private int calculateLengthAfterFullReaction(String input) {
        Stack<Character> prefix = new Stack<>();

        int currentIndex = 0;

        while (currentIndex < input.length()) {
            char currentChar = input.charAt(currentIndex);

            if (!prefix.isEmpty() && Math.abs(prefix.peek() - currentChar) == REACTING_POLYMERS_DIFFERENCE)
                prefix.pop();
            else
                prefix.push(currentChar);

            currentIndex++;
        }

        return prefix.size();
    }

    public Integer partTwo(String input) {
        int minimumLength = Integer.MAX_VALUE;

        for (int polymerRemoved = 0; polymerRemoved < 26; polymerRemoved++) {
            char toReplace = (char) ('a' + polymerRemoved);
            char toReplace2 = (char) ('a' + polymerRemoved - REACTING_POLYMERS_DIFFERENCE);

            String inputModified = input.replaceAll("" + toReplace, "")
                    .replaceAll("" + toReplace2, "");

            minimumLength = Math.min(calculateLengthAfterFullReaction(inputModified), minimumLength);
        }

        return minimumLength;
    }

}
