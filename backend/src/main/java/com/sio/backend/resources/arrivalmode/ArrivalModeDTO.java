package com.sio.backend.resources.arrivalmode;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) in formato record per il trasferimento
 * dei dati relativi alle modalità di arrivo in Pronto Soccorso nel sistema SIO.
 */
@Schema(description = "DTO per il trasferimento dei dati riguardanti la Modalità di Arrivo")
public record ArrivalModeDTO(
        @Schema(
                description = "Codice univoco della modalità di arrivo",
                example = "AMB",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 10
        )
        @NotBlank(message = "Il codice è obbligatorio")
        @Size(max = 10, message = "Il codice non può superare i 10 caratteri")
        String code,

        @Schema(
                description = "Descrizione della modalità di arrivo",
                example = "Ambulanza 118",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 50
        )
        @NotBlank(message = "La descrizione è obbligatoria")
        @Size(max = 50, message = "La descrizione non può superare i 50 caratteri")
        String description
) {}