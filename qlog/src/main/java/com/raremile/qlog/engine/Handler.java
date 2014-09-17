package com.raremile.qlog.engine;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;

import com.raremile.qlog.entities.LogBuffer;
import com.raremile.qlog.exceptions.PropertyDoesNotExist;
import com.raremile.qlog.helper.CommonConstants;
import com.raremile.qlog.helper.Configurations;


public class Handler extends Thread
{
    private String handlerName;
    private Pattern regex;
    private String startsWith;
    private String endsWith;


    private LogBuffer logBuffer;
    private volatile boolean stopProcessing;


    public Handler( String handlerName ) throws PropertyDoesNotExist
    {
        this.handlerName = handlerName;
        checkPreConditions();
        if ( this.getHandlerProperty( CommonConstants.HANDLER.REGEX ) != null ) {
            this.regex = Pattern.compile( this.getHandlerProperty( CommonConstants.HANDLER.REGEX ) );
        }

        if ( this.getHandlerProperty( CommonConstants.HANDLER.STARTS_WITH ) != null ) {
            this.startsWith = this.getHandlerProperty( CommonConstants.HANDLER.STARTS_WITH );
        }

        if ( this.getHandlerProperty( CommonConstants.HANDLER.ENDS_WITH ) != null ) {
            this.endsWith = this.getHandlerProperty( CommonConstants.HANDLER.ENDS_WITH );
        }

        logBuffer = new LogBuffer( Configurations.getInt( CommonConstants.APPLICATION.LINES_AFTER )
            + Configurations.getInt( CommonConstants.APPLICATION.LINES_BEFORE ) + 1 );
    }


    private void checkPreConditions() throws PropertyDoesNotExist
    {
        String[] propertiesToCheck = { CommonConstants.HANDLER.LOG_FILE };
        for ( String property : propertiesToCheck ) {
            if ( this.getObjectHandlerProperty( property ) == null ) {
                throw new PropertyDoesNotExist( "Property " + this.handlerName + "." + property + " must be defined" );
            }
        }
    }


    @Override
    public void run()
    {
        Tailer tailer = Tailer.create( new File( this.getHandlerProperty( CommonConstants.HANDLER.LOG_FILE ) ),
            new LogListener(), 500 );
        while ( !stopProcessing ) {

        }
        tailer.stop();
    }


    private boolean matches( String log )
    {
        boolean matches = false;

        if ( this.regex != null ) {
            matches |= this.regex.matcher( log ).matches();
        }

        if ( this.startsWith != null ) {
            matches |= log.startsWith( startsWith );
        }

        if ( this.endsWith != null ) {
            matches |= log.endsWith( endsWith );
        }

        return matches;
    }


    private String getHandlerProperty( String property )
    {
        return Configurations.getString( this.handlerName + "." + property );
    }


    private List<String> getListHandlerProperty( String property )
    {
        return Configurations.getStringOrList( this.handlerName + "." + property );
    }


    private Object getObjectHandlerProperty( String property )
    {
        return Configurations.getObject( this.handlerName + "." + property );
    }


    /**
     * 
     * Class that listens to Logs and processes it line by line
     * @author Samarth Bhargav
     *
     */
    private class LogListener extends TailerListenerAdapter
    {

        private int linesLeft = Configurations.getInt( CommonConstants.APPLICATION.LINES_AFTER ) + 1;
        private boolean exceptionFound = false;


        @Override
        public void handle( String log )
        {
            logBuffer.addLine( log );
            linesLeft--;
            if ( matches( log ) ) {
                exceptionFound = true;
                linesLeft = Configurations.getInt( CommonConstants.APPLICATION.LINES_AFTER ) + 1;
            }
            if ( linesLeft == 0 && exceptionFound ) {
                exceptionFound = false;
                List<String> toEmails = null;
                if ( getObjectHandlerProperty( CommonConstants.HANDLER.EMAIL ) == null ) {
                    toEmails = Configurations.getStringOrList( CommonConstants.GLOBAL.EMAILS );
                } else {
                    toEmails = getListHandlerProperty( CommonConstants.HANDLER.EMAIL );
                }
                SendMail.sendMail( handlerName, ( toEmails ).toArray( new String[] {} ), logBuffer.getLines() );
            }
        }
    }


    public void stopHandler()
    {
        this.stopProcessing = true;
    }
}
