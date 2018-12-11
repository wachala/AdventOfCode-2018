package pl.wachala.adventofcode.day9;

import pl.wachala.adventofcode.AdventOfCodeSolution;

import java.util.ArrayDeque;
import java.util.Deque;

public class MarbleMania implements AdventOfCodeSolution<Long, Long> {

    @Override
    public Long partOne(String input) {
        int[] inputParsed = parse(input);
        int noPlayers = inputParsed[0];
        int lastMarbleWorth = inputParsed[1];

        return getWinningElfScore(noPlayers, lastMarbleWorth);
    }

    @Override
    public Long partTwo(String input) {
        int[] inputParsed = parse(input);
        int noPlayers = inputParsed[0];
        int lastMarbleWorth = inputParsed[1];

        return getWinningElfScore(noPlayers, lastMarbleWorth * 100);
    }

    private long getWinningElfScore(int noPlayers, long lastMarbleWorth) {
        long playersScore[] = new long[noPlayers];

        Deque<Integer> circle = new ArrayDeque<>();
        circle.addFirst(0);

        for (int currentMarbleNo = 1; currentMarbleNo <= lastMarbleWorth; currentMarbleNo++) {
            if (currentMarbleNo % 23 == 0) {
                rotateDequeue(circle, -7);
                playersScore[currentMarbleNo % noPlayers] += currentMarbleNo + circle.pop();
            } else {
                rotateDequeue(circle, 2);
                circle.addLast(currentMarbleNo);
            }
        }

        long max = Integer.MIN_VALUE;

        for (long n : playersScore)
            max = Math.max(n, max);

        return max;
    }

    private void rotateDequeue(Deque<Integer> circle, int rotationFactor) {
        if (rotationFactor >= 0) {
            for (int i = 0; i < rotationFactor; i++)
                circle.addFirst(circle.removeLast());
        } else {
            for (int i = 0; i < (-rotationFactor) - 1; i++)
                circle.addLast(circle.remove());
        }
    }

    private int[] parse(String input) {
        String[] split = input.split(" ");
        return new int[]{Integer.parseInt(split[0]), Integer.parseInt(split[6])};
    }

}
