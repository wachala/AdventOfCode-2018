package pl.wachala.adventofcode.day14;

import pl.wachala.adventofcode.AdventOfCodeSolution;

import java.util.ArrayList;
import java.util.List;

public class ChocolateCharts implements AdventOfCodeSolution<String, Integer> {
    private List<Integer> receipts;
    private int noReceipts;
    private int firstElveIndex;
    private int secondElveIndex;

    @Override
    public String partOne(String input) {
        Integer limit = Integer.parseInt(input);
        initSolution();

        while (noReceipts < limit + 10) {
            iterate();
            noReceipts = receipts.size();
        }

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < 10; i++)
            builder.append(receipts.get((limit + i) % receipts.size()));

        return builder.toString();
    }

    private void initSolution() {
        receipts = new ArrayList<>();
        receipts.add(3);
        receipts.add(7);

        noReceipts = 2;
        firstElveIndex = 0;
        secondElveIndex = 1;
    }

    private void iterate() {
        int currentScoreFirst = receipts.get(firstElveIndex);
        int currentScoreSecond = receipts.get(secondElveIndex);
        Integer nextReceipt = currentScoreFirst + currentScoreSecond;

        if (nextReceipt >= 10) {
            receipts.add(nextReceipt / 10);
            receipts.add(nextReceipt % 10);
        } else
            receipts.add(nextReceipt);

        firstElveIndex = (firstElveIndex + 1 + currentScoreFirst) % receipts.size();
        secondElveIndex = (secondElveIndex + 1 + currentScoreSecond) % receipts.size();
    }

    @Override
    public Integer partTwo(String input) {
        initSolution();
        List<Integer> inputAsList = new ArrayList<>(input.length());
        int noIterations = 25000000;

        for (int i = 0; i < input.length(); i++)
            inputAsList.add(Integer.parseInt(input.substring(i, i + 1)));

        while (noIterations > 0) {
            iterate();
            noIterations--;
        }

        for (int i = 0; i < receipts.size() - input.length(); i++) {
            boolean matched = true;

            for (int j = 0; j < input.length(); j++) {
                if (!receipts.get(i + j).equals(inputAsList.get(j))) {
                    matched = false;
                    break;
                }
            }
            if (matched)
                return i;
        }

        return -1;
    }
}
