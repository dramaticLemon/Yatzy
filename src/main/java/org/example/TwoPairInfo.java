package org.example;

public class TwoPairInfo {
    private final int higherPairValue;
    private final int lowerPairValue;
    private final int kickerValue;

    public TwoPairInfo (int pair1Value, int pair2Value, int kickerValue) {

        if (pair1Value > pair2Value) {
            this.higherPairValue = pair1Value;
            this.lowerPairValue = pair2Value;
        } else {
            this.higherPairValue = pair2Value;
            this.lowerPairValue = pair1Value;
        }
        this.kickerValue = kickerValue;
    }

    public int getHigherPairValue() {
        return higherPairValue;
    }

    public int getLowerPairValue() {
        return lowerPairValue;
    }

    public int getKickerValue() {
        return kickerValue;
    }

    @Override
    public String toString() {
        return "TwoPairInfo{" +
                "higherPairValue=" + higherPairValue +
                ", lowerPairValue=" + lowerPairValue +
                ", kickerValue=" + kickerValue +
                '}';
    }
}
