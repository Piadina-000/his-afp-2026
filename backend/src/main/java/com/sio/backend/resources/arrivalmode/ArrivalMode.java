package com.sio.backend.resources.arrivalmode;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

/**
 * Entità JPA che rappresenta una modalità di arrivo in Pronto Soccorso all'interno del sistema SIO.
 * Mappata sulla tabella relazionale "arrival_modes" del database.
 */
@Getter
@Setter
@Entity
@Table(name = "arrival_modes") // schema="sio"
@Schema(description = "Entità di persistenza per la gestione delle modalità di arrivo nel sistema SIO")
public class ArrivalMode {

    /**
     * Codice identificativo univoco della modalità di arrivo (Chiave Primaria).
     */
    @Id
    @Size(max = 10)
    @NotBlank
    @Column(name = "code", nullable = false, length = 10)
    @Schema(description = "Codice identificativo univoco della modalità di arrivo", example = "AMB", maxLength = 10)
    private String code;

    /**
     * Descrizione testuale della modalità di arrivo.
     */
    @Size(max = 50)
    @NotBlank
    @Column(name = "description", nullable = false, length = 50)
    @Schema(description = "Descrizione dettagliata della modalità di arrivo", example = "Ambulanza 118", maxLength = 50)
    private String description;

    /**
     * Timestamp di creazione del record, generato automaticamente dal database.
     */
    @CreationTimestamp
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    @Schema(description = "Data e ora di registrazione della modalità di arrivo")
    private Instant createdAt;
}