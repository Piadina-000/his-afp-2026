package com.sio.backend.resources.triagecolor;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service che gestisce la logica di business per l'entità {@link TriageColor} nel sistema SIO.
 * Gestisce transazionalità, operazioni CRUD e log di auditing dei colori Triage.
 */
@Service
@Slf4j
@Transactional
public class TriageColorService {
    private final TriageColorRepository repo;
    private final TriageColorMapper mapper;

    /**
     * Inizializza il service iniettando il repository e il mapper dei colori Triage.
     *
     * @param repo repository per l'accesso ai dati dei colori Triage
     * @param mapper componente di conversione tra Entity e DTO
     */
    public TriageColorService(TriageColorRepository repo, TriageColorMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    /**
     * Recupera l'elenco completo di tutti i colori Triage registrati.
     *
     * @return una lista contenente i {@link TriageColorDTO} di tutti i colori Triage
     */
    @Transactional(readOnly = true)
    public List<TriageColorDTO> getAllTriageColors() {
        log.debug("Recupero di tutti i colori Triage");
        List<TriageColor> list = repo.findAll();
        List<TriageColorDTO> dtos = new ArrayList<>();

        for (TriageColor e : list) {
            dtos.add(mapper.toDTO(e));
        }

        return dtos;
    }

    /**
     * Recupera un elenco paginato dei colori Triage.
     *
     * @param pageable oggetto contenente i parametri di paginazione e ordinamento
     * @return una pagina di {@link TriageColorDTO}
     */
    @Transactional(readOnly = true)
    public Page<TriageColorDTO> getAllTriageColorsPaginated(Pageable pageable) {
        Page<TriageColor> entityPage = repo.findAll(pageable);

        return entityPage.map(mapper::toDTO);
    }

    /**
     * Recupera un singolo colore Triage tramite il suo codice identificativo.
     *
     * @param code il codice identificativo del colore Triage
     * @return il {@link TriageColorDTO} corrispondente
     * @throws EntityNotFoundException se il colore Triage non viene trovato
     */
    @Transactional(readOnly = true)
    public TriageColorDTO getTriageColorById(String code) {
        TriageColor e = repo.findById(code)
                .orElseThrow(() -> new EntityNotFoundException("Colore Triage con codice: " + code + " non trovato"));

        return mapper.toDTO(e);
    }

    /**
     * Crea un nuovo colore Triage nel database.
     * Verifica preventivamente l'esistenza di un codice duplicato.
     *
     * @param dto il DTO contenente i dati del colore Triage da creare
     * @return il {@link TriageColorDTO} del colore Triage creato
     * @throws IllegalArgumentException se il codice del colore Triage esiste già
     */
    public TriageColorDTO createNewTriageColor(TriageColorDTO dto) {
        log.info("Creazione nuova entità triageColor con codice: {}", dto.code());
        log.debug("Entità in creazione con codice: {}, displayName: {}, priority: {}", dto.code(), dto.displayName(), dto.priority());

        if (repo.existsById(dto.code())) {
            log.warn("Tentativo di creazione di un colore Triage con codice già esistente: {}", dto.code());
            throw new IllegalArgumentException("Colore Triage con codice: " + dto.code() + " già esistente");
        }

        TriageColor entity = mapper.toEntity(dto);
        entity = repo.save(entity);

        log.info("Entità triageColor con codice: {} creata con successo", entity.getCode());
        return mapper.toDTO(entity);
    }

    /**
     * Elimina un colore Triage in base al codice identificativo.
     *
     * @param code il codice del colore Triage da eliminare
     * @throws EntityNotFoundException se il colore Triage non viene trovato nel database
     */
    public void deleteTriageColor(String code) {
        if (!repo.existsById(code)) {
            throw new EntityNotFoundException("Colore Triage con codice: " + code + " non trovato");
        }
        repo.deleteById(code);
    }

    /**
     * Modifica i dati di un colore Triage esistente.
     *
     * @param code il codice identificativo del colore Triage da aggiornare
     * @param dto il DTO contenente i dati aggiornati
     * @return il {@link TriageColorDTO} aggiornato
     * @throws EntityNotFoundException se il colore Triage non viene trovato nel database
     */
    public TriageColorDTO editTriageColor(String code, TriageColorDTO dto) {
        TriageColor e = repo.findById(code)
                .orElseThrow(() -> new EntityNotFoundException("Colore Triage con codice: " + code + " non trovato"));

        e.setDisplayName(dto.displayName());
        e.setPriority(dto.priority());
        e.setHexValue(dto.hexValue());

        TriageColor savedEntity = repo.save(e);

        return mapper.toDTO(savedEntity);
    }
}