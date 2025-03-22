package com.todolist.todo.controller;

import com.todolist.todo.service.VerificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/verificar")
@RequiredArgsConstructor
public class VerificarController {
    private final VerificacionService verificacionService;

    @GetMapping
    public ResponseEntity<?> verificarCuenta(@RequestParam String token){
        if(verificacionService.verificarCuenta(token)){
            return ResponseEntity.ok(Map.of("mensaje","Cuenta verificada correctamente"));
        }
        return ResponseEntity.badRequest().body(Map.of("mensaje","El token es invalido o esta expirado"));
    }
}
