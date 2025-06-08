package org.example;

import java.util.Objects;

public class TwoPairInfo implements Comparable<TwoPairInfo>{
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
    public int compareTo(TwoPairInfo other) {
        if (this.higherPairValue != other.higherPairValue) {
            return Integer.compare(this.higherPairValue, other.higherPairValue);
        }

        if (this.lowerPairValue != other.higherPairValue) {
            return Integer.compare(this.lowerPairValue, other.lowerPairValue);
        }

        return Integer.compare(this.kickerValue, other.kickerValue);
    }

    @Override
    public boolean equals (Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        TwoPairInfo that = (TwoPairInfo) obj;
        return higherPairValue == that.higherPairValue &&
                lowerPairValue == that.lowerPairValue &&
                kickerValue == that.kickerValue;

    }

    @Override
    public int hashCode () {
        return Objects.hash(higherPairValue, lowerPairValue, kickerValue);
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
