package org.example;

public class Game {
    RandomEngine engine = new RandomEngine();

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

 }

