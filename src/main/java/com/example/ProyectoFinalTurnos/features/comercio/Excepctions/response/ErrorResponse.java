package com.example.ProyectoFinalTurnos.features.comercio.Excepctions.response;

import lombok.*;
import java.time.LocalDateTime;
import java.util.Map;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private int status;
    private String error;
    private String message;
    private String path;
    private LocalDateTime timestamp;

    private Map<String, String> errors;
}
