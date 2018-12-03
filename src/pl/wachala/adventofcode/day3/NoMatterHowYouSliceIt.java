package pl.wachala.adventofcode.day3;

import pl.wachala.adventofcode.AdventOfCodeSolution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NoMatterHowYouSliceIt implements AdventOfCodeSolution<Long, Integer> {
    private final int MAX_X = 1000;
    private final int MAX_Y = 1000;

    class Rectangle {
        int id;
        int x;
        int y;
        int width;
        int height;
    }

    @Override
    public Long partOne(String input) {
        long usedMoreThanOnce = 0;
        int fabric[][] = new int[MAX_Y][MAX_X];

        List<Rectangle> parse = parse(input);

        for (Rectangle r : parse) {
            for (int dy = 0; dy < r.height; dy++) {
                for (int dx = 0; dx < r.width; dx++) {
                    fabric[r.y + dy][r.x + dx]++;

                    if (fabric[r.y + dy][r.x + dx] == 2)
                        usedMoreThanOnce++;
                }
            }
        }

        return usedMoreThanOnce;
    }

    @Override
    public Integer partTwo(String input) {
        List<Rectangle> parse = parse(input);

        for (int i = 0; i < parse.size(); i++) {
            if (!overlapsWithAny(parse.get(i), parse, i)) {
                return parse.get(i).id;
            }
        }

        return -1;
    }

    private boolean overlapsWithAny(Rectangle rectangle, List<Rectangle> other, int index) {
        for (int i = 0; i < other.size(); i++) {
            Rectangle o = other.get(i);

            if (index != i && overlaps(rectangle, o))
                return true;
        }

        return false;
    }

    private boolean overlaps(Rectangle first, Rectangle second) {
        return second.x < first.x + first.width
                && second.x + second.width > first.x
                && second.y < first.y + first.height
                && second.y + second.height > first.y;
    }

    private List<Rectangle> parse(String input) {
        return Arrays.stream(input.replaceAll(" ", "").split("\n"))
                .map(this::buildRectangle)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private Rectangle buildRectangle(String input) {
        Rectangle result = new Rectangle();
        String[] firstSplit = input.split("@");

        Integer id = Integer.parseInt(firstSplit[0].replace("#", ""));
        String coordinates = firstSplit[1];

        String[] coordinatesSplitted = coordinates.split(":");
        String[] secondSplit = coordinatesSplitted[0].split(",");
        String[] thirdSplit = coordinatesSplitted[1].split("x");

        int x = Integer.parseInt(secondSplit[0]);
        int y = Integer.parseInt(secondSplit[1]);

        int width = Integer.parseInt(thirdSplit[0]);
        int height = Integer.parseInt(thirdSplit[1]);

        result.id = id;
        result.x = x;
        result.y = y;
        result.width = width;
        result.height = height;

        return result;
    }

}
