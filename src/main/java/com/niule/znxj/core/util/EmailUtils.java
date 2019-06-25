package com.niule.znxj.core.util;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class EmailUtils {

//    private static final String USER = "kefu@ronglaoban.cn";
//    private static final String PASSWORD = "Kefu6676";
//    private static final String SMTP_ADDRESS = "smtp.exmail.qq.com";
//    private static final String SMTP_PORT = "465";
//    private static final String SEND_EMAIL_ADDRESS = "kefu@ronglaoban.cn";
    private static final String TITLE = "智能巡检";
    private static final String CHARSET = "utf-8";


//    public static void main(String[] args) {
//        String[] strings = new String[]{"13203927924@163.com","1497665236@qq.com"};
//        sendEmails(strings,"测试邮件发送","这是一个测试");
//    }


    public static boolean sendEmails(String USER ,String PASSWORD ,String SMTP_ADDRESS ,String SMTP_PORT,String[] emails, String title, String content){
        try {
            HtmlEmail email = new HtmlEmail();
            email.setHostName(SMTP_ADDRESS);
            email.setSslSmtpPort(SMTP_PORT);
            email.setAuthentication(USER, PASSWORD);
            email.setCharset(CHARSET);
            email.addTo(emails);
            email.setFrom(USER, TITLE, CHARSET);
            email.setSubject(title);
            email.setHtmlMsg(content);
            email.send();
            return true;
        } catch (EmailException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean sendEmails(String USER ,String PASSWORD ,String SMTP_ADDRESS ,String SMTP_PORT,String emailAddress, String title, String content){
            HtmlEmail email = new HtmlEmail();
            email.setHostName(SMTP_ADDRESS);
            email.setSslSmtpPort(SMTP_PORT);
            email.setAuthentication(USER, PASSWORD);
            email.setCharset(CHARSET);
        try {
            email.addTo(emailAddress);
            email.setFrom(USER, TITLE, CHARSET);
            email.setSubject(title);
            email.setHtmlMsg(content);
            email.send();
            return true;
        } catch (EmailException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean sendEmail(String USER ,String PASSWORD ,String SMTP_ADDRESS ,String SMTP_PORT,String emailNum, String title, String content){
        try {
            HtmlEmail email = new HtmlEmail();
            email.setHostName(SMTP_ADDRESS);
            email.setSslSmtpPort(SMTP_PORT);
            email.setAuthentication(USER, PASSWORD);
            email.setCharset(CHARSET);

            email.addTo(emailNum);
            email.setFrom(USER, TITLE, CHARSET);
            email.setSubject(title);
            email.setHtmlMsg(content);
            email.send();
            return true;
        } catch (EmailException e) {
            e.printStackTrace();
            return false;
        }

    }
}
