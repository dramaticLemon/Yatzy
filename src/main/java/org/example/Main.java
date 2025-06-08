package org.example;

public class Main {

    public static void main (String[] args) {
        Game object = new Game();
        User u1 = new User("Test user 1");
        User u2 = new User("Test user 2");

        int winsPlayer1 = 0;
        int winsPlayer2 = 0;
        int roundCounting = 0;

        while (winsPlayer1 < 2 && winsPlayer2 < 2 && roundCounting < 3) {
            System.out.println("Раунд " + (roundCounting));
            UserChecker result = object.start(u1, u2);

            if (result.equals(UserChecker.USER1)) {
                winsPlayer1++;
            } else if (result.equals(UserChecker.USER2)) {
                winsPlayer2++;
            } else if (result.equals(UserChecker.IS_BANKRUPT)) {
                System.out.println("Закончились деньги");
                break;
            } else if (result.equals(UserChecker.ERROR)) {
                System.err.println("Ошибка"); // log
            }
            roundCounting++;

            System.out.println();
            System.out.println("Current scores");
            System.out.println("User1: " + winsPlayer1);
            System.out.println("User2: " + winsPlayer2);
            System.out.println();
        }

        System.out.println("=== ИТОГ ===");
        if (winsPlayer1 > winsPlayer2) {
            System.out.println(u1.getName() + " выиграл матч со счётом " + winsPlayer1 + ":" + winsPlayer2);
        } else if (winsPlayer2 > winsPlayer1) {
            System.out.println(u2.getName() + " выиграл матч со счётом " + winsPlayer2 + ":" + winsPlayer1);
        } else {
            System.out.println("Матч закончился ничьёй: " + winsPlayer1 + ":" + winsPlayer2);
        }


    }
}