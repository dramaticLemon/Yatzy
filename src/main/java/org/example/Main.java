package org.example;

public class Main {
    public static void main (String[] args) {
        User user = new User();
        CubeSide.rollCubes(user);
        user.printCubeInfo();
        System.out.println();
        int[] val = {5,6};
        CubeSide.rollCubes(user, val);
        user.printCubeInfo();
    }
}