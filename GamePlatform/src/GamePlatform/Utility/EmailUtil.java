import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.util.Random;

public class EmailUtil {
    private static final String FROM_EMAIL = "1335549399@qq.com";
    private static final String PASSWORD = "nhcoldnjavlogdhc";
    
    public static String sendVerificationCode(String toEmail) {
        String verificationCode = String.format("%04d", new Random().nextInt(10000));
        
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");  // 使用SSL
        props.put("mail.smtp.host", "smtp.qq.com");
        props.put("mail.smtp.port", "465");         // SSL端口
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        });
        
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Game Platform - Email Verification Code");
            message.setText("Your verification code is: " + verificationCode + 
                          "\nThis verification code will expire in 10 minutes.");
            
            Transport.send(message);
            return verificationCode;
            
        } catch (MessagingException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }
} 