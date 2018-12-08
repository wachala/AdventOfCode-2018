package pl.wachala.adventofcode.day8;

import pl.wachala.adventofcode.AdventOfCodeSolution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MemoryManeuver implements AdventOfCodeSolution<Long, Long> {
    private int index;
    private List<Integer> numbers;

    class Node {
        int metadata[];
        List<Node> children;

        long getValue() {
            long value = 0;

            if (children.size() > 0) {
                for (int m : metadata) {
                    if (children.size() > m - 1)
                        value += children.get(m - 1).getValue();
                }
            } else {
                for (int m : metadata)
                    value += m;
            }
            return value;
        }

    }

    class Pair {
        long sum;
        int index;

        Pair(int sum, int index) {
            this.sum = sum;
            this.index = index;
        }
    }

    @Override
    public Long partOne(String input) {
        List<Integer> numbers = parse(input);

        Pair pair = buildTree(numbers, 0);

        return pair.sum;
    }

    @Override
    public Long partTwo(String input) {
        numbers = parse(input);

        index = 0;
        Node root = buildTreeNodes();

        return root.getValue();
    }

    private Node buildTreeNodes() {
        Node result = new Node();

        int noChildren = numbers.get(index++);
        int noMetadata = numbers.get(index++);

        result.metadata = new int[noMetadata];
        result.children = new ArrayList<>(noChildren);

        if (noChildren != 0)
            while (noChildren-- > 0)
                result.children.add(buildTreeNodes());

        while (noMetadata > 0)
            result.metadata[--noMetadata] = numbers.get(index++);

        return result;
    }

    private List<Integer> parse(String input) {
        return Arrays.stream(input.split(" "))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private Pair buildTree(List<Integer> input, int i) {
        if (i > input.size() - 1)
            return new Pair(0, i);

        int noChildren = input.get(i++);
        int noMetadata = input.get(i++);

        int metadataSum = 0;

        if (noChildren != 0) {
            for (int k = 0; k < noChildren; k++) {
                Pair partial = buildTree(input, i);

                metadataSum += partial.sum;
                i = partial.index;
            }
        }

        while (noMetadata > 0) {
            noMetadata--;
            metadataSum += input.get(i++);
        }

        return new Pair(metadataSum, i);
    }

}
