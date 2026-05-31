package com.example.ProyectoFinalTurnos.features.turno;


import com.example.ProyectoFinalTurnos.features.turno.Dtos.TurnoRequestDto;
import com.example.ProyectoFinalTurnos.features.turno.Dtos.TurnoResponceDto;
import jakarta.validation.Valid;
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
    public TurnoResponceDto crearTurno (@Valid @RequestBody TurnoRequestDto dto)
    {
        return turnoService.crearTurno(dto);
    }

    @GetMapping
    public List<TurnoResponceDto> obtenerTodosTurnos()
    {
        return turnoService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public TurnoResponceDto obtenerPorId(@PathVariable Long id)
    {
        return turnoService.obtenerPorId(id);
    }

    @DeleteMapping("/{id}")
    public void eliminarTurno(@PathVariable Long id)
    {
        turnoService.eliminarUnTurno(id);
    }
}
