package org.example;

import java.util.*;

import static org.example.Main.scanner;

public class Game {

    public UserChecker start(User firstUser, User secondUser) {
        CubeSide.rollCubes(firstUser, secondUser);

        CombinationResult user1Result = printingCurrentHand(firstUser);
        CombinationResult user2Result = printingCurrentHand(secondUser);

        System.out.println();
        BetSize userBet = selectBet();

        chanceToReRoll(firstUser, user1Result);
        chanceToReRoll(secondUser, user2Result);

        return playRound(firstUser, secondUser, userBet);

    }

    private CombinationResult printingCurrentHand(User user) {
        user.printCubeInfo();
        Map<Integer, Integer> arr = getSideFrequency(user.getMyCubes());
        CombinationResult combResult = getResult(arr);
        System.out.println("Combination is: " + combResult.getType() + "/" + combResult.getKeyValues());
        return combResult;
    }

    /**
     * Offer the player to choose the bet size.
     * Requests user input and checks it against available bet sizes.
     *
     * @return player choose BetSize object
     */
    private static BetSize selectBet() {
        BetSize selectedBetSize = null;
        System.out.println("Choose bet size: ");
        for (BetSize size : BetSize.values()) {
            System.out.println("- " + size.name() + " (" + size.getAmount() + " Orens)");
        }
        System.out.println("Choose bet sum (ex. 10, 50, 100):");

        while (true) {
            try {
                int userAmount = scanner.nextInt();
                scanner.nextLine();
                selectedBetSize = BetSize.fromAmount(userAmount);

                if (selectedBetSize == null) {
                    System.out.println("Uncorrected bet sum. Please, enter 10, 50 or 100.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Uncorrected input.Please  enter a number.");
                scanner.next();
                scanner.nextLine();
            }

            System.out.println("You choose bet: " + selectedBetSize.name() + " (" + selectedBetSize.getAmount() + " Orens)");
            return selectedBetSize;
        }
    }

    private static void chanceToReRoll(User user, CombinationResult cResult) {
        user.printCubeInfo();
        System.out.println("Combination is: " + cResult.getType() + "/" + cResult.getKeyValues());
        reRoll(user);
    }

    private static void reRoll(User user) {
        System.out.println("Do you want to roll the dice? (yes/no)");
        String userAnswer;
        while (true) {
            userAnswer = scanner.nextLine().trim().toLowerCase();
            if (userAnswer.equals("no") || userAnswer.equals("n")) {
                System.out.println(user.getName() + " want not re roll!");
                return;
            } else if (userAnswer.equals("yes") || (userAnswer.equals("y"))) {
                System.out.println(user.getName() + " want re roll");

                Set<RerollSide> selectedSides = new HashSet<>();
                System.out.println("Enter side number (1 - 5). To finish, enter 'end' ");
                while (true) {
                    String token = scanner.next();

                    if (token.equalsIgnoreCase("end")) {
                        System.out.println("Input finish");
                        break;
                    }
                    try {
                        int number = Integer.parseInt(token);
                        RerollSide side = RerollSide.fromNumber(number);

                        if (side != null) {
                            selectedSides.add(side);
                        } else {
                            System.out.println("Error: '" + number + "' - uncorrected CUBE number (must be 1 - 5). Skip.");

                        }
                    } catch (InputMismatchException e) {
                        String invalidToken = scanner.next();
                        System.out.println("Warning: '" + invalidToken + "' - uncorrected input (expect number). Skip.");

                    }
                }
                    CubeSide.rollCubes(user, selectedSides);
                    user.printCubeInfo();
                    return;
                } else {
                    System.out.println("Uncorrected input. Please enter 'yes | no'.");
                }
            }
        }



    public UserChecker playRound(User player1, User player2, BetSize betSize) {

        int bet = betSize.getAmount();
        if (player1.getBalance() < bet || player2.getBalance() < bet) {
            System.out.println("One of the players does not have enough money to place a bet.");
            return UserChecker.IS_BANKRUPT;
        }

        Map<Integer, Integer> finalSideArrUser1 = getSideFrequency(player1.getMyCubes());
        Map<Integer, Integer> finalSideArrUser2 = getSideFrequency(player2.getMyCubes());

        CombinationResult resultUser1 = getResult(finalSideArrUser1);
        CombinationResult resultUser2 = getResult(finalSideArrUser2);

        int cmp = resultUser1.compareTo(resultUser2);
        GameResult result;

        if (cmp > 0) {
            result = GameResult.WIN;
        } else if (cmp < 0) {
            result = GameResult.LOSE;
        } else {
            result = GameResult.DRAW;
        }

        switch (result) {
            case WIN -> {
                System.out.println(player1.getName() + " wined with combination: " + resultUser1);
                System.out.println(player2.getName() + " loose with combination:: " + resultUser2);
                player1.changeBalance(bet);
                player2.changeBalance(-bet);
                return UserChecker.USER1;
            }
            case LOSE -> {
                System.out.println(player2.getName() +" wined with combination: " + resultUser2);
                System.out.println(player1.getName() + " loose with combination: " + resultUser1);
                player1.changeBalance(-bet);
                player2.changeBalance(bet);
                return UserChecker.USER2;
            }
            case DRAW -> {
                System.out.println("Draw !" + resultUser1);
                return UserChecker.DRAW;
            }
        }
        printBalances(player1, player2);
        return UserChecker.ERROR;
    }

    public static Map<Integer, Integer> getSideFrequency(List<CubeSide> arrCubes) {
        Map<Integer, Integer> frequencyMap = new HashMap<>();

        for (CubeSide sideCubeValue: arrCubes) {
            int cubeSideIntValue = sideCubeValue.getValue();
            frequencyMap.put(cubeSideIntValue, frequencyMap.getOrDefault(cubeSideIntValue, 0) + 1);
        }
        return frequencyMap;
    }

    public static CombinationResult getResult(Map<Integer, Integer> frequencyMap) {

        if (isPokerCombination(frequencyMap).isPresent()) {
            int value = isPokerCombination(frequencyMap).get();
            return new CombinationResult(ComboType.POKER, List.of(value));
        }

        if (isCareCombination(frequencyMap).isPresent()) {
            int value = isPokerCombination(frequencyMap).get();
            return new CombinationResult(ComboType.CARE, List.of(value));
        }

        if (isFullHouse(frequencyMap).isPresent()) {
            Map<String, Integer> mapValues = isFullHouse(frequencyMap).get();
            return new CombinationResult(ComboType.FULL_HOUSE, List.of(mapValues.get("triple"), mapValues.get("pair")));
        }

        if (isBigStraight(frequencyMap).isPresent()) {
            return new CombinationResult(ComboType.BIG_STRAIGHT, List.of(0));
        }

        if (isSmallStraight(frequencyMap).isPresent()) {
            return new CombinationResult(ComboType.SMALL_STRAIGHT, List.of(0));
        }

        if (isThreeOfAKind(frequencyMap).isPresent()) {
            int value = isThreeOfAKind(frequencyMap).get();
            return new CombinationResult(ComboType.THREE_OF_A_KIND, List.of(value));
        }

        if (isTwoPair(frequencyMap).isPresent()) {
            Map<String, Integer> mapVal = isTwoPair(frequencyMap).get();
            return new CombinationResult(ComboType.TWO_PAIR, List.of(mapVal.get("higherPairValue"), mapVal.get("lowerPairValue"), mapVal.get("kicker")));
        }

        if (isPair(frequencyMap).isPresent()) {
            int value = isPair(frequencyMap).get();
            return new CombinationResult(ComboType.PAIR, List.of(value));
        }

        return new CombinationResult(ComboType.NOTHING, List.of(0));
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

    private static Optional<Map<String, Integer>> isFullHouse(Map<Integer, Integer> map) {
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
            return Optional.of(Map.of("triple", tripleValue, "pair", pairValue));
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
                return Optional.of(entry.getKey());
            }
        }

        return Optional.empty();
    }

    private static Optional<Map<String, Integer>> isTwoPair(Map<Integer, Integer> map) {
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
            return Optional.of(Map.of("higherPairValue", pairs.get(0), "lowerPairValue", pairs.get(1), "kicker", kicker));
        }
        return Optional.empty();
    }

    private static Optional<Integer> isPair(Map<Integer, Integer> map) {
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 2) {
                return Optional.of(entry.getKey());
            }
        }
        return Optional.empty();
    }

    private void printBalances(User p1, User p2) {
        System.out.println(p1.getName() + " balance: " + p1.getBalance());
        System.out.println(p2.getName() + " balance: " + p2.getBalance());
        System.out.println("-----------------------");
    }

}

