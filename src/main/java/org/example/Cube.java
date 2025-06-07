package org.example;

import java.util.stream.IntStream;

public class Cube {
    private int value;
    private final int faceValue;
    final static RandomEngine engine = new RandomEngine();

    public Cube(int faceValue) {
        if (faceValue < 1 || faceValue > 6) throw new IllegalArgumentException("От 1 до 6 значение");
        this.faceValue = faceValue;
    }

    public int getFaceValue() {
        return this.faceValue;
    }

    // получить значение кубика
    public int getValue () {
        return value;
    }


    // один юзер выборочно кидает кубики
    public static void rollCube(User user1, int[] val) {
        for (Cube cube : user1.getMyCubes()) {
            if (IntStream.of(val).anyMatch(v -> v == cube.getFaceValue())) {
                cube.roll();
            }
        }
    }

    // один юзер кидает все кубики
    public static void rollCube(User user) {
        Cube.rollMultiple(user);
    }

    // два юзера кидают все кубики
    public static void rollCube(User user1, User user2) {
        Cube.rollMultiple(user1);
        Cube.rollMultiple(user2);
    }

    // поменять значение у конкретного кубика
    private void roll () {
        this.value = engine.generateDiceRoll();
    }

    // кинуть все кубики у конкретного пользователя
    private static void rollMultiple(User user) {
        user.getMyCubes().forEach(Cube::roll);
    }

    @Override
    public String toString () {
        return "Cube{" +
                "value=" + value +
                '}';
    }
}
