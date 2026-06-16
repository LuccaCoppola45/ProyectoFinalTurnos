#  Sistema de Gestión de Turnos - API RESTful

**Proyecto Final - Programación III** **Institución:** UTN - Facultad Regional Mar del Plata (2026)  
**Cátedra:** Prof. Eduardo Mango  

---

##  Presentación del Proyecto
Este proyecto consiste en el desarrollo de una **API RESTful** construida con Java y el framework **Spring Boot**. El sistema provee una plataforma centralizada para la gestión, administración y reserva de turnos, conectando a clientes con diferentes comercios y prestadores de servicios. 

La arquitectura del proyecto sigue los estándares de diseño requeridos por la cátedra, implementando capas bien definidas (Controladores, Servicios, Repositorios, DTOs, Mappers), inyección de dependencias, manejo global de excepciones y seguridad mediante autenticación JWT.

---

##  Problemáticas que Resuelve
En la actualidad, muchos comercios y profesionales independientes sufren de ineficiencias en la gestión de sus agendas. Este sistema aborda de forma directa las siguientes problemáticas:
1. **Desorganización y Superposición de Turnos:** Elimina la posibilidad de doble reserva para un mismo servicio/profesional en el mismo bloque horario.
2. **Falta de Accesibilidad 24/7:** Permite a los clientes autogestionar la reserva de sus turnos en cualquier momento sin depender de la atención humana (llamadas o mensajes).
3. **Pérdida de Información:** Centraliza en una base de datos relacional (MySQL) todo el historial de turnos, clientes y cancelaciones, evitando el uso de agendas de papel o planillas descentralizadas.
4. **Falta de Trazabilidad en Cancelaciones:** Establece un mecanismo formal para registrar quién y por qué se canceló un turno, mejorando las métricas del negocio.

---

##  Reglas de Negocio Abordadas
El núcleo de la aplicación (Domain-Driven) valida estrictamente las siguientes reglas:

* **Control de Disponibilidad Horaria:** No se puede emitir un turno si el horario solicitado cae fuera del `Horario de Atención` configurado por el comercio.
* **Control de Concurrencia:** El sistema verifica que el horario y fecha solicitados para un servicio no estén previamente ocupados o confirmados por otro cliente.
* **Gestión de Estados del Turno:** Los turnos poseen un ciclo de vida estricto a través de un Enum (`PENDIENTE`, `CONFIRMADO`, `CANCELADO`, `FINALIZADO`). Un turno cancelado o finalizado no puede volver a modificarse.
* **Jerarquía de Catálogo:** Un `Turno` depende de un `Servicio` específico, y a su vez, ese servicio pertenece a un `Comercio`. Un comercio puede estar etiquetado bajo múltiples `Categorías` (Ej: Peluquería, Estética).
* **Seguridad y Accesos (Roles):** * `CLIENTE`: Solo puede reservar turnos y ver su propio historial.
  * `COMERCIO`: Administra su catálogo de servicios, horarios de atención y aprueba/cancela los turnos de su local.
  * `ADMIN`: Tiene acceso global para gestionar categorías y moderar la plataforma.
* **Auditoría de Cancelaciones:** Todo turno que pase a estado `CANCELADO` debe generar una entidad de `Cancelacion` asociada con el motivo y la fecha exacta de la baja.

---

##  Tecnologías Utilizadas
* **Lenguaje:** Java 17+
* **Framework Core:** Spring Boot 3.x
* **Capa Web:** Spring Web (MVC, REST)
* **Persistencia:** Spring Data JPA / Hibernate
* **Base de Datos:** MySQL
* **Seguridad:** Spring Security + JWT (JSON Web Tokens)
* **Validaciones:** Spring Validation API (`@Valid`, `@NotBlank`, `@Positive`, etc.)
* **Documentación:** Swagger / OpenAPI 3.0
* **Mapeo de Objetos:** ModelMapper / MapStruct
* **Manejo de Dependencias:** Maven
* **Control de Versiones:** Git / GitHub

---

##  Pasos Requeridos para la Ejecución

### 1. Prerrequisitos
Asegúrese de tener instalado en su entorno local:
* **Java Development Kit (JDK) 17** o superior.
* **MySQL Server** (funcionando en el puerto 3306).
* **Maven** (opcional, el proyecto incluye el wrapper `mvnw`).
* **Git**.

### 2. Clonar el Repositorio
```bash
git clone [https://github.com/LuccaCoppola45/ProyectoFinalTurnos.git](https://github.com/LuccaCoppola45/ProyectoFinalTurnos.git)
cd ProyectoFinalTurnos
