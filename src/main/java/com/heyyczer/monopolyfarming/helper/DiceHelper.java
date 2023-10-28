package com.heyyczer.monopolyfarming.helper;

import java.util.Random;

public class DiceHelper {

    private static final Random random = new Random();

    private static final int min = 1;
    private static final int max = 6;

    public static int getRandomNumber() {
        return random.nextInt(max - min) + min;
    }

}
