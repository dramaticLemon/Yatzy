package org.example;

import java.util.Set;
import java.util.stream.IntStream;

public class CubeSide {
    private int value;
    private final int faceValue;
    final static RandomEngine engine = new RandomEngine();

    public CubeSide (int faceValue) {
        if (faceValue < 1 || faceValue > 5) throw new IllegalArgumentException("От 1 до 5 значение");
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
    public static void rollCubes(User user, Set<RerollSide> selectedSides) {
        for (CubeSide cube : user.getMyCubes()) {
            int faveValue = cube.getFaceValue();
            boolean shouldRoll = selectedSides.stream()
                    .anyMatch(side -> side.getNumber() == faveValue);

            if (shouldRoll) {
                cube.roll();
            }
        }
    }

    // один юзер кидает все кубики
    public static void rollCubes(User user) {
        CubeSide.rollMultiple(user);
    }

    // два юзера кидают все кубики
    public static void rollCubes(User first, User second) {
        CubeSide.rollMultiple(first);
        CubeSide.rollMultiple(second);
    }

    // поменять значение у конкретного кубика
    private void roll () {
        this.value = engine.generateDiceRoll();
    }

    // кинуть все кубики у конкретного пользователя
    private static void rollMultiple(User user) {
        user.getMyCubes().forEach(CubeSide::roll);
    }

    @Override
    public String toString () {
        return "CubeSide{" +
                "value=" + value +
                '}';
    }
}
