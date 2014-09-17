package com.raremile.qlog;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raremile.qlog.engine.Handler;
import com.raremile.qlog.exceptions.PropertyDoesNotExist;
import com.raremile.qlog.helper.CommonConstants;
import com.raremile.qlog.helper.Configurations;


public class QLog
{

    private static Map<String, Handler> handlers = new HashMap<String, Handler>();


    public QLog()
    {
        checkPreConditions();
    }


    private void checkPreConditions()
    {
        String[] propertiesToCheck = { CommonConstants.APPLICATION.HANDLERS, CommonConstants.APPLICATION.LINES_AFTER,
            CommonConstants.APPLICATION.LINES_BEFORE };
        for ( String property : propertiesToCheck ) {
            if ( Configurations.getObject( property ) == null ) {
                throw new PropertyDoesNotExist( "Property " + property + " must be defined" );
            }
        }
    }


    public void addHandler( String handlerName, Handler handler )
    {
        handlers.put( handlerName, handler );
    }


    public boolean startHandler( String handlerName )
    {
        if ( handlers.containsKey( handlerName ) ) {
            handlers.get( handlerName ).start();
            return true;
        }
        return false;
    }


    public void stopHandler( String handlerName )
    {
        if ( handlers.containsKey( handlerName ) ) {
            handlers.get( handlerName ).stopHandler();
        }
    }


    public static void main( String[] args ) throws IOException, InterruptedException
    {
        QLog qlog = new QLog();

        List<String> handlers = Configurations.getStringOrList( CommonConstants.APPLICATION.HANDLERS );
        for ( String handlerName : handlers ) {
            qlog.addHandler( handlerName, new Handler( handlerName ) );
            qlog.startHandler( handlerName );
        }
    }
}
