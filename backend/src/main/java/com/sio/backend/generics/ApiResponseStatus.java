package com.sio.backend.generics;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Enumerazione che definisce lo stato dell'esito delle risposte REST nel sistema SIO.
 * <ul>
 *   <li>{@link #SUCCESS}: Operazione completata correttamente.</li>
 *   <li>{@link #FAIL}: Richiesta non valida o fallimento di validazione/business rules (es. 4xx).</li>
 *   <li>{@link #ERROR}: Errore interno inatteso del server o criticità sistemica (es. 5xx).</li>
 * </ul>
 */
@Schema(description = "Stato dell'esito della risposta API (SUCCESS, FAIL, ERROR)")
public enum ApiResponseStatus {

    @Schema(description = "Operazione completata con successo")
    SUCCESS,

    @Schema(description = "Errore critico di sistema o lato server")
    ERROR,

    @Schema(description = "Fallimento della richiesta da parte del client (es. validazione o dati non trovati)")
    FAIL
}