package com.todolist.todo.service;

import com.todolist.todo.model.Usuario;
import com.todolist.todo.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificacionService {
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;
    @Value("${app.url:http://localhost:8080}")
    private String appUrl;

    public void enviarEmailVerificacion(Usuario usuario) {
        String token= UUID.randomUUID().toString();
        usuario.setTokenVerificacion(token);
        usuarioRepository.save(usuario);
        String enlaceVerificacion=appUrl+"/api/verificar?token="+token;
        String asunto="Verificar cuenta";
        String contenido=String.format(
                "Hola %s, \n\nPor favor verifique su cuenta haciendo click a traves del enlace:\n%s\nSaludos, \nYandroDev",
                usuario.getUsername(),enlaceVerificacion
        );
        emailService.enviarEmail(usuario.getEmail(), asunto, contenido);
    }
    public boolean verificarCuenta(String token) {
        return usuarioRepository.findByTokenVerificacion(token).
                map(usuario->{
                    usuario.setCuentaVerificada(true);
                    usuario.setTokenVerificacion(null);
                    usuarioRepository.save(usuario);
                    return true;
                }).orElse(false);
    }
}
