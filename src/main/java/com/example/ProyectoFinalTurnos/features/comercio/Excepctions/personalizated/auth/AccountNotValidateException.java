package com.example.ProyectoFinalTurnos.features.comercio.Excepctions.personalizated.auth;

public class AccountNotValidateException extends RuntimeException {
    public AccountNotValidateException(String message) {
        super(message);
    }
}
