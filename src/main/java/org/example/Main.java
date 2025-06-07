package org.example;

import java.util.*;

public class Main {

    public static void main (String[] args) {
        User user = new User("user1");
        CubeSide.rollCubes(user);
        user.printCubeInfo();
        Map<Integer, Integer> frequency = getSideFrequency(user.getMyCubes());
        getValueCombination(frequency);


    }

    public static void getValueCombination(Map<Integer, Integer> frequencyMap) {

        if (isPokerCombination(frequencyMap).isPresent()) {
            // add logic
            System.out.println("Poker combination");
            return;
        }

        if (isCareCombination(frequencyMap).isPresent()) {
            // add logic
            System.out.println("Care combination");
            return;
        }

        if (isFullHouse(frequencyMap).isPresent()) {
            // add logic
            System.out.println("Full House");
            return;
        }

        if (isBigStraight(frequencyMap).isPresent()) {
            // add logic
            System.out.println("Big Straight combination");
            return;
        }

        if (isSmallStraight(frequencyMap).isPresent()) {
            // add logic
            System.out.println("Big Small Straight combination");
            return;
        }

        if (isThreeOfAKind(frequencyMap).isPresent()) {
            // add logic
            System.out.println("Three combination");
            return;
        }

        if (isPair(frequencyMap).isPresent()) {
            // add logic
            System.out.println("Pair combination");
            return;
        }

        if (isTwoPair(frequencyMap).isPresent()) {
            // add logic
            System.out.println("Two Pair combination");
            return;
        }

        System.out.println("Nothing");

    }

    public static Map<Integer, Integer> getSideFrequency(List<CubeSide> arrCubes) {
        Map<Integer, Integer> frequencyMap = new HashMap<>();

        for (CubeSide sideCubeValue: arrCubes) {
            int cubeSideIntValue = sideCubeValue.getValue();
            frequencyMap.put(cubeSideIntValue, frequencyMap.getOrDefault(cubeSideIntValue, 0) + 1);
        }
        return frequencyMap;
    }

    private static Optional<Integer> isPokerCombination(Map<Integer, Integer> map) {
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 5) {
                return Optional.of(entry.getKey());
            }
        }

        return Optional.empty();
    }

    private static Optional<Integer> isCareCombination(Map<Integer, Integer> map) {
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 4) {
                return Optional.of(entry.getKey());
            }
        }

        return Optional.empty();
    }

    private static Optional<FullHouseInfo> isFullHouse(Map<Integer, Integer> map) {
        int tripleValue = -1;
        int pairValue = -1;

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 3) {
                tripleValue = entry.getKey();
            } else if (entry.getValue() == 2) {
                pairValue = entry.getKey();
            }
        }

        if (tripleValue != -1 && pairValue != -1) {
            return Optional.of(new FullHouseInfo(tripleValue, pairValue));
        }

        return Optional.empty();
    }

    private static Optional<Boolean> isBigStraight(Map<Integer, Integer> map) {
       if (map.size() != 5) {
           return Optional.empty();
       }
       Set<Integer> sideValues = new HashSet<>();
       for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() != 1) {
                return Optional.empty();
            }
            sideValues.add(entry.getKey());
       }

       if (sideValues.containsAll(Arrays.asList(2, 3, 4, 5, 6))) {
            return Optional.of(true);
       }

       return Optional.empty();
    }
    private static Optional<Boolean> isSmallStraight(Map<Integer, Integer> map) {
        if (map.size() != 5) {
            return Optional.empty();
        }
        Set<Integer> sideValues = new HashSet<>();
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() != 1) {
                return Optional.empty();
            }
            sideValues.add(entry.getKey());
        }

        if (sideValues.containsAll(Arrays.asList(1, 2, 3, 4, 5))) {
            return Optional.of(true);
        }

        return Optional.empty();
    }

    private static Optional<Integer> isThreeOfAKind(Map<Integer, Integer> map) {
       for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
           if (entry.getValue() == 3) {
               return Optional.of(entry.getValue());
           }
       }

       return Optional.empty();
    }

    private static Optional<TwoPairInfo> isTwoPair(Map<Integer, Integer> map) {
        List<Integer> pairs = new ArrayList<>();
        int kicker = -1;

        for (Map.Entry<Integer, Integer> entry: map.entrySet()) {
            if (entry.getValue() == 2) {
                pairs.add(entry.getKey());
            } else if (entry.getValue() == 1) {
                kicker = entry.getKey();
            }
        }

        if (pairs.size() == 2 && kicker != -1) {
            pairs.sort(Collections.reverseOrder());
            return Optional.of(new TwoPairInfo(pairs.get(0), pairs.get(1), kicker));
        }
        return Optional.empty();
    }

    private static Optional<Integer> isPair(Map<Integer, Integer> map) {
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 2) {
                return Optional.of(entry.getValue());
            }
        }
        return Optional.empty();
    }


}