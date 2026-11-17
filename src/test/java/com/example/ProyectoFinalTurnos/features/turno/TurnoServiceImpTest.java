package com.example.ProyectoFinalTurnos.features.turno;

import com.example.ProyectoFinalTurnos.exceptions.horarioAtencion.HorarioFueraDeAtencionException;
import com.example.ProyectoFinalTurnos.exceptions.turno.DuplicateTurnException;
import com.example.ProyectoFinalTurnos.exceptions.turno.TurnFinishedException;
import com.example.ProyectoFinalTurnos.exceptions.turno.TurnoAlreadyCancelledException;
import com.example.ProyectoFinalTurnos.exceptions.turno.TurnoPastDateException;
import com.example.ProyectoFinalTurnos.features.comercio.ComercioEntity;
import com.example.ProyectoFinalTurnos.features.horarioAtencion.HorarioAtencionEntity;
import com.example.ProyectoFinalTurnos.features.notificacion.TurnoCreadoEvent;
import com.example.ProyectoFinalTurnos.features.servicio.ServicioEntity;
import com.example.ProyectoFinalTurnos.features.servicio.ServicioRepository;
import com.example.ProyectoFinalTurnos.features.turno.Dtos.TurnoRequestDto;
import com.example.ProyectoFinalTurnos.features.turno.Dtos.TurnoResponceDto;
import com.example.ProyectoFinalTurnos.features.turno.Mapper.TurnoMapper;
import com.example.ProyectoFinalTurnos.features.usuario.UsuarioEntity;
import com.example.ProyectoFinalTurnos.features.usuario.UsuarioRepository;
import com.example.ProyectoFinalTurnos.model.enums.Diasemana;
import com.example.ProyectoFinalTurnos.model.enums.EstadoTurno;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios de las reglas de negocio de los turnos (SCRUM-15).
 * No levantan Spring ni la base: los repositorios están simulados con Mockito.
 */
@ExtendWith(MockitoExtension.class)
class TurnoServiceImpTest {

    private static final String EMAIL = "cliente@test.com";
    private static final LocalDate FECHA = LocalDate.now().plusDays(7);
    private static final LocalTime APERTURA = LocalTime.of(9, 0);
    private static final LocalTime CIERRE = LocalTime.of(18, 0);
    private static final int DURACION_MINUTOS = 30;

    @Mock private TurnoRepository turnoRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private ServicioRepository servicioRepository;
    @Mock private TurnoMapper turnoMapper;
    @Mock private ApplicationEventPublisher eventPublisher;

    @InjectMocks private TurnoServiceImp turnoService;

