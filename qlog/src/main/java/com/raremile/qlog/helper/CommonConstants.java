package com.raremile.qlog.helper;

public class CommonConstants
{

    /**
     * 
     * Application Properties
     *
     */
    public static class APPLICATION
    {
        public static final String HANDLERS = "application.handlers";
        public static final String LINES_BEFORE = "application.lines.before";
        public static final String LINES_AFTER = "application.lines.after";
        public static final String CONFIG_FILE = "qlog.conf";
        public static final String EMAIL = "application.email.address";
        public static final String PASSWORD = "application.email.password";
    }


    /**
     * Global Handler Properties    
     */
    public static class GLOBAL
    {
        public static final String EMAILS = "global.emails";
    }


    /**
     * 
     * Handler Properties
     *
     */
    public static class HANDLER
    {
        public static final String LOG_FILE = "file";
        public static final String WAIT_TIMEOUT = "wait";
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
        public static final String STARTS_WITH = "startsWith";
        public static final String ENDS_WITH = "endsWith";
        public static final String REGEX = "regex";

    }

    public static final String EXCEPTION_BODY = "<html><body><h3>We found a problem!</h3><p>%s</p>"
        + "<h4>Showing only the last few lines of the Exception. There may be Exceptions matched immediately before this.</h4</body></html>";
}
