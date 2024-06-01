package com.codigo.clinica.msstaff.application.controller;

import com.codigo.clinica.msstaff.domain.aggregates.dto.ClinicDto;
import com.codigo.clinica.msstaff.domain.aggregates.request.ClinicRequest;
import com.codigo.clinica.msstaff.domain.ports.in.ClinicServiceIn;
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
        name = "API Rest de mantenimiento de Clínicas.",
        description = "Incluye EndPoints para realizar el mantenimiento de una clínica."
)
@RestController
@RequestMapping("/api/v1/ms-staff/clinic")
@AllArgsConstructor
public class ClinicController {

    private final ClinicServiceIn clinicServiceIn;

    @Operation(summary = "Crear una Clínica.",
            description = "Para usar este EndPoint, debes enviar un objeto Clínica que será guardado en base de datos, previa validacion.")
    @ApiResponse(responseCode = "200", description = "Clínica creada con éxito.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ClinicDto.class))})
    @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = { @Content(schema = @Schema()) })
    @PostMapping("/create")
    public ResponseEntity<ClinicDto> create(@Valid @RequestBody ClinicRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clinicServiceIn.createIn(request));
    }

    @Operation(summary = "Buscar todos los registros de Clínicas.",
            description = "EndPoint que lista todos los registros de Clínicas de la base de datos.")
    @ApiResponse(responseCode = "200", description = "Clínicas encontradas con éxito.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ClinicDto.class))})
    @ApiResponse(responseCode = "404", description = "Clínicas no encontradas.", content = { @Content(schema = @Schema()) })
    @GetMapping("/all")
    public ResponseEntity<List<ClinicDto>> getAll(){
        return ResponseEntity.ok(clinicServiceIn.getAllIn());
    }

    @Operation(summary = "Buscar una Clínica por su Id.",
            description = "Para usar este EndPoint, debes enviar el Id de la Clínica a través de un PathVariable.",
            parameters = {
                    @Parameter(name = "id", description = "Id de búsqueda.", required = true, example = "1")
            })
    @ApiResponse(responseCode = "200", description = "Clínica encontrado con éxito.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ClinicDto.class))})
    @ApiResponse(responseCode = "404", description = "Clínica no encontrado.", content = { @Content(schema = @Schema()) })
    @GetMapping("/find/{id}")
    public ResponseEntity<ClinicDto> getPatientById(@PathVariable Long id){
        return clinicServiceIn.findByIdIn(id).map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @Operation(summary = "Actualizar una Clínica.",
            description = "Para usar este EndPoint, debes enviar un objeto Clínica (sus cambios) que será guardado en base de datos, previa validacion.",
            parameters = {
                    @Parameter(name = "id", description = "Id de Clínica.", required = true, example = "1"),
            })
    @ApiResponse(responseCode = "200", description = "Clínica actualizada con éxito.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ClinicDto.class))})
    @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = { @Content(schema = @Schema()) })
    @PutMapping("/update/{id}")
    public ResponseEntity<ClinicDto> updatePatient(@PathVariable Long id,
                                                   @Valid @RequestBody ClinicRequest request){
        return ResponseEntity.ok(clinicServiceIn.updateIn(id, request));
    }

    @Operation(summary = "Eliminar una Clínica por su Id.",
            description = "Para usar este EndPoint, enviar el Id de la Clínica a través de un PathVariable.",
            parameters = {
                    @Parameter(name = "id", description = "Id para eliminación.", required = true, example = "1")
            })
    @ApiResponse(responseCode = "200", description = "Clínica eliminada con éxito.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ClinicDto.class))})
    @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = { @Content(schema = @Schema()) })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ClinicDto> deletePatient(@PathVariable Long id){
        return ResponseEntity.ok(clinicServiceIn.deleteIn(id));
    }
}
