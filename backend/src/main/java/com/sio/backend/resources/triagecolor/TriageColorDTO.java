package com.sio.backend.resources.triagecolor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) in formato record per il trasferimento
 * dei dati relativi ai colori e codici di priorità del Triage nel sistema SIO.
 */
@Schema(description = "DTO per il trasferimento dei dati riguardanti il Colore Triage")
public record TriageColorDTO(
        @Schema(
                description = "Codice univoco del colore Triage",
                example = "ROSSO",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 20
        )
        @NotBlank(message = "Il codice è obbligatorio")
        @Size(max = 20, message = "Il codice non può superare i 20 caratteri")
        String code,

        @Schema(
                description = "Nome visualizzato del livello di priorità",
                example = "Emergenza",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 50
        )
        @NotBlank(message = "Il nome visualizzato è obbligatorio")
        @Size(max = 50, message = "Il nome visualizzato non può superare i 50 caratteri")
        String displayName,

        @Schema(
                description = "Livello numerico di priorità (1 = massima urgenza, 5 = non urgente)",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "La priorità è obbligatoria")
        @Min(value = 1, message = "La priorità minima è 1")
        @Max(value = 5, message = "La priorità massima è 5")
        Integer priority,

        @Schema(
                description = "Valore esadecimale del colore",
                example = "#FF4E4A",
                requiredMode = Schema.RequiredMode.REQUIRED,
                pattern = "^#([A-Fa-f0-9]{6})$"
        )
        @NotBlank(message = "Il valore esadecimale è obbligatorio")
        @Pattern(regexp = "^#([A-Fa-f0-9]{6})$", message = "Il valore esadecimale deve rispettare il formato #RRGGBB")
        String hexValue
) {}