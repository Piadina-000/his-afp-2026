package com.sio.backend.resources.arrivalmode;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service che gestisce la logica di business per l'entità {@link ArrivalMode} nel sistema SIO.
 * Gestisce transazionalità, operazioni CRUD e log di auditing delle modalità di arrivo.
 */
@Service
@Slf4j
@Transactional
public class ArrivalModeService {
    private final ArrivalModeRepository repo;
    private final ArrivalModeMapper mapper;

    /**
     * Inizializza il service iniettando il repository e il mapper delle modalità di arrivo.
     *
     * @param repo repository per l'accesso ai dati delle modalità di arrivo
     * @param mapper componente di conversione tra Entity e DTO
     */
    public ArrivalModeService(ArrivalModeRepository repo, ArrivalModeMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    /**
     * Recupera l'elenco completo di tutte le modalità di arrivo registrate.
     *
     * @return una lista contenente gli {@link ArrivalModeDTO} di tutte le modalità di arrivo
     */
    @Transactional(readOnly = true)
    public List<ArrivalModeDTO> getAllArrivalModes() {
        log.debug("Recupero di tutte le modalità di arrivo");
        List<ArrivalMode> list = repo.findAll();
        List<ArrivalModeDTO> dtos = new ArrayList<>();

        for (ArrivalMode e : list) {
            dtos.add(mapper.toDTO(e));
        }

        return dtos;
    }

    /**
     * Recupera un elenco paginato delle modalità di arrivo.
     *
     * @param pageable oggetto contenente i parametri di paginazione e ordinamento
     * @return una pagina di {@link ArrivalModeDTO}
     */
    @Transactional(readOnly = true)
    public Page<ArrivalModeDTO> getAllArrivalModesPaginated(Pageable pageable) {
        Page<ArrivalMode> entityPage = repo.findAll(pageable);

        return entityPage.map(mapper::toDTO);
    }

    /**
     * Recupera una singola modalità di arrivo tramite il suo codice identificativo.
     *
     * @param code il codice identificativo della modalità di arrivo
     * @return l'{@link ArrivalModeDTO} corrispondente
     * @throws EntityNotFoundException se la modalità di arrivo non viene trovata
     */
    @Transactional(readOnly = true)
    public ArrivalModeDTO getArrivalModeById(String code) {
        ArrivalMode e = repo.findById(code)
                .orElseThrow(() -> new EntityNotFoundException("Modalità di arrivo con codice: " + code + " non trovata"));

        return mapper.toDTO(e);
    }

    /**
     * Crea una nuova modalità di arrivo nel database.
     * Verifica preventivamente l'esistenza di un codice duplicato.
     *
     * @param dto il DTO contenente i dati della modalità di arrivo da creare
     * @return l'{@link ArrivalModeDTO} della modalità di arrivo creata
     * @throws IllegalArgumentException se il codice della modalità di arrivo esiste già
     */
    public ArrivalModeDTO createNewArrivalMode(ArrivalModeDTO dto) {
        log.info("Creazione nuova entità arrivalMode con codice: {}", dto.code());
        log.debug("Entità in creazione con codice: {}, e descrizione {}", dto.code(), dto.description());

        if (repo.existsById(dto.code())) {
            log.warn("Tentativo di creazione di una modalità di arrivo con codice già esistente: {}", dto.code());
            throw new IllegalArgumentException("Modalità di arrivo con codice: " + dto.code() + " già esistente");
        }

        ArrivalMode entity = mapper.toEntity(dto);
        entity = repo.save(entity);

        log.info("Entità arrivalMode con codice: {} creata con successo", entity.getCode());
        return mapper.toDTO(entity);
    }

    /**
     * Elimina una modalità di arrivo in base al codice identificativo.
     *
     * @param code il codice della modalità di arrivo da eliminare
     * @throws EntityNotFoundException se la modalità di arrivo non viene trovata nel database
     */
    public void deleteArrivalMode(String code) {
        if (!repo.existsById(code)) {
            throw new EntityNotFoundException("Modalità di arrivo con codice: " + code + " non trovata");
        }
        repo.deleteById(code);
    }

    /**
     * Modifica la descrizione di una modalità di arrivo esistente.
     *
     * @param code il codice identificativo della modalità di arrivo da aggiornare
     * @param dto il DTO contenente i dati aggiornati
     * @return l'{@link ArrivalModeDTO} aggiornato
     * @throws EntityNotFoundException se la modalità di arrivo non viene trovata nel database
     */
    public ArrivalModeDTO editArrivalMode(String code, ArrivalModeDTO dto) {
        ArrivalMode e = repo.findById(code)
                .orElseThrow(() -> new EntityNotFoundException("Modalità di arrivo con codice: " + code + " non trovata"));

        e.setDescription(dto.description());

        ArrivalMode savedEntity = repo.save(e);

        return mapper.toDTO(savedEntity);
    }
}