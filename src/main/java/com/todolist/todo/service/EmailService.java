package com.todolist.todo.service;


import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private static  final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String remitente;

    @Async
    public void enviarEmail(String destinatario,String asunto,String contenido){
        try{
            SimpleMailMessage mensaje=new SimpleMailMessage();
            mensaje.setFrom(remitente);
            mensaje.setTo(destinatario);
            mensaje.setSubject(asunto);
            mensaje.setText(contenido);
            mailSender.send(mensaje);
            log.info("Mensaje enviado a {}", destinatario);
        }catch (Exception e){
            log.error("Error enviando email: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public void enviarEmailHtml(String destinatario,String asunto,String contenidoHTML){
        try{
            MimeMessage mensaje=mailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(mensaje,true,"utf-8");
            helper.setFrom(remitente);
            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(contenidoHTML,true);
        }catch (Exception e){
            log.error("Error enviando email html: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
