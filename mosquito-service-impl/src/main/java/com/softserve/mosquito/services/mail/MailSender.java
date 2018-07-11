package com.softserve.mosquito.services.mail;

import com.softserve.mosquito.dtos.UserDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Component
public class MailSender {

    private JavaMailSender mailSender;
    private static final Logger LOGGER = LogManager.getLogger(MailSender.class);

    @Autowired
    public MailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public boolean sendMessage(UserDto user, String message, String subject) {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            mimeMessage.setSubject(subject);
            mimeMessage.setContent(message, "text/html");
            mailSender.send(mimeMessage);
            return true;
        } catch (MessagingException e) {
            LOGGER.info("Sending message was failed!");
            LOGGER.error(e.getMessage());
        }
        return false;
    }
}
