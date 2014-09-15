package com.raremile.qlog.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import com.raremile.qlog.entities.LogBuffer;
import com.raremile.qlog.helper.CommonConstants;
import com.raremile.qlog.helper.GlobalConfigurations;


public class Handler extends Thread
{
    private String handlerName;
    private Map<String, Object> handlerConfiguration;


    public Handler( String handlerName, Map<String, Object> handlerConfiguration )
    {
        super( handlerName );
        this.handlerName = handlerName;
        this.handlerConfiguration = handlerConfiguration;
    }


    @SuppressWarnings ( "unchecked")
    @Override
    public void run()
    {
        int linesBefore = GlobalConfigurations.linesBefore();
        int linesAfter = GlobalConfigurations.linesAfter();
        LogBuffer logBuffer = new LogBuffer( linesBefore + linesAfter + 1 );
        try {
            long waitTimeout = 1000 * 60 * 60 * 24; // One day
            if ( handlerConfiguration.containsKey( CommonConstants.WAIT_TIMEOUT ) ) {
                waitTimeout = Long.parseLong( (String) handlerConfiguration.get( CommonConstants.WAIT_TIMEOUT ) );
            }
            File logFile = new File( (String) handlerConfiguration.get( CommonConstants.LOG_FILE_NAME ) );
            BufferedReader br = new BufferedReader( new InputStreamReader( new FileInputStream( logFile ) ) );
            long lastMessageTime = System.currentTimeMillis();
            boolean timedOut = false;
            boolean exceptionFound = false;
            int linesLeft = linesAfter + 1;
            while ( true ) {
                while ( !br.ready() ) {
                    sleep( 100 );
                    if ( System.currentTimeMillis() - lastMessageTime > waitTimeout ) {
                        timedOut = true;
                        break;
                    }
                }
                if ( timedOut ) {
                    break;
                }
                lastMessageTime = System.currentTimeMillis();
                String log = br.readLine();
                logBuffer.addLine( log );
                linesLeft--;
                if ( matches( log ) ) {
                    exceptionFound = true;
                    linesLeft = linesAfter;
                }
                if ( linesLeft == 0 && exceptionFound ) {
                    exceptionFound = false;
                    if ( handlerConfiguration.get( CommonConstants.EMAIL ) instanceof String ) {
                        SendMail.sendMail( handlerName,
                            new String[] { (String) handlerConfiguration.get( CommonConstants.EMAIL ) }, logBuffer.getLines() );
                    } else {
                        SendMail.sendMail( handlerName,
                            ( (List<String>) handlerConfiguration.get( CommonConstants.EMAIL ) ).toArray( new String[] {} ),
                            logBuffer.getLines() );
                    }
                }
            }
            br.close();
        } catch ( IOException e ) {
            e.printStackTrace();
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
    }


    private boolean matches( String log )
    {
        return false;
    }
}
