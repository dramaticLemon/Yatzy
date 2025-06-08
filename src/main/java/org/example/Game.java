package org.example;

import java.util.*;

public class Game {
    RandomEngine engine = new RandomEngine();
    Scanner scanner = new Scanner(System.in);

    public UserChecker start(User firstUser, User secondUser) {
        // начало раунда игроки бросают свои кубики
        CubeSide.rollCubes(firstUser, secondUser);

        firstUser.printCubeInfo();

        Map<Integer, Integer> arr = getSideFrequency(firstUser.getMyCubes());
        CombinationResult u1 = getResult(arr);
        System.out.println("Combination is: " + u1.getType() + "/" + u1.getKeyValues());

        secondUser.printCubeInfo();

        Map<Integer, Integer> arr2 = getSideFrequency(secondUser.getMyCubes());
        CombinationResult u2 = getResult(arr2);
        System.out.println("Combination is: " + u2.getType() + "/" + u2.getKeyValues());


        // сделать ставку для первого игрока, автоматом такая ставка и для второго игрока
        System.out.println();
        BetSize userBet = selectBet(scanner);

        // возможность перебросить кубики один раз
        firstUser.printCubeInfo();
        System.out.println("Combination is: " + u1.getType() + "/" + u1.getKeyValues());
        reRoll(firstUser, scanner);

        secondUser.printCubeInfo();
        System.out.println("Combination is: " + u2.getType() + "/" + u2.getKeyValues());
        reRoll(secondUser, scanner);

        return playRound(firstUser, secondUser, userBet);

    }

    private static void reRoll(User user, Scanner scanner) {
        System.out.println("Хотите перекинуть кубики ?(да/нет)");
        String userAnswer;
        while (true) {
            userAnswer = scanner.nextLine().trim().toLowerCase();
            if (userAnswer.equals("нет") || userAnswer.equals("n")) {
                System.out.println(user.getName() + " решил не перебрасывать кубики");
                return;
            } else if (userAnswer.equals("да") || (userAnswer.equals("d"))) {
                System.out.println(user.getName() + " решил перебросить кубики");

                Set<RerollSide> selectedSides = new HashSet<>();
                System.out.println("Вводи номера сторон (от 1 до 5). Чтобы закончить — введи 'end'");
                while (true) {
                    String token = scanner.next();

                    if (token.equalsIgnoreCase("end")) {
                        System.out.println("Завершаем ввод");
                        break;
                    }
                    try {
                        int number = Integer.parseInt(token);
                        RerollSide side = RerollSide.fromNumber(number);

                        if (side != null) {
                            selectedSides.add(side);
                        } else {
                            System.out.println("Предупреждение: '" + number + "' - некорректный номер кубика (должен быть от 1 до 5). Пропускаем.");

                        }
                    } catch (InputMismatchException e) {
                        String invalidToken = scanner.next();
                        System.out.println("Предупреждение: '" + invalidToken + "' - некорректный ввод (ожидается число). Пропускаем.");

                    }
                }
                    // логика перебрасывания
                    CubeSide.rollCubes(user, selectedSides);
                    user.printCubeInfo();
                    return;
                } else {
                    System.out.println("Некоректный ввод. Пожалуйста введите 'да' или 'нет'.");
                }
            }
        }

    /**
     * Позволяет игроку выбрать размер ставки.
     * Запрашивает ввод пользователя и проверяет его на соответствие доступным размерам ставок.
     *
     * @return Выбранный игроком объект BetSize
     */
    private static BetSize selectBet(Scanner scanner) {
        BetSize selectedBetSize = null;
        System.out.println("Выберите размер ставки: ");
        for (BetSize size : BetSize.values()) {
            System.out.println("- " + size.name() + " (" + size.getAmount() + " оренов)");
        }
        System.out.println("Введите сумму ставки (например, 10, 50, 100):");

        while (true) {
            try {
                int userAmount = scanner.nextInt();
                scanner.nextLine();
                selectedBetSize = BetSize.fromAmount(userAmount);

                if (selectedBetSize == null) {
                    System.out.println("Некорректная сумма ставки. Пожалуйста, введите 10, 50 или 100.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Некорректный ввод. Пожалуйста, введите число.");
                scanner.next();
                scanner.nextLine();
            }

            System.out.println("Вы выбрали ставку: " + selectedBetSize.name() + " (" + selectedBetSize.getAmount() + " оренов)");
            return selectedBetSize;
        }
    }

    public UserChecker playRound(User player1, User player2, BetSize betSize) {

        int bet = betSize.getAmount();
        if (player1.getBalance() < bet || player2.getBalance() < bet) {
            System.out.println("У одного из игроков недостаточно денег для ставки.");
            return UserChecker.IS_BANKRUPT;
        }

        // получить частотность встречи сторон кубика
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
                System.out.println(player1.getName() + " выиграл с комбинацией: " + resultUser1);
                System.out.println(player2.getName() + " проиграл с комбинацией: " + resultUser2);
                player1.changeBalance(bet);
                player2.changeBalance(-bet);
                return UserChecker.USER1;
            }
            case LOSE -> {
                System.out.println(player2.getName() + " выиграл с комбинацией: " + resultUser2);
                System.out.println(player1.getName() + " проиграл с комбинацией: " + resultUser1);
                player1.changeBalance(-bet);
                player2.changeBalance(bet);
                return UserChecker.USER2;
            }
            case DRAW -> {
                System.out.println("Ничья! У обоих: " + resultUser1);
                return UserChecker.DRAW;
            }
        }
        printBalances(player1, player2);
        return UserChecker.ERROR;
    }

    private void printBalances(User p1, User p2) {
        System.out.println(p1.getName() + " баланс: " + p1.getBalance());
        System.out.println(p2.getName() + " баланс: " + p2.getBalance());
        System.out.println("-----------------------");
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
            FullHouseInfo obj = isFullHouse(frequencyMap).get();
            return new CombinationResult(ComboType.FULL_HOUSE, List.of(obj.getTripleValue(), obj.getPairValue()));
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
            TwoPairInfo obj = isTwoPair(frequencyMap).get();
            return new CombinationResult(ComboType.TWO_PAIR, List.of(obj.getHigherPairValue(), obj.getLowerPairValue(), obj.getKickerValue()));
        }

        if (isPair(frequencyMap).isPresent()) {
            int value = isPair(frequencyMap).get();
            return new CombinationResult(ComboType.PAIR, List.of(value));
        }

        return new CombinationResult(ComboType.NOTHING, List.of(0));
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
                return Optional.of(entry.getKey());
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
                return Optional.of(entry.getKey());
            }
        }
        return Optional.empty();
    }

 }

