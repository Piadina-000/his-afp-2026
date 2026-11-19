package com.sio.backend.resources;

import com.sio.backend.generics.ApiResponse;
import com.sio.backend.resources.arrivalmode.ArrivalModeDTO;
import com.sio.backend.resources.arrivalmode.ArrivalModeService;
import com.sio.backend.resources.pathology.PathologiesDTO;
import com.sio.backend.resources.pathology.PathologiesService;
import com.sio.backend.resources.triagecolor.TriageColorDTO;
import com.sio.backend.resources.triagecolor.TriageColorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springdoc.core.annotations.ParameterObject;


/**
 * Controller REST per la gestione delle risorse cliniche e di sistema del progetto SIO.
 * Espone gli endpoint per le operazioni CRUD su patologie, modalità di arrivo e colori Triage.
 */
@RestController
@RequestMapping("/resources")
@Tag(name="Gestione risorse SIO", description = "Endpoint per la gestione delle risorse SIO")
class ResourcesController {

    @Autowired
    private PathologiesService pathologiesService;

    @Autowired
    private ArrivalModeService arrivalModeService;

    @Autowired
    private TriageColorService triageColorService;

    // ==================== PATHOLOGIES ====================

    /**
     * Recupera un elenco paginato di tutte le patologie registrate.
     *
     * @param pageable parametri di paginazione e ordinamento
     * @return un'istanza di {@link ApiResponse} contenente la pagina di {@link PathologiesDTO}
     */
    @Operation(summary = "Recupera tutte le patologie", description = "Recupero di tutte le patologie presenti a DB con supporto alla paginazione")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Lista di patologie recuperata con successo"
            )
    })
    @GetMapping("/pathologies")
    public ApiResponse<Page<PathologiesDTO>> getAllPathologies(
//            @PageableDefault(size = 10, sort = "code", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return ApiResponse.success(
                pathologiesService.getAllPathologiesPaginated(pageable)
        );
    }

    /**
     * Recupera una singola patologia tramite il codice identificativo univoco.
     *
     * @param code il codice identificativo della patologia
     * @return un'istanza di {@link ApiResponse} contenente il {@link PathologiesDTO} corrispondente
     */
    @Operation(
            summary = "Recupera una patologia dato il suo codice",
            description = "Recupero di una patologia specifica tramite il relativo codice"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Patologia trovata"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Patologia non trovata")
    })
    @GetMapping("/pathologies/{code}")
    public ApiResponse<PathologiesDTO> getPathologiesByCode(
            @Parameter(description = "Codice identificativo della patologia", example = "C02")
            @Valid
            @PathVariable
            String code
    ){
        PathologiesDTO dto = pathologiesService.getPathologyById(code);

        return ApiResponse.success(dto);
    }

    /**
     * Crea una nuova patologia nel sistema.
     *
     * @param dto il DTO contenente i dati della patologia da creare
     * @return il {@link PathologiesDTO} della patologia appena creata
     */
    @Operation(summary = "Crea una nuova patologia", description = "Inserisce una nuova patologia nel database")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Patologia creata con successo"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dati non validi o codice già esistente")
    })
    @PostMapping("/pathologies")
    public PathologiesDTO createPathology(@Valid @RequestBody PathologiesDTO dto){
        return pathologiesService.createNewPathology(dto);
    }

    /**
     * Elimina una patologia esistente in base al codice.
     *
     * @param code il codice identificativo della patologia da rimuovere
     * @return un'istanza di {@link ApiResponse} vuota con esito positivo
     */
    @Operation(summary = "Elimina una patologia", description = "Rimuove una patologia dal database tramite il codice")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Patologia eliminata con successo")
    })
    @DeleteMapping("/pathologies")
    public ApiResponse<Void> deletePathology(
            @Parameter(description = "Codice identificativo della patologia da eliminare", example = "C02")
            @RequestParam String code
    ){
        pathologiesService.deletePathology(code);

        return ApiResponse.success(null);
    }

    /**
     * Modifica i dati di una patologia esistente.
     *
     * @param dto il DTO contenente i campi aggiornati
     * @return un'istanza di {@link ApiResponse} con il {@link PathologiesDTO} aggiornato
     */
    @Operation(summary = "Modifica una patologia", description = "Aggiorna la descrizione di una patologia esistente")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Patologia aggiornata con successo"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Patologia non trovata")
    })
    @PutMapping("/pathologies")
    public ApiResponse<PathologiesDTO> editPathology(@RequestBody PathologiesDTO dto){
        PathologiesDTO newDto = pathologiesService.editPathology(dto);
        return ApiResponse.success(newDto);
    }

    /**
     * Endpoint di utilità per simulare un errore del server a fini di test e debug.
     *
     * @return non restituisce valori, solleva un'eccezione di test
     * @throws Exception eccezione generica simulata
     */
    @Operation(summary = "Simula un errore", description = "Endpoint di test per verificare la gestione globale delle eccezioni")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Errore interno simulato del server")
    })
    @GetMapping("/sim-error")
    public ApiResponse<PathologiesDTO> getSimError() throws Exception {
        throw new Exception("è scoppiato tutto!");
    }

    // ==================== ARRIVAL MODES ====================

    /**
     * Recupera un elenco paginato di tutte le modalità di arrivo registrate.
     *
     * @param pageable parametri di paginazione e ordinamento
     * @return un'istanza di {@link ApiResponse} contenente la pagina di {@link ArrivalModeDTO}
     */
    @Operation(summary = "Recupera tutte le modalità di arrivo", description = "Recupero di tutte le modalità di arrivo presenti a DB con supporto alla paginazione")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Lista di modalità di arrivo recuperata con successo"
            )
    })
    @GetMapping("/arrival-modes")
