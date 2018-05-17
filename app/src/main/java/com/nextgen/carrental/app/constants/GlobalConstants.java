package com.nextgen.carrental.app.constants;

/**
 * Global constants for the App
 * @author Abhishek
 * @since 5/13/2018.
 */

public class GlobalConstants {

    public static final String SHARED_PREF_NAME = "CarRentalPref";

    public static final String KEY_NAME = "name";
    public static final String KEY_USERID = "userId";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_SESSIONID = "sessionId";


    // FINAL URL - Micro-Service Gateway
    public static final String URL_GATEWAY = "http://18.188.102.146:8080/";

    // TEMP URLs - for testing
    public static final String URL_USER_LOGIN = "http://18.188.102.146:8001/";
    public static final String URL_LIST_TRIPS = "http://18.188.102.146:8002/";
    public static final String URL_SHOW_RESERVATIONS = "http://18.188.102.146:8002/";
    public static final String URL_SAVE_DUMMY_ZIPCODE = "http://18.188.102.146:8002/";


}
