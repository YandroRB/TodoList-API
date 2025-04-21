package com.todolist.todo.controller;

import com.todolist.todo.service.VerificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/verificar")
@Tag(name = "Verificar")
@RequiredArgsConstructor
public class VerificarController {
    private final VerificacionService verificacionService;

    @Operation(summary = "Verifica la cuenta a traves del correo electronico"
            ,description = "Se envia un correo electronico con un token y se introduce para verificar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Se ha verificado la cuenta"
                    ,content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400",description = "El token es invalido"
                    ,content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))

    })
    @GetMapping
    public ResponseEntity<?> verificarCuenta(@RequestParam String token){
        if(verificacionService.verificarCuenta(token)){
            return ResponseEntity.ok(Map.of("mensaje","Cuenta verificada correctamente"));
        }
        return ResponseEntity.badRequest().body(Map.of("mensaje","El token es invalido o esta expirado"));
    }
}
