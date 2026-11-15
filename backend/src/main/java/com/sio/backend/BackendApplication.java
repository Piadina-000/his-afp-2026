package com.sio.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principale ed entry-point dell'applicazione Spring Boot per il sistema SIO[cite: 10].
 * L'annotazione {@link SpringApplication} abilita la configurazione automatica,
 * la scansione dei componenti e la configurazione della proprietà di Spring Boot.
 */
@SpringBootApplication
public class BackendApplication {

    /**
     * Metodo main che avvia l'applicazione Spring Boot come eseguibile standalone[cite: 10].
     *
     * @param args argomenti passati da riga di comando all'avvio[cite: 10]
     */
    static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}