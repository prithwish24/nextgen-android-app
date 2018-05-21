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

    public enum Services {
        USER_LOGIN ("profile", "8001", "/login"),
        UPCOMING_RESERVATION ("reservation", "8002", "/trips/upcoming"),
        ALL_RESERVATION ("reservation", "8002", "/rentals"),
        SAVE_DUMMY_ZIPCODE ("reservation", "8002", "/zipcode/{sessionId}");

        private final String app;
        private final String port;
        private final String name;
        Services(String app, String port, String name) {
            this.app  = app;
            this.port = port;
            this.name = name;
        }
        public String getName() {
            return name;
        }
        public String getPort() {
            return port;
        }
        public String getApp() {
            return app;
        }
    }

}
