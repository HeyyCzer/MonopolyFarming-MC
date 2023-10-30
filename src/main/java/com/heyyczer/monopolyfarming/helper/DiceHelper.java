package com.heyyczer.monopolyfarming.helper;

import java.util.Random;

public class DiceHelper {

    private static final int MIN = 1;
    private static final int MAX = 6;

    public static int getRandomNumber(long seed) {
        return new Random(seed).nextInt(MAX - MIN) + MIN;
    }

}
