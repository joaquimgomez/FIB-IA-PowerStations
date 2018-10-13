package src;

public class IAUtils {

    // if val < min -> min
    // if val > max -> max
    // return val
    public static <T extends Comparable<T>> T clamp(T val, T min, T max) {
        if (val.compareTo(min) < 0) return min;
        else if (val.compareTo(max) > 0) return max;
        else return val;
    }


    // Random
    public static double random() {
        return (Math.random());
    }

    public static int random(int min, int max) {
        return (int)(Math.random() * (max - min) + min);
    }

    public static double random(double min, double max) {
        return Math.random() * (max - min) + min;
    }

}
