package com.example.ProyectoFinalTurnos.features.turno;


import com.example.ProyectoFinalTurnos.features.turno.Dtos.TurnoRequestDto;
import com.example.ProyectoFinalTurnos.features.turno.Dtos.TurnoResponceDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/turnos")
public class TurnoController
{
    private final TurnoService turnoService;

    public TurnoController(TurnoService turnoService)
    {
        this.turnoService=turnoService;
    }

    @PostMapping
    public ResponseEntity<TurnoResponceDto> crearTurno(
            @Valid @RequestBody TurnoRequestDto dto)
    {
        TurnoResponceDto turno = turnoService.crearTurno(dto);


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(turno);
    }

    @GetMapping
    public ResponseEntity<List<TurnoResponceDto>> obtenerTodosTurnos()
    {
        List<TurnoResponceDto> turnos = turnoService.obtenerTodos();

        if(turnos.isEmpty())
        {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(turnos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TurnoResponceDto> obtenerPorId(
            @PathVariable Long id)
    {
        TurnoResponceDto turno = turnoService.obtenerPorId(id);

        return ResponseEntity.ok(turno);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TurnoResponceDto> actualizarTurno(
            @PathVariable Long id,
            @Valid @RequestBody TurnoRequestDto dto)
    {
        TurnoResponceDto turnoActualizado =
                turnoService.actualizarTurno(id, dto);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(turnoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTurno(
            @PathVariable Long id)
    {
        turnoService.eliminarUnTurno(id);

        return ResponseEntity.noContent().build();
    }
}
