package org.example;

import java.util.ArrayList;
import java.util.List;

public class User {
    private final String name;
    private final List<CubeSide> myCubes = new ArrayList<>();
    private int balance = 1000;
    private int saveValue;

    /**
     * Create user object, and 5 Cube object
     * @param name User name
     */
    public User(String name) {
        for (int i = 1; i <= 5; i++) {
            myCubes.add(new CubeSide(i));
        }
        this.name = name;
    }

    /**
     *
     * @return list of User Cubes
     */
    public List<CubeSide> getMyCubes() {
        return this.myCubes;
    }


    public void printCubeInfo() {
        System.out.println();
        System.out.println("Current hand " + this.name);
        myCubes.forEach(System.out::println);
        System.out.println("-------");
    }

    public void getMoneyValues () {
        System.out.println(this.balance);
    }

    public void changeBalance (int amount) {
        this.balance += amount;
    }

    public int getBalance() {
        return balance;
    }

    public String getName () {
        return name;
    }
}
