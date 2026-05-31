package com.example.ProyectoFinalTurnos.features.comercio.Excepctions.personalizated.auth;

public class AccountLockedException extends RuntimeException {
    public AccountLockedException() {
        super("La Cuenta esta bloqueada");
    }
}
