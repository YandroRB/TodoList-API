package com.todolist.todo.service;

import com.todolist.todo.dto.request.LoginRequestDTO;
import com.todolist.todo.dto.request.RegistroUsuarioRequestDTO;
import com.todolist.todo.dto.response.JwtResponseDTO;
import com.todolist.todo.enumerator.TipoRol;
import com.todolist.todo.exception.CredencialExpiradaException;
import com.todolist.todo.exception.UsuarioExistenteException;
import com.todolist.todo.model.Usuario;
import com.todolist.todo.repository.UsuarioRepository;
import com.todolist.todo.security.JwtService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostConstruct
    public void inicializarUsuarioAdmin(){
        if(usuarioRepository.findByUsername("admin").isEmpty()){
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@email.com");
            admin.setNombre("Santiago");
            admin.setApellido("Rodriguez Bone");
            admin.setRoles(Set.of(TipoRol.ADMIN));
            usuarioRepository.save(admin);
        }
    }

    public Usuario registrarUsuario(Usuario usuario) {
        if(usuarioRepository.findByUsername(usuario.getUsername()).isPresent()){
            throw new UsuarioExistenteException("El nombre "+usuario.getUsername()+" ya existe");
        }
        if(usuarioRepository.findByEmail(usuario.getEmail()).isPresent()){
            throw new UsuarioExistenteException("El correo "+usuario.getEmail()+" ya está registrado");
        }
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(usuario.getUsername());
        nuevoUsuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        nuevoUsuario.setEmail(usuario.getEmail());
        nuevoUsuario.setNombre(usuario.getNombre());
        nuevoUsuario.setApellido(usuario.getApellido());
        nuevoUsuario.setRoles(Set.of(TipoRol.USER));
        return usuarioRepository.save(nuevoUsuario);
    }

    public JwtResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getUsername(),
                        loginRequestDTO.getPassword())
        );
        if(authentication.isAuthenticated()){
            UserDetails userDetails= userDetailsService.loadUserByUsername(loginRequestDTO.getUsername());
            Map<String,Object> extraClaims= new HashMap<>();
            String token= jwtService.generateToken(extraClaims,userDetails);
            String refreshToken= jwtService.generateRefreshToken(userDetails);
            return  JwtResponseDTO.builder()
                    .token(token)
                    .tokenRefresh(refreshToken)
                    .username(loginRequestDTO.getUsername())
                    .build();
        }
        throw new UsernameNotFoundException("Credenciales invalidas");
    }
    public JwtResponseDTO refreshToken(String refreshToken) {
        if(jwtService.isValidateToken(refreshToken)){
            String username= jwtService.extractUsername(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            Map<String,Object> extraClaims= new HashMap<>();
            String token= jwtService.generateToken(extraClaims,userDetails);
            return JwtResponseDTO.builder()
                    .tokenRefresh(refreshToken)
                    .token(token)
                    .username(username)
                    .build();
        }
        throw new CredencialExpiradaException("El refresh token es invalido o está expirado");
    }
}
