package com.todolist.todo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Token generado a iniciar sesion, para volver a generar un nuevo token y mantener la sesion")
public class TokenRefreshRequestDTO {
    @Schema(description = "Token generado a iniciar sesion"
            ,example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
    String token;
}
