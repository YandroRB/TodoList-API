package com.todolist.todo.controller;

import com.todolist.todo.exception.CredencialExpiradaException;
import com.todolist.todo.exception.EntradaInvalidaException;
import com.todolist.todo.exception.RecursoNoEncontradoException;
import com.todolist.todo.exception.UsuarioExistenteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<?> recursoNoEncontradoException(RecursoNoEncontradoException e, WebRequest request) {
        Map<String,Object> detalleError=new HashMap<>();
        detalleError.put("mensaje",e.getMessage());
        detalleError.put("detalle",request.getDescription(false));
        return new ResponseEntity<>(detalleError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntradaInvalidaException.class)
    public ResponseEntity<?> entradaInvalidaException(EntradaInvalidaException e, WebRequest request) {
        Map<String,Object> detalleError=new HashMap<>();
        detalleError.put("mensaje",e.getMessage());
        detalleError.put("detalle",request.getDescription(false));
        return new ResponseEntity<>(detalleError, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UsuarioExistenteException.class)
    public ResponseEntity<?> usuarioExistenteException(UsuarioExistenteException e, WebRequest request) {
        Map<String,Object> detalleError=new HashMap<>();
        detalleError.put("mensaje",e.getMessage());
        detalleError.put("detalle",request.getDescription(false));
        return new ResponseEntity<>(detalleError, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(CredencialExpiradaException.class)
    public ResponseEntity<?> credencialExpiradaException(CredencialExpiradaException e, WebRequest request) {
        Map<String,Object> detalleError=new HashMap<>();
        detalleError.put("mensaje",e.getMessage());
        detalleError.put("detalle",request.getDescription(false));
        return new ResponseEntity<>(detalleError, HttpStatus.FORBIDDEN);
    }
}
