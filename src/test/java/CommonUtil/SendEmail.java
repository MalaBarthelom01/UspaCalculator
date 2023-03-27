package CommonUtil;



import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

    public class SendEmail
    {

        public static void mailSend(){
            String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
            // Get a Properties object
            Properties props = System.getProperties();
            props.setProperty("mail.smtp.host", "smtp.gmail.com");
            props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            props.setProperty("mail.smtp.port", "465");
            props.setProperty("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.auth", "true");
            props.put("mail.debug", "true");
            props.put("mail.store.protocol", "pop3");
            props.put("mail.transport.protocol", "smtp");
            final String username = "mala.barthelom@gmail.com";//
            final String password = "Ze1234123$";
            try{
                Session session = Session.getDefaultInstance(props,
                        new Authenticator(){
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(username, password);
                            }});

                // -- Create a new message --
                Message msg = new MimeMessage(session);

                // -- Set the FROM and TO fields --
                msg.setFrom(new InternetAddress("mala.barthelom@gmail.com"));
                msg.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse("mala.barthelom@gmail.com",false));
                msg.setSubject("Hello");
                msg.setText("How are you");
                msg.setSentDate(new Date());

                // Create a multipar message
                BodyPart messageBodyPart = new MimeBodyPart();
                String htmlText = "<H1>Hello</H1><img src=\"cid:image\" height=\"600\" width=\"1200\">";
                messageBodyPart.setContent(htmlText, "text/html");
                //messageBodyPart.setText("This is message body");
                Multipart multipart = new MimeMultipart();

                // Set text message part
                multipart.addBodyPart(messageBodyPart);

                // -- Add Attachment --
                messageBodyPart = new MimeBodyPart();

                String filename = System.getProperty("user.dir") + "/target/automation-reports/email_Snapshot.png";
                DataSource source = new FileDataSource(filename);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(filename);
                messageBodyPart.setHeader("Content-ID", "<image>");
                multipart.addBodyPart(messageBodyPart);

                // Send the complete message parts
                msg.setContent(multipart);

                Transport.send(msg);
                System.out.println("Message sent.");
            }catch (MessagingException e){ System.out.println("Erreur d'envoi, cause: " + e);}
        }
}