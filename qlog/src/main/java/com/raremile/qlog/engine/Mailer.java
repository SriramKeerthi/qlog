package com.raremile.qlog.engine;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.raremile.qlog.exceptions.MailingException;
import com.raremile.qlog.helper.CommonConstants;
import com.raremile.qlog.helper.Configurations;


public class Mailer
{

    private static final Logger LOG = LoggerFactory.getLogger( Mailer.class );

    private static Properties properties;

    static {
        properties = new Properties();
        properties.put( "mail.smtp.auth", "true" );
        properties.put( "mail.smtp.starttls.enable", "true" );
        properties.put( "mail.smtp.host", "smtp.gmail.com" );
        properties.put( "mail.smtp.port", "587" );
    }

    private Session session = Session.getInstance( properties, new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication()
        {
            return new PasswordAuthentication( Configurations.getString( CommonConstants.APPLICATION.EMAIL ), Configurations
                .getString( CommonConstants.APPLICATION.PASSWORD ) );
        }
    } );


    public void sendMail( String handlerName, String[] recepients, String[] lines ) throws MailingException
    {

        try {
            MimeMessage message = new MimeMessage( session );

            message.setFrom( new InternetAddress( Configurations.getString( CommonConstants.APPLICATION.EMAIL ) ) );

            for ( String recepient : recepients ) {
                message.addRecipient( Message.RecipientType.TO, new InternetAddress( recepient ) );
                LOG.info( "{}: Sending Mail", recepient );
            }

            message.setSubject( "Exception in " + handlerName );

            message.setContent( String.format( CommonConstants.EXCEPTION_BODY, formatStringArray( lines ) ), "text/html" );

            Transport.send( message );
            LOG.info( "{}: Sent Mail", handlerName );
        } catch ( MessagingException mex ) {
            throw new MailingException( mex );
        }
    }


    private static String formatStringArray( String[] lines )
    {
        StringBuilder builder = new StringBuilder();
        for ( String line : lines ) {
            builder.append( line + "<br>" );
        }
        return builder.toString();
    }
}
