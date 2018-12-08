package pl.wachala.adventofcode.day6;

import pl.wachala.adventofcode.AdventOfCodeSolution;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChronalCoordinates implements AdventOfCodeSolution<Long, Long> {

    class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    @Override
    public Long partOne(String input) {
        List<Point> points = parse(input);

        int maxX = getMaxX(points);
        int maxY = getMaxY(points);
        int grid[][] = new int[maxY + 1][maxX + 1];
        Map<Integer, Long> pointToNoCells = new HashMap<>();

        for (int i = 0; i <= maxY; i++) {
            for (int j = 0; j <= maxX; j++) {

                int lowestDistance = Integer.MAX_VALUE;
                int pointWithLowestDistance = -1;
                Point current = new Point(j, i);

                for (int k = 0; k < points.size(); k++) {
                    Point p = points.get(k);

                    int distance = calculateManhattanDistance(current, p);

                    if (distance < lowestDistance) {
                        lowestDistance = distance;
                        pointWithLowestDistance = k;
                    } else if (distance == lowestDistance) {
                        pointWithLowestDistance = -1;
                    }
                }

                grid[i][j] = pointWithLowestDistance;
                Long cellsForPoint = pointToNoCells.getOrDefault(pointWithLowestDistance, 0L) + 1;
                pointToNoCells.put(pointWithLowestDistance, cellsForPoint);
            }
        }

        for (int i = 0; i < maxX; i++) {
            int leftInfRegion = grid[0][i];
            int rightInfRegion = grid[maxY][i];

            pointToNoCells.remove(leftInfRegion);
            pointToNoCells.remove(rightInfRegion);
        }

        for (int i = 0; i < maxY; i++) {
            int topInfRegion = grid[i][0];
            int bottomInfRegion = grid[i][maxX];

            pointToNoCells.remove(topInfRegion);
            pointToNoCells.remove(bottomInfRegion);
        }

        return pointToNoCells.values()
                .stream()
                .mapToLong(s -> s)
                .max()
                .orElse(0L);
    }


    private int calculateManhattanDistance(Point first, Point second) {
        return Math.abs(first.x - second.x) + Math.abs(first.y - second.y);
    }

    private int getMaxX(List<Point> points) {
        return points.stream()
                .mapToInt(p -> p.x)
                .max()
                .orElse(0);
    }

    private int getMaxY(List<Point> points) {
        return points.stream()
                .mapToInt(p -> p.y)
                .max()
                .orElse(0);
    }


    @Override
    public Long partTwo(String input) {
        List<Point> points = parse(input);
        int maxX = getMaxX(points);
        int maxY = getMaxY(points);
        long matchingPoints = 0;

        for (int i = 0; i <= maxY; i++) {
            for (int j = 0; j <= maxX; j++) {
                Point current = new Point(j, i);

                long sumDistance = points.stream()
                        .map(p -> calculateManhattanDistance(current, p))
                        .mapToLong(s -> s)
                        .sum();

                long THRESHOLD = 10000L;
                if (sumDistance < THRESHOLD)
                    matchingPoints++;
            }
        }

        return matchingPoints;
    }

    private List<Point> parse(String input) {
        return Arrays.stream(input.split("\n"))
                .map(line -> line.replaceFirst(" ", ""))
                .map(line -> {
                    String split[] = line.split(",");
                    return new Point(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
                })
                .collect(Collectors.toList());
    }
}
