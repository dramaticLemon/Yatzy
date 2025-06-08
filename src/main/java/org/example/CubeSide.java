package org.example;

import java.util.Set;

public class CubeSide {
    private int value;
    private final int faceValue;
    final static RandomEngine engine = new RandomEngine();

    public CubeSide (int faceValue) {
        if (faceValue < 1 || faceValue > 5) throw new IllegalArgumentException("От 1 до 5 значение");
        this.faceValue = faceValue;
    }

    /**
     * get oder Cube
     * @return int value order Cube
     */
    public int getFaceValue() {
        return this.faceValue;
    }

    public int getValue () {
        return value;
    }

    /**
     *
     * @param user the specified user throwing who carries out the operation
     * @param selectedSides selected dice to re roll
     */
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

    /**
     *
     * @param firstUser user
     * @param secondUser user
     */
    public static void rollCubes(User firstUser, User secondUser) {
        CubeSide.rollingCubeHand(firstUser);
        CubeSide.rollingCubeHand(secondUser);
    }

    /**
     * assign a new value to a cube
     */
    private void roll () {
        this.value = engine.generateDiceRoll();
    }

    /**
     *
     * @param user the user who is conducting the operation
     */
    private static void rollingCubeHand (User user) {
        user.getMyCubes().forEach(CubeSide::roll);
    }

    @Override
    public String toString () {
        return RerollSide.fromNumber(faceValue) + "[" + this.value + "]";
    }
}
