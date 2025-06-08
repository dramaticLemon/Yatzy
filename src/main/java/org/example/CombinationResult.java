package org.example;

import java.util.List;

public class CombinationResult implements Comparable<CombinationResult> {
    private final ComboType type;
    private final List<Integer> keyValues;

    public CombinationResult (ComboType type, List<Integer> keyValues) {
        this.type = type;
        this.keyValues = keyValues;
    }

    public ComboType getType () {
        return this.type;
    }

    public List<Integer> getKeyValues () {
        return this.keyValues;
    }

    @Override
    public int compareTo(CombinationResult other) {
        int rankCompare = Integer.compare(this.type.getRank(), other.type.getRank());
        if (rankCompare != 0) return rankCompare;

        // Если типы одинаковы, сравниваем значения
        for (int i = 0; i < Math.min(this.keyValues.size(), other.keyValues.size()); i++) {
            int cmp = Integer.compare(this.keyValues.get(i), other.keyValues.get(i));
            if (cmp != 0) return cmp;
        }

        return 0; // ничья
    }

    @Override
    public String toString() {
        return type + " " + keyValues;
    }

}
