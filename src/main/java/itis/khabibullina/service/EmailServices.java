package itis.khabibullina.service;

import itis.khabibullina.config.MailConfig;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.util.*;


@Service
public class EmailServices {
    private final String username;
    private final String password;
    private final String symbols = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm1234567890";
    private final FreeMarkerConfigurer freeMarkerConfigurer;

    public EmailServices(MailConfig mailConfig, FreeMarkerConfigurer freeMarkerConfigurer) {
        this.freeMarkerConfigurer = freeMarkerConfigurer;

        this.username = mailConfig.getUsername();
        this.password = mailConfig.getPassword();
    }


    public String sendEmail(String to) throws IOException, TemplateException {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", true);

        Session session = Session.getDefaultInstance(props);
        session.setDebug(false);

        String temporalPassword = generatePassword();
        Map<String, Object> model = new HashMap<>();
        model.put("email", to);
        model.put("password", temporalPassword);

        Template template = freeMarkerConfigurer.getConfiguration().getTemplate("email-template.ftl");
        String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(username));
            msg.setRecipients(Message.RecipientType.TO, to);
            msg.setSubject("Верификация");
            msg.setContent(htmlBody, "text/html; charset=UTF-8");
            msg.setSentDate(new Date());
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", 587, username, password);
            transport.sendMessage(msg, msg.getAllRecipients());
        } catch (MessagingException mex) {
            mex.printStackTrace();
            Exception ex;
            if ((ex = mex.getNextException()) != null) {
                ex.printStackTrace();
            }
        }
        return temporalPassword;

    }

    private String generatePassword() {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            stringBuilder.append(symbols.charAt(random.nextInt(symbols.length())));
        }
        System.out.println("pass " + stringBuilder.toString());
        return stringBuilder.toString();
    }
}
