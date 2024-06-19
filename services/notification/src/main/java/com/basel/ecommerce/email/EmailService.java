package com.basel.ecommerce.email;

import com.basel.ecommerce.kafka.order.Product;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendPaymentSuccessEmail(String destinationEmail, String customerName, BigDecimal amount, String orderReference) throws MessagingException {
        createMimeMessage(EmailTemplate.PAYMENT_CONFIRMATION, destinationEmail, customerName, amount, orderReference, null);
    }

    @Async
    public void sendOrderConfirmationEmail(String destinationEmail, String customerName, BigDecimal amount, String orderReference, List<Product> products) throws MessagingException {
        createMimeMessage(EmailTemplate.ORDER_CONFIRMATION, destinationEmail, customerName, amount, orderReference, products);
    }

    private void createMimeMessage(EmailTemplate emailTemplate, String destinationEmail, String customerName, BigDecimal amount, String orderReference, List<Product> products) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_RELATED, StandardCharsets.UTF_8.name());
        messageHelper.setFrom("eng.baselbashir@gmail.com");
        messageHelper.setSubject(emailTemplate.getSubject());
        final String templateName = emailTemplate.getTemplate();

        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customerName);
        variables.put("orderReference", orderReference);

        if (emailTemplate == EmailTemplate.PAYMENT_CONFIRMATION) {
            variables.put("amount", amount);
        } else if (emailTemplate == EmailTemplate.ORDER_CONFIRMATION) {
            variables.put("totalAmount", amount);
            variables.put("products", products);
        }

        Context context = new Context();
        context.setVariables(variables);

        sendMail(mimeMessage, messageHelper, context, templateName, destinationEmail);
    }

    private void sendMail(MimeMessage mimeMessage, MimeMessageHelper messageHelper, Context context, String templateName, String destinationEmail) throws MessagingException {
        try {
            String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);
            messageHelper.setTo(destinationEmail);
            mailSender.send(mimeMessage);
            log.info(String.format("INFO - Email successfully sent to %s with template %s", destinationEmail, templateName));
        } catch (MessagingException e) {
            log.warn("WARNING - Cannot send email to {}", destinationEmail);
            throw e;
        }
    }

}
