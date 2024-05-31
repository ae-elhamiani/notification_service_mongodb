package com.eainfo.notificationService.service;

import com.eainfo.notificationService.model.EmailTemplate;
import com.eainfo.notificationService.model.Notification;
import com.eainfo.notificationService.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderService {

    @Autowired
    private final JavaMailSender mailSender;
    @Autowired
    private final EmailTemplateService emailTemplateService;
    @Autowired
    private final ThymeleafService thymeleafService;
    @Autowired
    private final NotificationRepository notificationRepository;

    public void sendEmail(String toEmail, String templateName, Map<String, Object> variables) {
        try {
            Optional<EmailTemplate> emailTemplateOptional = thymeleafService.getEmailTemplate(templateName);
            if (!emailTemplateOptional.isPresent()) {
                throw new RuntimeException("Template not found");
            }
            EmailTemplate emailTemplate = emailTemplateOptional.get();

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            helper.setTo(toEmail);
            helper.setSubject(emailTemplate.getSubject());
            helper.setText(thymeleafService.createContent(emailTemplate.getContent(), variables), true);

            mailSender.send(message);

            // Enregistrer la notification dans la base de données
            Notification notification = new Notification();
            notification.setEmail(toEmail);
            notification.setTemplateName(emailTemplate.getName());
            notification.setCreatedDate(new Date());
            notification.setSent(true);

            notificationRepository.save(notification);

        } catch (Exception e) {
            log.error("Failed to send email", e);
            // Enregistrer l'erreur de notification dans la base de données
            Notification notification = new Notification();
            notification.setEmail(toEmail);
            notification.setException("Failed: " + e);
            notification.setCreatedDate(new Date());
            notification.setSent(false);

            notificationRepository.save(notification);
        }
    }
}
