package com.example.ProyectoFinalTurnos.features.turno;

import com.example.ProyectoFinalTurnos.features.turno.Dtos.TurnoResponceDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/turnos")
public class TurnoWebController {

    private final TurnoService turnoService;

    public TurnoWebController(TurnoService turnoService) {
        this.turnoService = turnoService;
    }

    @GetMapping("/detalles/{id}")
    public String verDetalleTurno(@PathVariable Long id, Model model) {
        TurnoResponceDto turno = turnoService.obtenerPorId(id);

        model.addAttribute("turno", turno);

        return "detalle-turno";
    }
}