package com.example.ProyectoFinalTurnos.exceptions;

import com.example.ProyectoFinalTurnos.exceptions.auth.*;
import com.example.ProyectoFinalTurnos.exceptions.cancelacion.CancelacionNotFoundException;
import com.example.ProyectoFinalTurnos.exceptions.catalog.ComercioAlreadyExistsException;
import com.example.ProyectoFinalTurnos.exceptions.catalog.ComercioInvalidDataException;
import com.example.ProyectoFinalTurnos.exceptions.catalog.ComercioNotFoundException;
import com.example.ProyectoFinalTurnos.exceptions.categoria.CategoriaAlreadyExistsException;
import com.example.ProyectoFinalTurnos.exceptions.categoria.CategoriaInUseException;
import com.example.ProyectoFinalTurnos.exceptions.categoria.CategoriaNoEncontradaException;
import com.example.ProyectoFinalTurnos.exceptions.horarioAtencion.*;
import com.example.ProyectoFinalTurnos.exceptions.servicio.ServicioNotFoundException;
import com.example.ProyectoFinalTurnos.exceptions.turno.*;
import com.example.ProyectoFinalTurnos.exceptions.user.*;
import lombok.NonNull;
import com.example.ProyectoFinalTurnos.exceptions.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {



    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<@NonNull ErrorResponse> handlerResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request){
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value()) // 404
                .error(HttpStatus.NOT_FOUND.getReasonPhrase()) // "Not Found"
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    //Metodo que captura las excepciones d ela dependencia de Validation que definimos en las entidades
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();

        // Recorremos todos los errores que saltaron en la validación
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String campo = ((FieldError) error).getField();
            String mensaje = error.getDefaultMessage();
            errores.put(campo, mensaje);
        });

        return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity <@NonNull ErrorResponse> handleAccountLockedException(AccountLockedException ex, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountNotValidateException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleAccountNotValidateException(AccountNotValidateException ex, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordMissMatchException.class)
    public ResponseEntity<@NonNull ErrorResponse> handlePasswordMissMatchException(PasswordMissMatchException ex, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ResponseEntity<@NonNull ErrorResponse> handlerRefreshTokenExpiredException(RefreshTokenExpiredException ex, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public ResponseEntity<@NonNull ErrorResponse> handlerRefreshTokenNotFoundException(RefreshTokenNotFoundException ex, HttpServletRequest request){
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(VerificationTokenInvalidException.class)
    public ResponseEntity<@NonNull ErrorResponse> handlerVerificationTokenInvalidException(VerificationTokenInvalidException ex, HttpServletRequest request){
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WrongCredentialsException.class)
    public ResponseEntity<@NonNull ErrorResponse> handlerWrongCredentialsException(WrongCredentialsException ex, HttpServletRequest request){
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ComercioAlreadyExistsException.class)
    public ResponseEntity<@NonNull ErrorResponse> handlerComercioAlreadyExistsException(ComercioAlreadyExistsException ex, HttpServletRequest request){
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ComercioInvalidDataException.class)
    public ResponseEntity<@NonNull ErrorResponse> handlerComercioInvalidDataException(ComercioInvalidDataException ex, HttpServletRequest requestH){
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(requestH.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ComercioNotFoundException.class)
    public ResponseEntity<@NonNull ErrorResponse> handlerComercioNotFoundException(ComercioNotFoundException ex, HttpServletRequest requestH){
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(requestH.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNameAlreadyInUseException.class)
    public ResponseEntity<@NonNull ErrorResponse> handlerUserNameAlreadyInUseException(UserNameAlreadyInUseException ex, HttpServletRequest request){
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UserProfileNotFoundException.class)
    public ResponseEntity<@NonNull ErrorResponse> handlerUserProfileNotFoundException(UserProfileNotFoundException ex, HttpServletRequest request){
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TurnoNotFoundException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleTurnoNotFoundException(TurnoNotFoundException ex, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value()) // 404
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedTurnoAccessException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleUnauthorizedTurnoAccessException(UnauthorizedTurnoAccessException ex, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN.value()) // 403
                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(TurnoPastDateException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleTurnoPastDateException(TurnoPastDateException ex, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value()) // 400
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TurnoAlreadyCancelledException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleTurnoAlreadyCancelledException(TurnoAlreadyCancelledException ex, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value()) // 400
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CancelacionNotFoundException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleCancelacionNotFoundException(CancelacionNotFoundException ex, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value()) // 404
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // --- EXCEPCIONES DE CATEGORIA ---

    @ExceptionHandler(CategoriaAlreadyExistsException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleCategoriaAlreadyExistsException(CategoriaAlreadyExistsException ex, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value()) // 400
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CategoriaInUseException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleCategoriaInUseException(CategoriaInUseException ex, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value()) // 409 CONFLICT
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DuplicateTurnException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleDuplicateTurnException(
            DuplicateTurnException ex,
            HttpServletRequest request) {

        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value()) // 409
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TurnFinishedException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleTurnFinishedException(
            TurnFinishedException ex,
            HttpServletRequest request) {

        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value()) // 400
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserHasAssociatedTurnosException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleUserHasAssociatedTurnosException(
            UserHasAssociatedTurnosException ex,
            HttpServletRequest request) {

        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserWithoutChangesException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleUserWithoutChangesException(
            UserWithoutChangesException ex,
            HttpServletRequest request) {

        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserEmailAlreadyInUseException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleUserEmailAlreadyInUseException(
            UserEmailAlreadyInUseException ex,
            HttpServletRequest request) {

        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler (CategoriaNoEncontradaException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleCategoriaNoEncontradaException( CategoriaNoEncontradaException ex,HttpServletRequest request)
    {

        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TurnoOutsideBusinessException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleTurnoOutsideBusinessHoursException(TurnoOutsideBusinessException ex, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value()) // 400
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServicioNotFoundException.class)
    public ResponseEntity<@NonNull ErrorResponse> handlerServicioNotFoundException(
            ServicioNotFoundException ex,
            HttpServletRequest request)
    {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(HorarioFueraDeAtencionException.class)
    public ResponseEntity<@NonNull ErrorResponse> handlerHorarioFueraDeAtencionException(
            HorarioFueraDeAtencionException ex,
            HttpServletRequest request)
    {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(HorarioInvalidoException.class)
    public ResponseEntity<@NonNull ErrorResponse> handlerHorarioInvalidoException(
            HorarioInvalidoException ex,
            HttpServletRequest request)
    {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value()) // 400
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(HorarioSuperpuestoException.class)
    public ResponseEntity<@NonNull ErrorResponse> handlerHorarioSuperpuestoException(
            HorarioSuperpuestoException ex,
            HttpServletRequest request)
    {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value()) // 409
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(HorarioFueraDeComercioException.class)
    public ResponseEntity<@NonNull ErrorResponse> handlerHorarioFueraDeComercioException(
            HorarioFueraDeComercioException ex,
            HttpServletRequest request)
    {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value()) // 400
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HorarioSinReferenciaException.class)
    public ResponseEntity<@NonNull ErrorResponse> handlerHorarioSinReferenciaException(
            HorarioSinReferenciaException ex,
            HttpServletRequest request)
    {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value()) // 400
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


}
