/**
 * 
 */
package com.raremile.qlog.helper;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;


/**
 * @author Samarth Bhargav
 *
 */
public class Configurations
{

    private static final Logger LOG = LoggerFactory.getLogger( Configurations.class );

    static {
        reload();
    }


    private static Config config;


    /**
     * Return the value of <b>propertyName</b>. null is returned if property does not exist.
     * Type <i><b>must</b></i> be a string, or an exception is thrown. 
     * @param propertyName
     * @return 
     */
    public static String getString( String propertyName )
    {
        if ( config.hasPath( propertyName ) ) {
            return config.getString( propertyName );
        }
        return null;
    }


    /**
     * Return the value of <b>propertyName</b>. null is returned if property does not exist.
     * Type <i><b>must</b></i> be a List of Strings, or an exception is thrown. 
     * @param propertyName
     * @return
     */
    public static List<String> getStringList( String propertyName )
    {
        if ( config.hasPath( propertyName ) ) {
            return config.getStringList( propertyName );
        }
        return null;
    }


    /**
     * Return the value of <b>propertyName</b>. null is returned if property does not exist.
     * Type can be either a list of strings or a string 
     * @param propertyName
     * @return
     */
    public static List<String> getStringOrList( String propertyName )
    {
        if ( config.hasPath( propertyName ) ) {
            Object object = config.getAnyRef( propertyName );
            if ( object instanceof List ) {
                return getStringList( propertyName );
            } else {
                return Arrays.asList( (String) object );
            }
        }
        return null;
    }


    /**
     * Return the value of <b>propertyName</b>. null is returned if property does not exist.
     * Type <i><b>must</b></i> be an int, or an exception is thrown. 
     * @param propertyName
     * @return
     */
    public static Integer getInt( String propertyName )
    {
        if ( config.hasPath( propertyName ) ) {
            return config.getInt( propertyName );
        }
        return null;
    }


    /**
     * Return the value of <b>propertyName</b>. null is returned if property does not exist.
     * @param propertyName
     * @return
     */
    public static Object getObject( String propertyName )
    {
        if ( config.hasPath( propertyName ) ) {
            return config.getAnyRef( propertyName );
        }
        return null;
    }


    /**
     * Reloads the config
     **/
    public static void reload()
    {
        LOG.info( "Reloading Config" );
        ConfigFactory.invalidateCaches();
        Config originalConfig = ConfigFactory.load();
        // TODO Pick path from Sytem Property
        config = ConfigFactory.parseFileAnySyntax( new File( "../" + CommonConstants.APPLICATION.CONFIG_FILE ) ).resolve()
            .withFallback( originalConfig );
    }
}
