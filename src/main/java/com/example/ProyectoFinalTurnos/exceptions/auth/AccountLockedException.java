package com.example.ProyectoFinalTurnos.exceptions.auth;

public class AccountLockedException extends RuntimeException {
    public AccountLockedException() {
        super("La Cuenta esta bloqueada");
    }
}
