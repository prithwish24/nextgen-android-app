package com.nextgen.carrental.app.constants;

/**
 * Global constants for the App
 * @author Abhishek
 * @since 5/13/2018.
 */

public class GlobalConstants {

    public static final String AGENT_NAME = "Agent Emily";

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

    public class AppSettings {
        public static final String switchZipCodeMocking = "enable_zip_code_mocking";
        public static final String MockZipCode = "mock_zip_code";
        public static final String Environment = "app_environment";
        public static final String ServiceUrl = "app_service_url";
        public static final String DialogFlowClientToken = "dialog_flow_client_token";
    }

}
