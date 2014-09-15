package com.raremile.qlog.engine;

import java.util.Arrays;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.raremile.qlog.helper.CommonConstants;



public class SendMail
{
    private static Properties properties;
    static {
        properties = new Properties();
        properties.put( "mail.smtp.auth", "true" );
        properties.put( "mail.smtp.starttls.enable", "true" );
        properties.put( "mail.smtp.host", "smtp.gmail.com" );
        properties.put( "mail.smtp.port", "587" );
    }


    public static void sendMail( String handlerName, String[] recepients, String[] lines )
    {
        Session session = Session.getInstance( properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication( "sriram@raremile.com", "password" );
            }
        } );

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage( session );

            // Set From: header field of the header.
            //            message.setFrom( new InternetAddress( GlobalConfigurations.email() ) );
            message.setFrom( new InternetAddress( "sriram@raremile.com" ) );

            // Set To: header field of the header.
            for ( String recepient : recepients ) {
                message.addRecipient( Message.RecipientType.TO, new InternetAddress( recepient ) );
            }

            message.setSubject( "Exception in " + handlerName );

            message.setContent( String.format( CommonConstants.EXCEPTION_BODY, Arrays.toString( lines ) ), "text/html" );

            Transport.send( message );
        } catch ( MessagingException mex ) {
            mex.printStackTrace();
        }
    }


    public static void main( String[] args )
    {
        sendMail( "Test", new String[] { "shramk@gmail.com" }, new String[] { "Line 1", "Line 2" } );
    }
}
