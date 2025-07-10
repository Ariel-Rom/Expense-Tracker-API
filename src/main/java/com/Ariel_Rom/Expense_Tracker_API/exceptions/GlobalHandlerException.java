package com.Ariel_Rom.Expense_Tracker_API.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j // Genera un logger para la clase (usado para registrar mensajes en consola o archivo)
@RestControllerAdvice // Controlador global para manejar excepciones en toda la aplicación REST
public class GlobalHandlerException {

    // Metodo auxiliar para construir la respuesta de error estándar con el cuerpo ApiErrorResponse
    private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status, String message, Map<String, String> errors){
        ApiErrorResponse body = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now()) // Fecha y hora actual del error
                .status(status.value()) // Código HTTP del error (ej: 404, 500)
                .error(status.getReasonPhrase()) // Frase asociada al código HTTP (ej: "Not Found")
                .message(message) // Mensaje descriptivo del error
                .errors(errors) // Detalles específicos (puede ser null)
                .build();
        return ResponseEntity.status(status).body(body); // Construye la respuesta HTTP con el cuerpo
    }

    // Maneja excepciones cuando una entidad no se encuentra (404)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleEntityNotFound(EntityNotFoundException exception){
        log.warn("Resource not found: {}", exception.getMessage()); // Log de advertencia
        String message = exception.getMessage(); // Mensaje de error desde la excepción
        return buildResponse(HttpStatus.NOT_FOUND, message, null);
    }

    // Maneja violaciones de integridad (ej: duplicados) con código 409 Conflict
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException exception){
        log.warn("Conflict: Data already exists. Please use unique values.");
        String message = "Data already exists. Please use unique values.";
        return buildResponse(HttpStatus.CONFLICT, message, null);
    }

    // Maneja errores de validación de campos (ej: @NotNull, @Positive) con 400 Bad Request
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationErrors(MethodArgumentNotValidException exception){
        Map<String, String> errors = new HashMap<>();
        // Por cada error de validación agrega el campo y su mensaje
        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        log.info("Validation error: {}", errors);
        String message = "Validation failed for one or more fields";
        return buildResponse(HttpStatus.BAD_REQUEST, message, errors);
    }

    // Maneja cualquier excepción genérica no controlada con código 500 Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception exception){
        log.error("Internal server error. Contact support.", exception);
        String message = exception.getMessage();
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, message, null);
    }

    // Maneja errores de JSON malformado o datos inválidos con 400 Bad Request
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleUnreadable(HttpMessageNotReadableException exception){
        log.info("Bad Request: Malformed JSON or invalid value");
        String message = "Malformed JSON or invalid value";
        return buildResponse(HttpStatus.BAD_REQUEST, message, null);
    }

    // ======= Excepciones relacionadas con autenticación ======= //

    // Maneja fallos de autenticación (credenciales inválidas) con 401 Unauthorized
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleCredential(BadCredentialsException exception){
        log.warn("Authentication failed: {}", exception.getMessage());
        String message = "Email or password is incorrect";
        return buildResponse(HttpStatus.UNAUTHORIZED, message, null);
    }

    // Maneja tokens JWT expirados con 401 Unauthorized
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiErrorResponse> handleExpiredJWT(ExpiredJwtException exception){
        log.warn("Expired JWT: {} ", exception.getMessage());
        String message = "Token has expired";
        return buildResponse(HttpStatus.UNAUTHORIZED, message, null);
    }

    // Maneja tokens JWT inválidos o malformados con 401 Unauthorized
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidToken(JwtException exception){
        log.warn("Invalid JWT: {} ", exception.getMessage());
        String message = "Invalid or malformed token";
        return buildResponse(HttpStatus.UNAUTHORIZED, message, null);
    }

}
