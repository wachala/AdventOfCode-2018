package pl.wachala.adventofcode;

import java.util.List;

public interface AdventOfCodeSolution<T,Q> {
    T partOne(String input);
    Q partTwo(String input);
}