    private UsuarioEntity usuario;
    private ServicioEntity servicio;
    private ComercioEntity comercio;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(EMAIL, null, List.of()));

        usuario = new UsuarioEntity();
        usuario.setIdUsuario(1L);
        usuario.setEmail(EMAIL);
        usuario.setNombre("Juan");

        HorarioAtencionEntity horario = new HorarioAtencionEntity();
        horario.setDiaSemana(diaSemana(FECHA.getDayOfWeek()));
        horario.setHoraApertura(APERTURA);
        horario.setHoraCierre(CIERRE);

        comercio = new ComercioEntity();
        comercio.setIdComercio(10L);
        comercio.setNombre("Barbería Centro");
        comercio.setHorarios(List.of(horario));

        servicio = new ServicioEntity();
        servicio.setIdServicio(100L);
        servicio.setNombre("Corte de pelo");
        servicio.setDuracionDelServicio(DURACION_MINUTOS);
        servicio.setComercio(comercio);
    }

    @AfterEach
    void limpiarSeguridad() {
        SecurityContextHolder.clearContext();
    }

    @Nested
    @DisplayName("crearTurno")
    class CrearTurno {

        @Test
        @DisplayName("guarda el turno PENDIENTE, calcula la hora de fin y avisa por mail")
        void creaTurnoValido() {
            prepararUsuarioYServicio();
            when(turnoMapper.toEntity(any())).thenAnswer(inv -> {
                TurnoRequestDto dto = inv.getArgument(0);
                TurnoEntity t = new TurnoEntity();
                t.setFecha(dto.getFecha());
                t.setHoraInicio(dto.getHoraInicio());
                return t;
            });
            when(turnoRepository.save(any())).thenAnswer(inv -> {
                TurnoEntity t = inv.getArgument(0);
                t.setIdTurno(55L);
                return t;
            });
            when(turnoMapper.toDto(any())).thenReturn(new TurnoResponceDto());

            turnoService.crearTurno(pedido(LocalTime.of(10, 0)));

            ArgumentCaptor<TurnoEntity> guardado = ArgumentCaptor.forClass(TurnoEntity.class);
            verify(turnoRepository).save(guardado.capture());
            assertThat(guardado.getValue().getEstado()).isEqualTo(EstadoTurno.PENDIENTE);
            assertThat(guardado.getValue().getHoraFin()).isEqualTo(LocalTime.of(10, 30));
            assertThat(guardado.getValue().getUsuario()).isSameAs(usuario);
            assertThat(guardado.getValue().getComercio()).isSameAs(comercio);

            ArgumentCaptor<TurnoCreadoEvent> evento = ArgumentCaptor.forClass(TurnoCreadoEvent.class);
            verify(eventPublisher).publishEvent(evento.capture());
            assertThat(evento.getValue().turnoId()).isEqualTo(55L);
            assertThat(evento.getValue().destinatario()).isEqualTo(EMAIL);
        }

        @Test
        @DisplayName("rechaza una fecha pasada")
        void rechazaFechaPasada() {
            TurnoRequestDto dto = pedido(LocalTime.of(10, 0));
            dto.setFecha(LocalDate.now().minusDays(1));

            assertThatThrownBy(() -> turnoService.crearTurno(dto))
                    .isInstanceOf(TurnoPastDateException.class);
            verify(turnoRepository, never()).save(any());
        }

        @Test
        @DisplayName("rechaza un turno antes de que abra el comercio")
        void rechazaAntesDeApertura() {
            prepararUsuarioYServicio();

            assertThatThrownBy(() -> turnoService.crearTurno(pedido(LocalTime.of(8, 0))))
                    .isInstanceOf(HorarioFueraDeAtencionException.class);
            verify(turnoRepository, never()).save(any());
        }

        @Test
        @DisplayName("rechaza un turno que termina después del cierre")
        void rechazaDespuesDelCierre() {
            prepararUsuarioYServicio();

            assertThatThrownBy(() -> turnoService.crearTurno(pedido(LocalTime.of(17, 45))))
                    .isInstanceOf(HorarioFueraDeAtencionException.class);
        }

        @Test
        @DisplayName("rechaza un día en que el comercio no atiende")
        void rechazaDiaSinAtencion() {
            prepararUsuarioYServicio();
            comercio.setHorarios(List.of());

            assertThatThrownBy(() -> turnoService.crearTurno(pedido(LocalTime.of(10, 0))))
                    .isInstanceOf(HorarioFueraDeAtencionException.class);
        }

        @Test
        @DisplayName("rechaza un horario que se superpone con otro turno del servicio")
        void rechazaSuperposicionDelServicio() {
            prepararUsuarioYServicio();
            when(turnoRepository.findByServicio_IdServicioAndFecha(100L, FECHA))
                    .thenReturn(List.of(turnoExistente(LocalTime.of(10, 15), EstadoTurno.PENDIENTE)));

            assertThatThrownBy(() -> turnoService.crearTurno(pedido(LocalTime.of(10, 0))))
                    .isInstanceOf(DuplicateTurnException.class)
                    .hasMessageContaining("ocupado");
        }

        @Test
        @DisplayName("rechaza si el usuario ya tiene otro turno a esa hora")
        void rechazaSuperposicionDelUsuario() {
            prepararUsuarioYServicio();
            when(turnoRepository.findByUsuario_IdUsuarioAndFecha(1L, FECHA))
                    .thenReturn(List.of(turnoExistente(LocalTime.of(10, 0), EstadoTurno.CONFIRMADO)));

            assertThatThrownBy(() -> turnoService.crearTurno(pedido(LocalTime.of(10, 0))))
                    .isInstanceOf(DuplicateTurnException.class)
                    .hasMessageContaining("usuario");
        }

        @Test
        @DisplayName("un turno cancelado no bloquea el horario")
        void turnoCanceladoNoBloquea() {
            prepararUsuarioYServicio();
            when(turnoRepository.findByServicio_IdServicioAndFecha(100L, FECHA))
                    .thenReturn(List.of(turnoExistente(LocalTime.of(10, 0), EstadoTurno.CANCELADO)));
            when(turnoMapper.toEntity(any())).thenReturn(new TurnoEntity());
            when(turnoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
            when(turnoMapper.toDto(any())).thenReturn(new TurnoResponceDto());

            turnoService.crearTurno(pedido(LocalTime.of(10, 0)));

            verify(turnoRepository).save(any());
        }
    }

    @Test
    @DisplayName("actualizarTurno: no se puede reprogramar un turno cancelado")
    void noReprogramaCancelado() {
        when(turnoRepository.findById(5L))
                .thenReturn(Optional.of(turnoExistente(LocalTime.of(10, 0), EstadoTurno.CANCELADO)));

        assertThatThrownBy(() -> turnoService.actualizarTurno(5L, pedido(LocalTime.of(11, 0))))
                .isInstanceOf(TurnoAlreadyCancelledException.class);
        verify(turnoRepository, never()).save(any());
    }

    @Test
    @DisplayName("actualizarTurno: no se puede reprogramar un turno finalizado")
    void noReprogramaFinalizado() {
        when(turnoRepository.findById(5L))
                .thenReturn(Optional.of(turnoExistente(LocalTime.of(10, 0), EstadoTurno.FINALIZADO)));

        assertThatThrownBy(() -> turnoService.actualizarTurno(5L, pedido(LocalTime.of(11, 0))))
                .isInstanceOf(TurnFinishedException.class);
    }

    @Test
    @DisplayName("eliminarUnTurno: no se puede eliminar un turno finalizado")
    void noEliminaFinalizado() {
        when(turnoRepository.findById(5L))
                .thenReturn(Optional.of(turnoExistente(LocalTime.of(10, 0), EstadoTurno.FINALIZADO)));

        assertThatThrownBy(() -> turnoService.eliminarUnTurno(5L))
                .isInstanceOf(TurnFinishedException.class);
        verify(turnoRepository, never()).delete(any());
    }

    private void prepararUsuarioYServicio() {
        when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuario));
        when(servicioRepository.findById(100L)).thenReturn(Optional.of(servicio));
    }

    private TurnoRequestDto pedido(LocalTime hora) {
        TurnoRequestDto dto = new TurnoRequestDto();
        dto.setFecha(FECHA);
        dto.setHoraInicio(hora);
        dto.setServicioId(100L);
        return dto;
    }

    private TurnoEntity turnoExistente(LocalTime inicio, EstadoTurno estado) {
        TurnoEntity t = new TurnoEntity();
        t.setIdTurno(99L);
        t.setFecha(FECHA);
        t.setHoraInicio(inicio);
        t.setHoraFin(inicio.plusMinutes(DURACION_MINUTOS));
        t.setEstado(estado);
        t.setServicio(servicio);
        t.setUsuario(usuario);
        return t;
    }

    private static Diasemana diaSemana(DayOfWeek dia) {
        return switch (dia) {
            case MONDAY -> Diasemana.LUNES;
            case TUESDAY -> Diasemana.MARTES;
            case WEDNESDAY -> Diasemana.MIERCOLES;
            case THURSDAY -> Diasemana.JUEVES;
            case FRIDAY -> Diasemana.VIERNES;
            case SATURDAY -> Diasemana.SABADO;
            case SUNDAY -> Diasemana.DOMINGO;
        };
    }
}
