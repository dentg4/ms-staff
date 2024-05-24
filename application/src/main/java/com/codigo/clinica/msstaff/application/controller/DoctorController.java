package com.codigo.clinica.msstaff.application.controller;

import com.codigo.clinica.msstaff.domain.aggregates.dto.DoctorDto;
import com.codigo.clinica.msstaff.domain.aggregates.request.DoctorRequest;
import com.codigo.clinica.msstaff.domain.ports.in.DoctorServiceIn;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "API Rest de mantenimiento de Doctor.",
        description = "Incluye EndPoints para realizar el mantenimiento de un doctor."
)
@RestController
@RequestMapping("/api/v1/ms-staff/doctor")
@AllArgsConstructor
public class DoctorController {

    private final DoctorServiceIn doctorServiceIn;

    @Operation(summary = "Crear un Doctor.",
            description = "Para usar este EndPoint, debes enviar un objeto Doctor que será guardado en base de datos, previa validacion.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Doctor creado con éxito.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorDto.class))}),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = { @Content(schema = @Schema()) })
    })
    @PostMapping("/create")
    public ResponseEntity<DoctorDto> create(@RequestBody DoctorRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(doctorServiceIn.createIn(request));
    }

    @Operation(summary = "Buscar todos los registros de Doctores.",
            description = "EndPoint que lista todos los registros de Doctores de la base de datos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Doctores encontradas con éxito.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorDto.class))}),
            @ApiResponse(responseCode = "404", description = "Doctores no encontradas.", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("/all")
    public ResponseEntity<List<DoctorDto>> getAll(){
        return ResponseEntity.ok(doctorServiceIn.getAllIn());
    }

    @Operation(summary = "Buscar un Doctor por su Id.",
            description = "Para usar este EndPoint, debes enviar el Id de la Doctor a través de un PathVariable.",
            parameters = {
                    @Parameter(name = "id", description = "Id de búsqueda.", required = true, example = "1")
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Doctor encontrado con éxito.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorDto.class))}),
            @ApiResponse(responseCode = "404", description = "Doctor no encontrado.", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("/find/{id}")
    public ResponseEntity<DoctorDto> getPatientById(@PathVariable Long id){
        return ResponseEntity.ok(doctorServiceIn.findByIdIn(id).orElseThrow());
    }

    @Operation(summary = "Actualizar un Doctor.",
            description = "Para usar este EndPoint, debes enviar un objeto Doctor (sus cambios) que será guardado en base de datos, previa validacion.",
            parameters = {
                    @Parameter(name = "id", description = "Id de Doctor.", required = true, example = "1"),
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Doctor actualizado con éxito.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorDto.class))}),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = { @Content(schema = @Schema()) })
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<DoctorDto> updatePatient(@PathVariable Long id,
                                                   @RequestBody DoctorRequest request){
        return ResponseEntity.ok(doctorServiceIn.updateIn(id, request));
    }

    @Operation(summary = "Eliminar un Doctor por su Id.",
            description = "Para usar este EndPoint, enviar el Id de la Doctor a través de un PathVariable.",
            parameters = {
                    @Parameter(name = "id", description = "Id para eliminación.", required = true, example = "1")
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Doctor eliminado con éxito.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorDto.class))}),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = { @Content(schema = @Schema()) })
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<DoctorDto> deletePatient(@PathVariable Long id){
        return ResponseEntity.ok(doctorServiceIn.deleteIn(id));
    }
}
