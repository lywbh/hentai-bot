package com.lyw.utils;

import java.util.Random;

public class RandomUtils {

    private static Random random = new Random();

    private static Random getRandom() {
        return random;
    }

    /**
     * 获得一个[0,max)之间的整数。
     */
    public static int getRandomInt(int max) {
        return getRandomInt(0, max);
    }

    /**
     * 获得一个[min,max)之间的整数。
     */
    public static int getRandomInt(int min, int max) {
        return Math.abs(getRandom().nextInt()) % max + min;
    }

}
