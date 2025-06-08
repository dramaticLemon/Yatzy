package org.example;

public enum ComboType {
    POKER(8),
    CARE(7),
    FULL_HOUSE(6),
    BIG_STRAIGHT(5),
    SMALL_STRAIGHT(4),
    THREE_OF_A_KIND(3),
    TWO_PAIR(2),
    PAIR(1),
    NOTHING(0);

    private final int rank;

    ComboType(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }
}
