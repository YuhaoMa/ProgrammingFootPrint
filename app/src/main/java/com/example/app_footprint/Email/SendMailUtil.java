package com.example.app_footprint.Email;

import android.content.Context;
import android.content.Intent;

import java.io.File;

public class SendMailUtil {
    //qq
    private static final String HOST = "smtp.qq.com";
    private static final String PORT = "587";
    private static final String FROM_ADD = "424224995@qq.com"; //发送方邮箱
    private static final String FROM_PSW = "duvmiukpdyqobhjh";//发送方邮箱授权码


    public static void send(final File file, String toAdd, String code,int type , String groupName) {
        final MailInfo mailInfo = creatMail(toAdd, code,type,groupName);
        final MailSender sms = new MailSender();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sms.sendFileMail(mailInfo, file);
            }
        }).start();
    }

    public static void send(String toAdd, String code,int type,String groupName) {
        final MailInfo mailInfo = creatMail(toAdd, code,type,groupName);
        final MailSender sms = new MailSender();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sms.sendTextMail(mailInfo);
            }
        }).start();
    }

    private static MailInfo creatMail(String toAdd, String code,int type,String groupName) {
        final MailInfo mailInfo = new MailInfo();
        mailInfo.setMailServerHost(HOST);
        mailInfo.setMailServerPort(PORT);
        mailInfo.setValidate(true);
        mailInfo.setUserName(FROM_ADD); // 你的邮箱地址
        mailInfo.setPassword(FROM_PSW);// 您的邮箱密码
        mailInfo.setFromAddress(FROM_ADD); // 发送的邮箱
        mailInfo.setToAddress(toAdd); // 发到哪个邮件去
        mailInfo.setSubject("This is your FootPrint verification code!"); // 邮件主题
    switch (type){
        case 1:
            mailInfo.setContent("Please copy the 8-bit verification code below." +
                    "To complete the registration" + code); // 注册用户邮件文本
            break;
        case 2:
            mailInfo.setContent("Congratulations! Your \""+groupName+"\" group has been created successfully!\n" +
                    "Please save the invitation code: "+code+".\n" +
                    "Send invitation codes to invite others to join your group!🙂"); // 注册group邮件文本
        case 3:
            mailInfo.setContent("Forgot your password? \n"+"Copy the verification code below : \n         "+
                    code+"\n and set your new password");
    }


        return mailInfo;
    }


}