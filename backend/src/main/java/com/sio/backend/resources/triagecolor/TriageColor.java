package com.sio.backend.resources.triagecolor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

/**
 * Entità JPA che rappresenta un codice colore di priorità del Triage all'interno del sistema SIO.
 * Mappata sulla tabella relazionale "triage_colors" del database.
 */
@Getter
@Setter
@Entity
@Table(name = "triage_colors") // schema="sio"
@Schema(description = "Entità di persistenza per la gestione dei colori e delle priorità del Triage nel sistema SIO")
public class TriageColor {

    /**
     * Codice identificativo univoco del colore Triage (Chiave Primaria).
     */
    @Id
    @Size(max = 20)
    @NotBlank
    @Column(name = "code", nullable = false, length = 20)
    @Schema(description = "Codice identificativo univoco del colore Triage", example = "ROSSO", maxLength = 20)
    private String code;

    /**
     * Nome visualizzato o etichetta clinica della priorità.
     */
    @Size(max = 50)
    @NotBlank
    @Column(name = "display_name", nullable = false, length = 50)
    @Schema(description = "Nome visualizzato del codice colore Triage", example = "Emergenza", maxLength = 50)
    private String displayName;

    /**
     * Livello numerico di priorità (1 = massima urgenza, 5 = non urgente).
     */
    @NotNull
    @Min(1)
    @Max(5)
    @Column(name = "priority", nullable = false)
    @Schema(description = "Livello numerico di priorità (da 1 a 5)", example = "1")
    private Integer priority;

    /**
     * Valore esadecimale del colore per la rappresentazione grafica.
     */
    @Size(max = 7)
    @NotBlank
    @Column(name = "hex_value", nullable = false, length = 7)
    @Schema(description = "Codice colore esadecimale", example = "#FF4E4A", maxLength = 7)
    private String hexValue;

    /**
     * Timestamp di creazione del record, generato automaticamente dal database.
     */
    @CreationTimestamp
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    @Schema(description = "Data e ora di registrazione del colore Triage")
    private Instant createdAt;
}