package org.example;

import java.util.Objects;

public class FullHouseInfo implements Comparable<FullHouseInfo>{
    private final int tripleValue;
    private final int pairValue;

    public FullHouseInfo(int tripleValue, int pairValue) {
        this.tripleValue = tripleValue;
        this.pairValue = pairValue;
    }

    public int getTripleValue () {
        return tripleValue;
    }

    public int getPairValue () {
        return pairValue;
    }

    @Override
    public int compareTo(FullHouseInfo other) {

        int tripleComparison = Integer.compare(this.tripleValue, other.tripleValue);
        if (tripleComparison != 0) {
            return tripleComparison;
        }

        return Integer.compare(this.pairValue, other.pairValue);
    }

    @Override
    public String toString () {
        return "FullHouseInfo{" +
                "tripleValue=" + tripleValue +
                ", pairValue=" + pairValue +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FullHouseInfo that = (FullHouseInfo) o;
        return tripleValue == that.tripleValue && pairValue == that.pairValue;
    }


    @Override
    public int hashCode() {
        return Objects.hash(tripleValue, pairValue);
    }

}
