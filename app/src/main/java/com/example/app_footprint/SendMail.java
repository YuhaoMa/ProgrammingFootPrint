package com.example.app_footprint;
import android.media.tv.TvInputService;

import java.security.GeneralSecurityException;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.sun.mail.util.MailSSLSocketFactory;

/**
 * Email Function : User login + sign_up
 */
class SendEmail
{
    private String user_mail ;
   // private  String  info ;
    private  Session mySession;
    private  String  from_add;
    public SendEmail(String user )
    {
        //Recipient email address
        user_mail = user;
        from_add = "424224995@qq.com";

    }
    private void GenerateMailInfo() throws GeneralSecurityException {
        // Sender email address

        // The host that sends the email: smtp.qq.com
        String host = "smtp.qq.com";  //QQ mail host

        // Obtaining System properties
        Properties properties = System.getProperties();

        // Setting the Mail Server
        properties.setProperty("mail.smtp.host", host);

        properties.put("mail.smtp.auth", "true");
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);
        // 获取默认session对象
         mySession = Session.getDefaultInstance(properties,new Authenticator(){
            public PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication("424224995@qq.com", "duvmiukpdyqobhjh"); //Sender email user name and password
            }
        });}

    public void sendEmail(String head,String info)  {

        try{
            // Create a default MimeMessage object
            GenerateMailInfo();
            MimeMessage message = new MimeMessage(mySession);
            // Set From: 头部头字段
            message.setFrom(new InternetAddress(from_add));

            // Set To: 头部头字段
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(user_mail));

            // Set Subject: 头部头字段
            message.setSubject(head);
            // 设置消息体
            message.setText(info);

            // 发送消息
            Transport.send(message);
            System.out.println("Sent message successfully");
        }catch (MessagingException | GeneralSecurityException mex) {
            mex.printStackTrace();
        }
    }



public  void GenerateCode()
{
    /**
     * Generate a code to verify email_address
     */
}
    /**
     * Test SendEmail
     */
  static class  emailTest {
      public static void main(String args[])  {
          SendEmail email = new SendEmail("1298163609@qq.com");
          email.sendEmail("Test_Email","Test");

      }
    }
}