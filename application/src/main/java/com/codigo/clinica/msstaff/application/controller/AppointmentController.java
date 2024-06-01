package com.codigo.clinica.msstaff.application.controller;

import com.codigo.clinica.msstaff.domain.aggregates.dto.AppointmentDto;
import com.codigo.clinica.msstaff.domain.aggregates.request.AppointmentRequest;
import com.codigo.clinica.msstaff.domain.ports.in.AppointmentServiceIn;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@Tag(
        name = "API Rest de mantenimiento de Citas.",
        description = "Incluye EndPoints para realizar el mantenimiento de una cita."
)
@RestController
@RequestMapping("/api/v1/ms-staff/appointment")
@AllArgsConstructor
public class AppointmentController {

    private final AppointmentServiceIn appointmentServiceIn;

    @Operation(summary = "Crear una Cita.",
            description = "Para usar este EndPoint, debes enviar un objeto Cita que será guardado en base de datos, previa validacion.")
    @ApiResponse(responseCode = "200", description = "Cita creada con éxito.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AppointmentDto.class))})
    @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = { @Content(schema = @Schema()) })
    @PostMapping("/create")
    public ResponseEntity<AppointmentDto> create(@Valid @RequestBody AppointmentRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(appointmentServiceIn.createIn(request));
    }

    @Operation(summary = "Buscar todos los registros de Citas.",
            description = "EndPoint que lista todos los registros de Citas de la base de datos.")
    @ApiResponse(responseCode = "200", description = "Citas encontradas con éxito.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AppointmentDto.class))})
    @ApiResponse(responseCode = "404", description = "Citas no encontradas.", content = { @Content(schema = @Schema()) })
    @GetMapping("/all")
    public ResponseEntity<List<AppointmentDto>> getAll(){
        return ResponseEntity.ok(appointmentServiceIn.getAllIn());
    }

    @Operation(summary = "Buscar una Cita por su Id.",
            description = "Para usar este EndPoint, debes enviar el Id de la Cita a través de un PathVariable.",
            parameters = {
                    @Parameter(name = "id", description = "Id de búsqueda.", required = true, example = "1")
            })
    @ApiResponse(responseCode = "200", description = "Cita encontrado con éxito.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AppointmentDto.class))})
    @ApiResponse(responseCode = "404", description = "Cita no encontrado.", content = { @Content(schema = @Schema()) })
    @GetMapping("/find/{id}")
    public ResponseEntity<AppointmentDto> getPatientById(@PathVariable Long id){
        return appointmentServiceIn.findByIdIn(id).map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @Operation(summary = "Actualizar una Cita.",
            description = "Para usar este EndPoint, debes enviar un objeto Cita (sus cambios) que será guardado en base de datos, previa validacion.",
            parameters = {
                    @Parameter(name = "id", description = "Id de Cita.", required = true, example = "1"),
            })
    @ApiResponse(responseCode = "200", description = "Cita actualizada con éxito.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AppointmentDto.class))})
    @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = { @Content(schema = @Schema()) })
    @PutMapping("/update/{id}")
    public ResponseEntity<AppointmentDto> updatePatient(@PathVariable Long id,
                                                        @Valid @RequestBody AppointmentRequest request){
        return ResponseEntity.ok(appointmentServiceIn.updateIn(id, request));
    }

    @Operation(summary = "Eliminar una Cita por su Id.",
            description = "Para usar este EndPoint, enviar el Id de la Cita a través de un PathVariable.",
            parameters = {
                    @Parameter(name = "id", description = "Id para eliminación.", required = true, example = "1")
            })
    @ApiResponse(responseCode = "200", description = "Cita eliminada con éxito.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AppointmentDto.class))})
    @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = { @Content(schema = @Schema()) })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<AppointmentDto> deletePatient(@PathVariable Long id){
        return ResponseEntity.ok(appointmentServiceIn.deleteIn(id));
    }
}