public ApiResponse<Page<ArrivalModeDTO>> getAllArrivalModes(@ParameterObject Pageable pageable) {
    return ApiResponse.success(
            arrivalModeService.getAllArrivalModesPaginated(pageable)
    );
}

    /**
     * Recupera una singola modalità di arrivo tramite il codice identificativo univoco.
     *
     * @param code il codice identificativo della modalità di arrivo
     * @return un'istanza di {@link ApiResponse} contenente l'{@link ArrivalModeDTO} corrispondente
     */
    @Operation(
            summary = "Recupera una modalità di arrivo dato il suo codice",
            description = "Recupero di una modalità di arrivo specifica tramite il relativo codice"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Modalità di arrivo trovata"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Modalità di arrivo non trovata")
    })
    @GetMapping("/arrival-modes/{code}")
    public ApiResponse<ArrivalModeDTO> getArrivalModeByCode(
            @Parameter(description = "Codice identificativo della modalità di arrivo", example = "AMB")
            @PathVariable String code
    ) {
        ArrivalModeDTO dto = arrivalModeService.getArrivalModeById(code);
        return ApiResponse.success(dto);
    }

    /**
     * Crea una nuova modalità di arrivo nel sistema.
     *
     * @param dto il DTO contenente i dati della modalità di arrivo da creare
     * @return un'istanza di {@link ApiResponse} con l'{@link ArrivalModeDTO} creato
     */
    @Operation(summary = "Crea una nuova modalità di arrivo", description = "Inserisce una nuova modalità di arrivo nel database")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Modalità di arrivo creata con successo"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dati non validi o codice già esistente")
    })
    @PostMapping("/arrival-modes")
    public ApiResponse<ArrivalModeDTO> createArrivalMode(@Valid @RequestBody ArrivalModeDTO dto) {
        return ApiResponse.success(
                arrivalModeService.createNewArrivalMode(dto)
        );
    }

    /**
     * Elimina una modalità di arrivo esistente in base al codice.
     *
     * @param code il codice identificativo della modalità di arrivo da rimuovere
     * @return un'istanza di {@link ApiResponse} vuota con esito positivo
     */
    @Operation(summary = "Elimina una modalità di arrivo", description = "Rimuove una modalità di arrivo dal database tramite il codice")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Modalità di arrivo eliminata con successo"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Modalità di arrivo non trovata")
    })
    @DeleteMapping("/arrival-modes/{code}")
    public ApiResponse<Void> deleteArrivalMode(
            @Parameter(description = "Codice identificativo della modalità di arrivo da eliminare", example = "AMB")
            @PathVariable String code
    ) {
        arrivalModeService.deleteArrivalMode(code);
        return ApiResponse.success(null);
    }

    /**
     * Modifica i dati di una modalità di arrivo esistente.
     *
     * @param code il codice identificativo della modalità di arrivo da modificare
     * @param dto il DTO contenente i campi aggiornati
     * @return un'istanza di {@link ApiResponse} con l'{@link ArrivalModeDTO} aggiornato
     */
    @Operation(summary = "Modifica una modalità di arrivo", description = "Aggiorna la descrizione di una modalità di arrivo esistente")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Modalità di arrivo aggiornata con successo"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Modalità di arrivo non trovata")
    })
    @PutMapping("/arrival-modes/{code}")
    public ApiResponse<ArrivalModeDTO> editArrivalMode(
            @Parameter(description = "Codice identificativo della modalità di arrivo da modificare", example = "AMB")
            @PathVariable String code,
            @Valid @RequestBody ArrivalModeDTO dto
    ) {
        ArrivalModeDTO newDto = arrivalModeService.editArrivalMode(code, dto);
        return ApiResponse.success(newDto);
    }

    // ==================== TRIAGE COLORS ====================

    /**
     * Recupera un elenco paginato di tutti i colori Triage registrati.
     *
     * @param pageable parametri di paginazione e ordinamento
     * @return un'istanza di {@link ApiResponse} contenente la pagina di {@link TriageColorDTO}
     */
    @Operation(summary = "Recupera tutti i colori Triage", description = "Recupero di tutti i colori Triage presenti a DB con supporto alla paginazione")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Lista di colori Triage recuperata con successo"
            )
    })
    @GetMapping("/triage-colors")
