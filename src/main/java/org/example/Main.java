package org.example;

import java.util.Scanner;

public class Main {
    final static Scanner scanner = new Scanner(System.in);

    public static void main (String[] args) {
        Game object = new Game();
        User u1 = new User("Test user 1");
        User u2 = new User("Test user 2");

        int winsPlayer1 = 0;
        int winsPlayer2 = 0;
        int roundCounting = 0;

        while (winsPlayer1 < 2 && winsPlayer2 < 2 && roundCounting < 3) {
            System.out.println("ROUND " + (roundCounting));
            UserChecker result = object.start(u1, u2);

            switch (result) {
                case USER1 -> winsPlayer1++;
                case USER2 -> winsPlayer2++;
                case IS_BANKRUPT -> {
                    System.out.println("Run out money");
                    break;
                }
                case ERROR -> System.err.println("Error");
                default -> System.err.println("Unknown result: " + result);
            }
            roundCounting++;

            System.out.println();
            System.out.println("Current scores");
            System.out.println("User1: " + winsPlayer1);
            System.out.println("User2: " + winsPlayer2);
            System.out.println();
        }

        System.out.println("=== TOTAL ===");
        if (winsPlayer1 > winsPlayer2) {
            System.out.println(u1.getName() + " won the match with a score of " + winsPlayer1 + ":" + winsPlayer2);
        } else if (winsPlayer2 > winsPlayer1) {
            System.out.println(u2.getName() + " won the match with a score of " + winsPlayer2 + ":" + winsPlayer1);
        } else {
            System.out.println("The match ended in a draw:: " + winsPlayer1 + ":" + winsPlayer2);
        }


    }
}