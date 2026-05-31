package com.example.ProyectoFinalTurnos.features.turno;


import com.example.ProyectoFinalTurnos.features.turno.Dtos.TurnoRequestDto;
import com.example.ProyectoFinalTurnos.features.turno.Dtos.TurnoResponceDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<TurnoResponceDto> crearTurno(@Valid @RequestBody TurnoRequestDto dto)
    {
        try {
            TurnoResponceDto response = turnoService.crearTurno(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400
        }
    }

    @GetMapping
    public ResponseEntity<List<TurnoResponceDto>> obtenerTodosTurnos()
    {
        try {
            List<TurnoResponceDto> turnos = turnoService.obtenerTodos();

            if (turnos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204
            }

            return ResponseEntity.ok(turnos); // 200
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TurnoResponceDto> obtenerPorId(@PathVariable Long id)
    {
        try {
            TurnoResponceDto turno = turnoService.obtenerPorId(id);
            return ResponseEntity.ok(turno); // 200
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TurnoResponceDto> actualizarTurno(@PathVariable Long id, @Valid @RequestBody TurnoRequestDto dto)
    {
        try {
            TurnoResponceDto response = turnoService.actualizarTurno(id, dto);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response); // 202
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTurno(@PathVariable Long id)
    {
        try {
            turnoService.eliminarUnTurno(id);
            return ResponseEntity.noContent().build(); // 204
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }
}
