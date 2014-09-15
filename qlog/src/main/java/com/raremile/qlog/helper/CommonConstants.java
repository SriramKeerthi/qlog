package com.raremile.qlog.helper;

public class CommonConstants
{
    public static final String CONFIG_FILE = "qlog.properties";
    public static final String LOG_FILE_NAME = "file";
    public static final String WAIT_TIMEOUT = "wait";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String STARTS_WITH = "startswith";
    public static final String ENDS_WITH = "endswith";
    public static final String REGEX = "regex";
    public static final String LINES_BEFORE = "linesbefore";
    public static final String LINES_AFTER = "linesafter";
    public static final String SMTP = "smtp";
    public static final String EXCEPTION_BODY = "<html><body><h3>We found a problem!</h3><p>%s</p>"
        + "<h4>Showing only the last few lines of the Exception. There may be Exceptions matched immediately before this.</h4</body></html>";
}
