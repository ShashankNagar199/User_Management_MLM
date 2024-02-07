package com.usermanagement.util;

import com.usermanagement.services.CustomLoggerService;
//import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
//@Slf4j
public class sendMailUtils {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private CustomLoggerService customLoggerService;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Value("${spring.mail.cc.group}")
    private String ccMailGroup;
    @Value("${redirect.url}")
    private String baseURL;

    @Value("${mlm.notification.email.address}")
    private String emailAddresses;

    public void sendMail(final String url, final String userEmail, String templateName, String templateSubject) {
//        if (!isMailEnabled) {
//            return;
//        }

        final Context context = new Context();
        context.setVariable("url", url);

        final String process = templateEngine.process(templateName, context);
        try {
            final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setSubject(templateSubject);
            helper.setText(process, true);
            helper.setTo(userEmail);
            helper.setFrom(emailFrom);
            customLoggerService.sendingMailLog("Sending mail to user", templateSubject, emailFrom, new String[]{userEmail}, "INFO");
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
  //          log.error("Error faced while sending :{} email to : {}", templateSubject, userEmail);
            customLoggerService.sendingMailLog("Error faced while sending mail to user", templateSubject, emailFrom, new String[]{userEmail}, "INFO"
            );
        }
    }
}
