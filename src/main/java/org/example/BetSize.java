package org.example;

public enum BetSize {
    SMALL(10),
    MEDIUM(50),
    LARGE(100);

    private final int amount;

    BetSize(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

}
