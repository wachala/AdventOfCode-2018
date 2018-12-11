package pl.wachala.adventofcode.day10;

import pl.wachala.adventofcode.AdventOfCodeSolution;

import java.util.*;


public class TheStarsAlign implements AdventOfCodeSolution<Long, Long> {
    class Particle {
        long x;
        long y;
        long velocityX;
        long velocityY;
    }

    @Override
    public Long partOne(String input) {
        return findMessage(input, true);
    }

    @Override
    public Long partTwo(String input) {
        return findMessage(input, false);
    }

    private long findMessage(String input, boolean printMessage) {
        List<Particle> particles = parseInput(input);
        boolean textFound = false;
        int textHeight = 10;
        long iterationsRequired = 0;

        while (!textFound) {
            long minX = Integer.MAX_VALUE;
            long maxX = Integer.MIN_VALUE;
            long minY = Integer.MAX_VALUE;
            long maxY = Integer.MIN_VALUE;

            for (Particle p : particles) {
                p.x = p.x + p.velocityX;
                p.y = p.y + p.velocityY;

                minX = Math.min(minX, p.x);
                minY = Math.min(minY, p.y);
                maxX = Math.max(maxX, p.x);
                maxY = Math.max(maxY, p.y);
            }

            if (Math.abs(maxY - minY) <= textHeight) {
                if (printMessage) {
                    Map<Long, Set<Long>> pointsByLine = new HashMap<>();

                    for (Particle p : particles) {
                        long x = p.x;
                        long y = p.y;

                        Set<Long> setOfXs = pointsByLine.getOrDefault(y, new HashSet<>());
                        setOfXs.add(x);

                        pointsByLine.put(y, setOfXs);
                    }

                    for (long i = minY; i <= maxY; i++) {
                        Set<Long> xs = pointsByLine.getOrDefault(i, new HashSet<>());

                        for (long j = minX; j <= maxX; j++) {
                            if (xs.contains(j))
                                System.out.print("#");
                            else
                                System.out.print(".");
                        }
                        System.out.println();
                    }
                }
                textFound = true;
            }
            iterationsRequired++;
        }

        return iterationsRequired;
    }

    private List<Particle> parseInput(String input) {
        List<Particle> result = new ArrayList<>();
        String lines[] = input.split("\n");

        for (String line : lines) {
            String parts[] = line.split(">");

            String positionPart = parts[0].split("<")[1];
            String velocityPart = parts[1].split("<")[1];

            String posX = positionPart.split(",")[0].trim();
            String posY = positionPart.split(",")[1].trim();
            String velX = velocityPart.split(",")[0].trim();
            String velY = velocityPart.split(",")[1].trim();

            Particle p = new Particle();
            p.x = Long.parseLong(posX);
            p.y = Long.parseLong(posY);
            p.velocityX = Long.parseLong(velX);
            p.velocityY = Long.parseLong(velY);

            result.add(p);
        }

        return result;
    }

}
