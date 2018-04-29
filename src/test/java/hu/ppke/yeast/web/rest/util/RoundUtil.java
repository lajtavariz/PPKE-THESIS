package hu.ppke.yeast.web.rest.util;

public class RoundUtil {

    public static double roundDouble(Double value) {
        return (double) Math.round(value * 100000d) / 100000d;
    }

}
