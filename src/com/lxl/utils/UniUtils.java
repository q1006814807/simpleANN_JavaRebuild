package com.lxl.utils;

import java.util.List;
import java.util.Random;

public class UniUtils {

    private final static Random random = new Random(System.currentTimeMillis());

    private UniUtils() {}

    public static <T> void resizeList(List<T> list, int len, T val) {
        if (len > list.size()) {
            int times = len - list.size();
            while (times-- > 0) {
                list.add(val);
            }
        } else {
            int times = list.size() - len;

            while (times-- > 0) {
                list.remove(list.size() - 1);
            }
        }
    }

    public static <T> void resizeList(List<T> list, int len) {
        if (len > list.size()) {
            int times = len - list.size();
            while (times-- > 0) {
                list.add(null);
            }
        } else {
            int times = list.size() - len;

            while (times-- > 0) {
                list.remove(list.size() - 1);
            }
        }
    }

    public static float randomFloat(float lower, float upper) {
        return random.nextFloat() * (upper - lower) + lower;
    }

    public static float getSquareDist(Matrix a, Matrix y) {
        double cost = 0;
        for (int r = 1; r < a.getRow(); ++r) {
            cost += Math.pow(a.get(r, 0) - y.get(r, 0), 2);
        }

        return (float)cost;
    }

    public static float getCrossEntropy(Matrix a, Matrix y) {
        double cost = 0;
        for (int r = 1; r < a.getRow(); ++r) {
            cost -= y.get(r, 0) * Math.log(a.get(r, 0));
        }

        return (float)cost;
    }

}
