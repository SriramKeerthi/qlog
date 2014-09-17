/**
 * 
 */
package com.raremile.qlog.exceptions;

/**
 * @author Samarth Bhargav
 *
 */
public class PropertyDoesNotExist extends RuntimeException
{

    /**
     * 
     */
    private static final long serialVersionUID = 2059877010220865053L;


    /**
     * 
     */
    public PropertyDoesNotExist()
    {
        super();
    }


    /**
     * @param message
     * @param cause
     */
    public PropertyDoesNotExist( String message, Throwable cause )
    {
        super( message, cause );
    }


    /**
     * @param message
     */
    public PropertyDoesNotExist( String message )
    {
        super( message );
    }


    /**
     * @param cause
     */
    public PropertyDoesNotExist( Throwable cause )
    {
        super( cause );
    }

}
