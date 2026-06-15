package com.example.ProyectoFinalTurnos.features.turno;

import com.example.ProyectoFinalTurnos.features.turno.Dtos.TurnoResponceDto;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;



@Service
public class NotificacionService {

    private final JavaMailSender mailSender;

    // Inyección de dependencias por constructor
    public NotificacionService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void enviarEmail(String destinatario, String asunto, String mensaje) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(destinatario);
        mail.setSubject(asunto);
        mail.setText(mensaje);
        mail.setFrom("luccacoppola@gmail.com"); // Debe coincidir con tu configuración

        mailSender.send(mail);
        System.out.println("Mail enviado correctamente a: " + destinatario);
    }
}
