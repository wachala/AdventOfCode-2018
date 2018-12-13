package pl.wachala.adventofcode.day11;

import pl.wachala.adventofcode.AdventOfCodeSolution;

public class ChronalCharge implements AdventOfCodeSolution<String, String> {
    class Result {
        int x;
        int y;
        int size;
        long maxEnergy;

        Result(int x, int y, int size, long maxEnergy) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.maxEnergy = maxEnergy;
        }
    }

    private int GRID_SIZE = 300;

    @Override
    public String partOne(String input) {
        Integer serialNumber = Integer.parseInt(input);
        long grid[][] = new long[GRID_SIZE][GRID_SIZE];

        initGridWithEnergy(grid, serialNumber);
        Result gridWithLargestPowerOfSize = getGridWithLargestPowerOfSize(grid, 3);

        return gridWithLargestPowerOfSize.x + ", " + gridWithLargestPowerOfSize.y;
    }

    private void initGridWithEnergy(long grid[][], int serialNumber) {
        for (int i = 0; i < GRID_SIZE; i++)
            for (int j = 0; j < GRID_SIZE; j++)
                grid[i][j] = calculateEnergy(j + 1, i + 1, serialNumber);
    }

    private Result getGridWithLargestPowerOfSize(long grid[][], int squareSize) {
        int maxX = -1;
        int maxY = -1;

        long maxEnergy = Long.MIN_VALUE;

        for (int i = 0; i < GRID_SIZE - squareSize; i++) {
            for (int j = 0; j < GRID_SIZE - squareSize; j++) {
                long squareEnergy = getSquareEnergy(j, i, grid, squareSize);

                if (maxEnergy < squareEnergy) {
                    maxEnergy = squareEnergy;
                    maxX = j + 1;
                    maxY = i + 1;
                }
            }
        }

        return new Result(maxX, maxY, squareSize, maxEnergy);
    }

    private long getSquareEnergy(int x, int y, long grid[][], int squareSize) {
        long totalEnergy = 0;

        for (int i = 0; i < squareSize; i++) {
            for (int j = 0; j < squareSize; j++)
                totalEnergy += grid[y + i][x + j];
        }
        return totalEnergy;
    }

    private long calculateEnergy(int x, int y, int serialNumber) {
        int rackId = x + 10;
        long partial = (rackId * y + serialNumber) * rackId;

        return (partial % 1000) / 100 - 5;
    }

    @Override
    public String partTwo(String input) {
        Integer serialNumber = Integer.parseInt(input);
        long grid[][] = new long[GRID_SIZE][GRID_SIZE];

        initGridWithEnergy(grid, serialNumber);

        Result res = new Result(-1, -1, 0, Long.MIN_VALUE);

        for (int i = 1; i < GRID_SIZE; i++) {
            Result gridWithLargestPowerOfSize = getGridWithLargestPowerOfSize(grid, i);

            if (gridWithLargestPowerOfSize.maxEnergy > res.maxEnergy)
                res = gridWithLargestPowerOfSize;
        }

        return res.x + "," + res.y + "," + res.size;
    }

}
