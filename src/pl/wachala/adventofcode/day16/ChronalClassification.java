package pl.wachala.adventofcode.day16;

import pl.wachala.adventofcode.AdventOfCodeSolution;

import java.util.*;

public class ChronalClassification implements AdventOfCodeSolution<Long, Integer> {

    private Map<Integer, TriFunction> numberToFunction;

    class Sample {
        int before[];
        int operation[];
        int after[];
    }

    @FunctionalInterface
    interface TriFunction {
        void apply(int A, int B, int C, int regs[]);
    }

    private List<TriFunction> operations;

    public ChronalClassification() {
        TriFunction addr = (a, b, c, reg) -> reg[c] = reg[a] + reg[b];
        TriFunction addi = (a, b, c, reg) -> reg[c] = reg[a] + b;
        TriFunction mulr = (a, b, c, reg) -> reg[c] = reg[a] * reg[b];
        TriFunction muli = (a, b, c, reg) -> reg[c] = reg[a] * b;
        TriFunction banr = (a, b, c, reg) -> reg[c] = reg[a] & reg[b];
        TriFunction bani = (a, b, c, reg) -> reg[c] = reg[a] & b;
        TriFunction borr = (a, b, c, reg) -> reg[c] = reg[a] | reg[b];
        TriFunction bori = (a, b, c, reg) -> reg[c] = reg[a] | b;
        TriFunction setr = (a, b, c, reg) -> reg[c] = reg[a];
        TriFunction seti = (a, b, c, reg) -> reg[c] = a;
        TriFunction gtir = (a, b, c, reg) -> {
            if (a > reg[b])
                reg[c] = 1;
            else
                reg[c] = 0;
        };
        TriFunction gtri = (a, b, c, reg) -> {
            if (reg[a] > b)
                reg[c] = 1;
            else
                reg[c] = 0;
        };
        TriFunction gtrr = (a, b, c, reg) -> {
            if (reg[a] > reg[b])
                reg[c] = 1;
            else
                reg[c] = 0;
        };

        TriFunction eqir = (a, b, c, reg) -> {
            if (a == reg[b])
                reg[c] = 1;
            else
                reg[c] = 0;
        };
        TriFunction eqri = (a, b, c, reg) -> {
            if (reg[a] == b)
                reg[c] = 1;
            else
                reg[c] = 0;
        };
        TriFunction eqrr = (a, b, c, reg) -> {
            if (reg[a] == reg[b])
                reg[c] = 1;
            else
                reg[c] = 0;
        };

        operations = Arrays.asList(
                addr, addi,
                mulr, muli,
                banr, bani,
                borr, bori,
                setr, seti,
                gtir, gtri, gtrr,
                eqir, eqri, eqrr
        );
    }

    @Override
    public Long partOne(String input) {
        List<Sample> samples = parseForPartOne(input);

        long globalCount = 0;

        for (Sample s : samples) {
            int count = 0;

            for (TriFunction tf : operations) {
                int beforeCopy[] = new int[s.before.length];
                System.arraycopy(s.before, 0, beforeCopy, 0, beforeCopy.length);

                tf.apply(s.operation[1], s.operation[2], s.operation[3], beforeCopy);

                if (Arrays.equals(s.after, beforeCopy)) {
                    count++;

                    if (count == 3) {
                        globalCount++;
                        break;
                    }
                }
            }
        }

        return globalCount;
    }

    @Override
    public Integer partTwo(String input) {
        List<int[]> operations = parseForPartTwo(input);
        int registers[] = new int[4];

        for (int operation[] : operations) {
            TriFunction function = numberToFunction.get(operation[0]);
            function.apply(operation[1], operation[2], operation[3], registers);
        }

        return registers[0];
    }

    private void initNumberToFunctionMap(String input) {
        List<Sample> samples = parseForPartOne(input);
        Map<TriFunction, Set<Integer>> matchingNumbers = new HashMap<>();
        Map<TriFunction, Set<Integer>> nonMatchingNumbers = new HashMap<>();

        for (TriFunction f : operations) {
            matchingNumbers.put(f, new HashSet<>());
            nonMatchingNumbers.put(f, new HashSet<>());
        }

        for (Sample s : samples) {
            for (TriFunction tf : operations) {
                int beforeCopy[] = new int[s.before.length];
                System.arraycopy(s.before, 0, beforeCopy, 0, beforeCopy.length);
                tf.apply(s.operation[1], s.operation[2], s.operation[3], beforeCopy);

                if (Arrays.equals(s.after, beforeCopy))
                    matchingNumbers.get(tf).add(s.operation[0]);
                else
                    nonMatchingNumbers.get(tf).add(s.operation[0]);
            }
        }

        int matchingMoreThanOne = 0;
        Set<Integer> matchingOnlyOne = new HashSet<>();

        for (TriFunction tf : operations) {
            Set<Integer> matching = matchingNumbers.get(tf);
            Set<Integer> nonMatching = nonMatchingNumbers.get(tf);

            matching.removeAll(nonMatching);

            if (matching.size() > 1)
                matchingMoreThanOne++;
            else
                matchingOnlyOne.add(matching.iterator().next());
        }

        while (matchingMoreThanOne > 0) {
            for (TriFunction tf : operations) {
                Set<Integer> matching = matchingNumbers.get(tf);

                if (matching.size() > 1) {
                    matching.removeAll(matchingOnlyOne);

                    if (matching.size() == 1) {
                        matchingMoreThanOne--;
                        matchingOnlyOne.add(matching.iterator().next());
                    }
                }
            }
        }

        numberToFunction = new HashMap<>();

        for (TriFunction tf : operations)
            numberToFunction.put(matchingNumbers.get(tf).iterator().next(), tf);
    }

    private List<Sample> parseForPartOne(String input) {
        List<Sample> result = new ArrayList<>();
        Sample sample = null;

        for (String line : input.split("\n")) {
            if (line.equals("")) {
                continue;
            }
            if (line.startsWith("Before")) {
                sample = new Sample();
                sample.before = retrieveArray(line);

            } else if (line.startsWith("After")) {
                sample.after = retrieveArray(line);
                result.add(sample);
            } else {
                sample.operation = retrieveArray(line.split(" "));
            }
        }

        return result;
    }

    private List<int[]> parseForPartTwo(String input) {
        String[] split = input.split("\n");
        List<int[]> result = new LinkedList<>();

        for (String line : split) {
            result.add(retrieveArray(line.split(" ")));
        }

        return result;
    }

    private int[] retrieveArray(String input) {
        String[] split = input.split(":")[1]
                .replaceAll(" ", "")
                .replaceAll("\\[", "")
                .replaceAll("]", "")
                .split(",");

        return retrieveArray(split);
    }

    private int[] retrieveArray(String preprocessed[]) {
        int res[] = new int[preprocessed.length];

        for (int i = 0; i < res.length; i++)
            res[i] = Integer.parseInt(preprocessed[i]);

        return res;
    }
    
}
