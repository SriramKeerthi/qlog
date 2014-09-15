package com.raremile.qlog.helper;

import java.util.Map;



public class GlobalConfigurations
{

    private static int linesBefore;
    private static int linesAfter;
    private static String email;
    private static String password;
    private static String smtp;


    public static void initialize( Map<String, Object> configurations )
    {
        linesBefore = 10;
        if ( configurations.containsKey( CommonConstants.LINES_BEFORE ) ) {
            linesBefore = Integer.parseInt( (String) configurations.get( CommonConstants.LINES_BEFORE ) );
        }
        linesAfter = 10;
        if ( configurations.containsKey( CommonConstants.LINES_AFTER ) ) {
            linesAfter = Integer.parseInt( (String) configurations.get( CommonConstants.LINES_AFTER ) );
        }
        email = (String) configurations.get( CommonConstants.EMAIL );
        password = (String) configurations.get( CommonConstants.PASSWORD );
        smtp = (String) configurations.get( CommonConstants.SMTP );
    }


    public static int linesBefore()
    {
        return linesBefore;
    }


    public static int linesAfter()
    {
        return linesAfter;
    }


    public static String email()
    {
        return email;
    }


    public static String password()
    {
        return password;
    }


    public static String smtp()
    {
        return smtp;
    }
}
