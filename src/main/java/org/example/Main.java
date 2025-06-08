package org.example;

import java.util.*;

public class Main {

    public static void main (String[] args) {
        Game object = new Game();
        User u1 = new User("u1");
        User u2 = new User("u2");
        object.start(u1, u2);


    }
}