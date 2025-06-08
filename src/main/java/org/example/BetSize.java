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

    public static BetSize fromAmount(int amount) {
        for (BetSize size : BetSize.values()) {
            if (size.getAmount() == amount) {
                return size;
            }
        }
        return null;
    }

}
