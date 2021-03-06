package com.example.koekata.utils;

public class Constants {
    public static final String BASE_URL = "https://androidcntn-default-rtdb.asia-southeast1.firebasedatabase.app/";
    public static final String USER_ROOT = "users";
    public static final String CONNECTION_ROOT = ".info/connected";
    public static final String STATIC_STATUS = "START";
    public static final String STUDY_STATUS = "STUDYING";
    public static final String DONE_STATUS = "DONE";
    public static final String RELAX_STATUS = "RELAXING";

    public static final String STATIC_BUTTON = "Focus";
    public static final String STUDY_BUTTON = "Reset";
    public static final String DONE_BUTTON = "Relax";

    public static final String RELAX_BUTTON = "Skip";
    public static final Long DEFAULT_STUDY_TIME = 25L * 60 * 1000;

    public static final Long DEFAULT_SHORT_RELAX_TIME = 5L * 1000;
    public static final Long DEFAULT_LONG_RELAX_TIME = 15L * 1000;

    public static final Long MILLIS_PER_SEC = 1000L;
    public static final Long MILLIS_PER_MIN = 60 * MILLIS_PER_SEC;
    public static final Long MILLIS_PER_HOUR = 60 * MILLIS_PER_MIN;
    public static final Long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;

    public static final String STUDY_TIME = "study";
    public static final String SHORT_RELAX_TIME = "short_relax";
    public static final String LONG_RELAX_TIME = "long_relax";
}
