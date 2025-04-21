package com.todolist.todo.controller;

import com.todolist.todo.documentation.AuthControllerDocumentation;
import com.todolist.todo.dto.request.LoginRequestDTO;
import com.todolist.todo.dto.request.RegistroUsuarioRequestDTO;
import com.todolist.todo.dto.request.TokenRefreshRequestDTO;
import com.todolist.todo.dto.response.JwtResponseDTO;
import com.todolist.todo.dto.response.UsuarioResumenDTO;
import com.todolist.todo.exception.EntradaInvalidaException;
import com.todolist.todo.model.Usuario;
import com.todolist.todo.service.AuthService;
import com.todolist.todo.utility.GenericoDTOConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.modelmapper.internal.bytebuddy.implementation.bytecode.Throw;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class AuthController implements AuthControllerDocumentation {
    private final AuthService authService;
    private final GenericoDTOConverter genericoDTOConverter;

    @Override
    @PostMapping("/registro")
    public ResponseEntity<UsuarioResumenDTO> registrarUsuario(@Valid @RequestBody RegistroUsuarioRequestDTO registroDTO
            , BindingResult result) {
        if(result.hasErrors()) {
            String err=result.getFieldErrors().stream()
                    .map(error->"Campo '"+error.getField()+"' : "+error.getDefaultMessage())
                    .collect(Collectors.joining(" ; "));
            throw new EntradaInvalidaException("Error al registrar el usuario: "+err);

        }
        Usuario usuarioRequest=genericoDTOConverter.convertirAEntidad(registroDTO, Usuario.class);
        Usuario usuario=authService.registrarUsuario(usuarioRequest);
        UsuarioResumenDTO usuarioResponse=genericoDTOConverter.convertirADTO(usuario,UsuarioResumenDTO.class);
        return new ResponseEntity<>(usuarioResponse, HttpStatus.CREATED);

    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginDTO, BindingResult result){
        if(result.hasFieldErrors()){
            String err= result.getFieldErrors().stream()
                    .map(error->"Campo '"+error.getField() +"' : "+error.getDefaultMessage())
                    .collect(Collectors.joining(" ; "));
            throw  new EntradaInvalidaException("Error al iniciar sesion: "+err);
        }
        JwtResponseDTO jwtResponseDTO = authService.login(loginDTO);
        return new ResponseEntity<>(jwtResponseDTO, HttpStatus.OK);

    }

    @Override
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDTO> refresh(@Valid @RequestBody TokenRefreshRequestDTO refreshDTO, BindingResult result){
        if(result.hasErrors()){
            throw new EntradaInvalidaException("Error al refrescar el token, entrada invalida: "+result.getFieldErrors().stream());
        }
        JwtResponseDTO jwtResponseDTO=authService.refreshToken(refreshDTO.getToken());
        return new ResponseEntity<>(jwtResponseDTO, HttpStatus.OK);
    }
}
