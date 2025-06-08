package org.example;

public enum RerollSide {
    ONE, TWO, THREE, FOUR, FIVE;

    public static RerollSide fromNumber(int number) {
        if (number >= 1 && number <= 5) {
            return RerollSide.values()[number - 1];
        }
        return null;
    }

    public int getNumber() {
        return this.ordinal() + 1;
    }
}
