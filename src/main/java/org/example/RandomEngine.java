package org.example;

import java.util.Random;

public class RandomEngine {
    private static final Random objRandom  = new Random();

    // генерация кидка кубика
    public int generateDiceRoll() {
        return objRandom.nextInt(1, 7);
    }
}
