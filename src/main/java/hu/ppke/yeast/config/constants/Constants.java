package hu.ppke.yeast.config.constants;

import hu.ppke.yeast.enums.WeightMethod;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";
    public static final String DEFAULT_LANGUAGE = "en";

    public static final WeightMethod DEFAULT_WEIGHT_METHOD = WeightMethod.TF_IDF;

    private Constants() {
    }
}
