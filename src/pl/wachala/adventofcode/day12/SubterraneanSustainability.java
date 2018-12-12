package pl.wachala.adventofcode.day12;

import pl.wachala.adventofcode.AdventOfCodeSolution;

import java.util.HashSet;
import java.util.Set;

public class SubterraneanSustainability implements AdventOfCodeSolution<Long, Long> {

    private String problemInput;
    private Set<String> rulesProducingPlant;
    private int NUMBER_OF_GENERATIONS = 20;
    long zeroOffset = 0;

    @Override
    public Long partOne(String input) {
        parseInput(input);

        return simulate(NUMBER_OF_GENERATIONS);
    }

    private Long simulate(int generationNumber) {
        for (int k = 0; k < generationNumber; k++) {
            StringBuilder builder = new StringBuilder();

            String state = "...." + problemInput + "...";
            zeroOffset += 2;

            for (int i = 2; i < state.length() - 2; i++) {
                String currentSpot = state.substring(i - 2, i + 3);

                if (rulesProducingPlant.contains(currentSpot))
                    builder.append("#");
                else
                    builder.append(".");
            }

            problemInput = builder.toString();
        }

        Long plantsIndexSum = 0L;

        for (int i = 0; i < problemInput.length(); i++)
            if (problemInput.charAt(i) == '#') plantsIndexSum += i - zeroOffset;

        return plantsIndexSum;
    }

    @Override
    public Long partTwo(String input) {
        parseInput(input);

        long firstThousandIterations = simulate(1000);

        long prev = simulate(1);
        long next = simulate(1);
        long diff = next - prev;

        return firstThousandIterations + (50000000000L - 1000) * diff;
    }

    private void parseInput(String input) {
        rulesProducingPlant = new HashSet<>();
        String[] split = input.split("\n");
        problemInput = split[0].split(" ")[2];

        for (int i = 2; i < split.length; i++) {
            String currentLineSplit[] = split[i].split(" => ");
            String rule = currentLineSplit[0];
            String result = currentLineSplit[1];

            if (result.equals("#"))
                rulesProducingPlant.add(rule);
        }
    }
}
