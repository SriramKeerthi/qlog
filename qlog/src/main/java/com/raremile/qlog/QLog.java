package com.raremile.qlog;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raremile.qlog.engine.Handler;
import com.raremile.qlog.helper.CommonConstants;
import com.raremile.qlog.helper.GlobalConfigurations;


public class QLog
{
    private static Map<String, Map<String, Object>> configurations;


    public static void main( String[] args ) throws IOException
    {
        GlobalConfigurations.initialize( loadGlobals() );
        configurations = loadConfigs();
        for ( String handlerName : configurations.keySet() ) {
            new Handler( handlerName, configurations.get( handlerName ) ).start();
        }
    }


    @SuppressWarnings ( "unchecked")
    private static Map<String, Object> loadGlobals() throws IOException
    {
        Map<String, Object> configurations = new HashMap<String, Object>();
        BufferedReader br = new BufferedReader( new InputStreamReader( new FileInputStream( CommonConstants.CONFIG_FILE ) ) );
        String line = null;
        while ( ( line = br.readLine().trim() ) != null ) {
            if ( !line.startsWith( "global." ) ) {
                continue;
            }
            int split = line.indexOf( '=' );
            if ( split < 1 ) {
                continue;
            }
            String key = line.substring( 0, split ).trim();
            String value = line.substring( split + 1 ).trim();
            if ( configurations.containsKey( key ) ) {
                if ( configurations.get( key ) instanceof String ) {
                    List<String> values = new ArrayList<String>();
                    values.add( (String) configurations.get( key ) );
                    configurations.put( key, values );
                }
                ( (List<String>) configurations.get( key ) ).add( value );
            } else {
                configurations.put( key, value );
            }
        }
        br.close();
        return configurations;
    }


    @SuppressWarnings ( "unchecked")
    private static Map<String, Map<String, Object>> loadConfigs() throws IOException
    {
        Map<String, Map<String, Object>> configurations = new HashMap<String, Map<String, Object>>();
        BufferedReader br = new BufferedReader( new InputStreamReader( new FileInputStream( CommonConstants.CONFIG_FILE ) ) );
        String line = null;
        while ( ( line = br.readLine().trim() ) != null ) {
            if ( line.startsWith( "#" ) || line.startsWith( "global." ) ) {
                continue;
            }
            int split = line.indexOf( '.' );
            if ( split < 1 ) {
                continue;
            }
            String handlerName = line.substring( 0, split ).trim();
            if ( !configurations.containsKey( handlerName ) ) {
                configurations.put( handlerName, new HashMap<String, Object>() );
            }
            Map<String, Object> handlerConfigurations = configurations.get( handlerName );
            line = line.substring( split + 1 ).trim();
            split = line.indexOf( '=' );
            String key = line.substring( 0, split ).trim();
            String value = line.substring( split + 1 ).trim();
            if ( handlerConfigurations.containsKey( key ) ) {
                if ( handlerConfigurations.get( key ) instanceof String ) {
                    List<String> values = new ArrayList<String>();
                    values.add( (String) handlerConfigurations.get( key ) );
                    handlerConfigurations.put( key, values );
                }
                ( (List<String>) handlerConfigurations.get( key ) ).add( value );
            } else {
                handlerConfigurations.put( key, value );
            }
        }
        br.close();
        return configurations;
    }
}