public ApiResponse<Page<TriageColorDTO>> getAllTriageColors(@ParameterObject Pageable pageable) {
    return ApiResponse.success(
            triageColorService.getAllTriageColorsPaginated(pageable)
    );
}
    /**
     * Recupera un singolo colore Triage tramite il codice identificativo univoco.
     *
     * @param code il codice identificativo del colore Triage
     * @return un'istanza di {@link ApiResponse} contenente il {@link TriageColorDTO} corrispondente
     */
    @Operation(
            summary = "Recupera un colore Triage dato il suo codice",
            description = "Recupero di un colore Triage specifico tramite il relativo codice"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Colore Triage trovato"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Colore Triage non trovato")
    })
    @GetMapping("/triage-colors/{code}")
    public ApiResponse<TriageColorDTO> getTriageColorByCode(
            @Parameter(description = "Codice identificativo del colore Triage", example = "ROSSO")
            @PathVariable String code
    ) {
        TriageColorDTO dto = triageColorService.getTriageColorById(code);
        return ApiResponse.success(dto);
    }

    /**
     * Crea un nuovo colore Triage nel sistema.
     *
     * @param dto il DTO contenente i dati del colore Triage da creare
     * @return un'istanza di {@link ApiResponse} con il {@link TriageColorDTO} creato
     */
    @Operation(summary = "Crea un nuovo colore Triage", description = "Inserisce un nuovo codice colore Triage nel database")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Colore Triage creato con successo"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dati non validi o codice già esistente")
    })
    @PostMapping("/triage-colors")
    public ApiResponse<TriageColorDTO> createTriageColor(@Valid @RequestBody TriageColorDTO dto) {
        return ApiResponse.success(
                triageColorService.createNewTriageColor(dto)
        );
    }

    /**
     * Elimina un colore Triage esistente in base al codice.
     *
     * @param code il codice identificativo del colore Triage da rimuovere
     * @return un'istanza di {@link ApiResponse} vuota con esito positivo
     */
    @Operation(summary = "Elimina un colore Triage", description = "Rimuove un colore Triage dal database tramite il codice")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Colore Triage eliminato con successo"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Colore Triage non trovato")
    })
    @DeleteMapping("/triage-colors/{code}")
    public ApiResponse<Void> deleteTriageColor(
            @Parameter(description = "Codice identificativo del colore Triage da eliminare", example = "ROSSO")
            @PathVariable String code
    ) {
        triageColorService.deleteTriageColor(code);
        return ApiResponse.success(null);
    }

    /**
     * Modifica i dati di un colore Triage esistente.
     *
     * @param code il codice identificativo del colore Triage da modificare
     * @param dto il DTO contenente i campi aggiornati
     * @return un'istanza di {@link ApiResponse} con il {@link TriageColorDTO} aggiornato
     */
    @Operation(summary = "Modifica un colore Triage", description = "Aggiorna i dati di un colore Triage esistente")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Colore Triage aggiornato con successo"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Colore Triage non trovato")
    })
    @PutMapping("/triage-colors/{code}")
    public ApiResponse<TriageColorDTO> editTriageColor(
            @Parameter(description = "Codice identificativo del colore Triage da modificare", example = "ROSSO")
            @PathVariable String code,
            @Valid @RequestBody TriageColorDTO dto
    ) {
        TriageColorDTO newDto = triageColorService.editTriageColor(code, dto);
        return ApiResponse.success(newDto);
    }
}