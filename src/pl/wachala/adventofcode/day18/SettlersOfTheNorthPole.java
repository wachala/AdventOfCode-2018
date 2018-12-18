package pl.wachala.adventofcode.day18;

import pl.wachala.adventofcode.AdventOfCodeSolution;

import java.util.HashMap;
import java.util.Map;

public class SettlersOfTheNorthPole implements AdventOfCodeSolution<Long, Long> {
    private int BOARD_SIZE = 50;

    @Override
    public Long partOne(String input) {
        char board[][] = parseInput(input);
        int iterations = 1000;

        for (int i = 0; i < iterations; i++) {
            board = simulate(board);
        }

        return getResources(board);
    }

    @Override
    public Long partTwo(String input) {
        char board[][] = parseInput(input);
        long resourceTable[] = new long[28];
        int cycleStart = 547;

        for (int i = 1; i < cycleStart + 28; i++) {
            board = simulate(board);

            if (i >= 547) {
                Long resources = getResources(board);
                resourceTable[i - 547] = resources;
            }
        }

        return resourceTable[(1000000000 - 547) % 28];
    }

    private Long getResources(char board[][]) {
        Map<Character, Integer> count = new HashMap<>();

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                char c = board[i][j];
                count.put(c, count.getOrDefault(c, 0) + 1);
            }
        }

        return Long.valueOf(count.getOrDefault('|', 0)) * count.getOrDefault('#', 0);
    }

    private char[][] simulate(char board[][]) {
        char newBoard[][] = new char[BOARD_SIZE][BOARD_SIZE];

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Map<Character, Integer> neighbours = getNeighbours(board, i, j);

                if (board[i][j] == '.') {
                    if (neighbours.getOrDefault('|', 0) >= 3)
                        newBoard[i][j] = '|';
                    else
                        newBoard[i][j] = '.';
                } else if (board[i][j] == '|') {
                    if (neighbours.getOrDefault('#', 0) >= 3)
                        newBoard[i][j] = '#';
                    else
                        newBoard[i][j] = '|';
                } else if (board[i][j] == '#') {
                    if (neighbours.getOrDefault('#', 0) >= 1
                            && neighbours.getOrDefault('|', 0) >= 1)
                        newBoard[i][j] = '#';
                    else
                        newBoard[i][j] = '.';
                }
            }
        }

        return newBoard;
    }

    private Map<Character, Integer> getNeighbours(char board[][], int x, int y) {
        Map<Character, Integer> result = new HashMap<>();

        if (x - 1 >= 0) {
            addToMap(result, board[x - 1][y]);

            if (y - 1 >= 0)
                addToMap(result, board[x - 1][y - 1]);
            if (y + 1 < BOARD_SIZE)
                addToMap(result, board[x - 1][y + 1]);
        }

        if (y - 1 >= 0)
            addToMap(result, board[x][y - 1]);
        if (y + 1 < BOARD_SIZE)
            addToMap(result, board[x][y + 1]);

        if (x + 1 < BOARD_SIZE) {
            addToMap(result, board[x + 1][y]);

            if (y - 1 >= 0)
                addToMap(result, board[x + 1][y - 1]);
            if (y + 1 < BOARD_SIZE)
                addToMap(result, board[x + 1][y + 1]);
        }

        return result;
    }

    private void addToMap(Map<Character, Integer> map, char c) {
        map.put(c, map.getOrDefault(c, 0) + 1);
    }

    private char[][] parseInput(String input) {
        char board[][] = new char[BOARD_SIZE][BOARD_SIZE];

        String inputSplit[] = input.split("\n");

        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++)
                board[i][j] = inputSplit[i].charAt(j);

        return board;
    }
}
