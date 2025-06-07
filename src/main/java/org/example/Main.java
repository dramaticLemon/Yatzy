package org.example;

public class Main {
    public static void main (String[] args) {
        User user = new User();
        Cube.rollCube(user);
        user.printCubeInfo();
        System.out.println();
        int[] val = {5,6};
        Cube.rollCube(user, val);
        user.printCubeInfo();
    }
}