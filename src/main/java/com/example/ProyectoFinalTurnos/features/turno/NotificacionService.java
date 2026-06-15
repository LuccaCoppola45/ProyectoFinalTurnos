package com.example.ProyectoFinalTurnos.features.turno;

import com.example.ProyectoFinalTurnos.features.turno.Dtos.TurnoResponceDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;



@Service
public class NotificacionService {

    private JavaMailSender mailSender;
    private TemplateEngine templateEngine;
    // Inyección de dependencias por constructor

    public NotificacionService(JavaMailSender mailSender,  TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }
    @Async
    public void enviarEmailConPlantilla(String destinatario, String asunto, String nombreTemplate, Context context) {
        System.out.println("Hilo actual: " + Thread.currentThread().getName());
        try {
            // 1. PROCESAR EL HTML AQUÍ (Aquí es donde Thymeleaf hace su magia)
            String htmlContent = templateEngine.process(nombreTemplate, context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setFrom("luccacoppola@gmail.com");

            // 2. Enviar el contenido procesado
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}