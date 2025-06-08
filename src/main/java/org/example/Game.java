package org.example;

import java.util.*;

public class Game {
    RandomEngine engine = new RandomEngine();
    Scanner scanner = new Scanner(System.in);

    public void start(User firstUser, User secondUser) {
        // начало раунда игрок1 и игрок2 бросают свои кубики
        CubeSide.rollCubes(firstUser, secondUser);

        System.out.println("First user");
        firstUser.printCubeInfo();

        Map<Integer, Integer> arr = getSideFrequency(firstUser.getMyCubes());
        getResult(arr);

        System.out.println("Second user");
        secondUser.printCubeInfo();

        Map<Integer, Integer> arr2 = getSideFrequency(secondUser.getMyCubes());
        getResult(arr2);

        // сделать ставку для первого игрока, автоматом такая ставка и для второго игрока
        BetSize userBet = selectBet(scanner);

        // возможность перебросить кубики один раз
        firstUser.printCubeInfo();
        reRoll(firstUser, scanner);


        playRound(firstUser, secondUser, userBet);

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

    public void playRound(User player1, User player2, BetSize betSize) {

        int bet = betSize.getAmount();

        if (player1.getBalance() < bet || player2.getBalance() < bet) {
            System.out.println("У одного из игроков недостаточно денег для ставки.");
            return;
        }


        GameResult result = getRandomResult(); // закинуть исход поединка

        switch (result) {
            case WIN -> {
                System.out.println(player1.getName() + " выиграл!");
                player1.changeBalance(bet);
                player2.changeBalance(-bet);
            }
            case LOSE -> {
                System.out.println(player2.getName() + " выиграл!");
                player1.changeBalance(-bet);
                player2.changeBalance(bet);
            }
            case DRAW -> {
                System.out.println("Ничья!");
                // балансы не меняются
            }
        }

        printBalances(player1, player2);
    }

    private GameResult getRandomResult() {
        int rand = (int) (Math.random() * 3);
        return switch (rand) {
            case 0 -> GameResult.WIN;
            case 1 -> GameResult.LOSE;
            default -> GameResult.DRAW;
        };
    }


    private void printBalances(User p1, User p2) {
        System.out.println(p1.getName() + " баланс: " + p1.getBalance());
        System.out.println(p2.getName() + " баланс: " + p2.getBalance());
        System.out.println("-----------------------");
    }

    public static void getResult(Map<Integer, Integer> frequencyMap) {

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

