package com.sio.backend.resources.pathology;

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
 * Entità JPA che rappresenta una patologia clinica all'interno del sistema SIO.
 * Mappata sulla tabella relazionale "pathologies" del database.
 */
@Getter
@Setter
@Entity
@Table(name = "pathologies") // schema="sio"
@Schema(description = "Entità di persistenza per la gestione delle patologie nel sistema SIO")
public class Pathologies {

    /**
     * Codice identificativo univoco della patologia (Chiave Primaria).
     */
    @Id
    @Size(max = 10)
    @NotBlank
    @Column(name = "code", nullable = false, length = 10)
    @Schema(description = "Codice identificativo univoco della patologia", example = "PAT-01", maxLength = 10)
    private String code;

    /**
     * Descrizione testuale estesa della patologia.
     */
    @Size(max = 100)
    @NotBlank
    @Column(name = "description", nullable = false, length = 100)
    @Schema(description = "Descrizione dettagliata della patologia", example = "Ipertensione arteriosa", maxLength = 100)
    private String description;

    /**
     * Timestamp di creazione del record, generato automaticamente dal database.
     */
    @CreationTimestamp
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    @Schema(description = "Data e ora di registrazione della patologia")
    private Instant createdAt;

}