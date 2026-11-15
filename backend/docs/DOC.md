# Spring Boot – Architettura e componenti fondamentali

## 1. Introduzione a Spring Boot

### 1.1 Cos'è Spring Boot?

Spring Boot è un framework basato su Spring, progettato per semplificare lo sviluppo di applicazioni Java, in particolare applicazioni Web, REST API e applicazioni backend.

Nasce con l'obiettivo di ridurre la quantità di configurazione manuale necessaria per realizzare un'applicazione Spring, fornendo configurazioni automatiche e strumenti già pronti all'utilizzo.

Tra le sue caratteristiche principali troviamo:

- **Auto-Configuration:** configura automaticamente numerosi componenti dell'applicazione in base alle dipendenze presenti.
- **Dependency Injection:** gestisce automaticamente le dipendenze tra gli oggetti.
- **Embedded Server:** permette di eseguire l'applicazione utilizzando un server integrato, come Tomcat.
- **Starter Dependencies:** mette a disposizione dipendenze preconfigurate per funzionalità comuni.
- **Production Ready:** offre strumenti per logging, monitoraggio e gestione dell'applicazione.

### 1.2 Perché utilizzare Spring Boot?

Sviluppare un backend Java senza un framework può richiedere la gestione manuale di numerosi aspetti infrastrutturali, come:

- Creazione e gestione degli oggetti.
- Configurazione delle connessioni al database.
- Gestione delle richieste HTTP.
- Serializzazione e deserializzazione dei dati.
- Gestione centralizzata degli errori.

Spring Boot permette di delegare gran parte di queste responsabilità al framework, consentendo allo sviluppatore di concentrarsi maggiormente sulla logica applicativa.

Il risultato è un'applicazione più modulare, manutenibile e facilmente estendibile.

---

## 2. Il concetto di IoC – Inversion of Control

Uno dei concetti fondamentali dell'ecosistema Spring è l'**Inversion of Control (IoC)**.

### 2.1 Cos'è l'Inversion of Control?

Tradizionalmente, quando sviluppiamo un'applicazione Java, siamo noi a creare direttamente gli oggetti e a gestirne le dipendenze.

Ad esempio:

```java
public class PathologiesController {

    private PathologiesService service;

    public PathologiesController() {
        this.service = new PathologiesService();
    }
}
```

In questo caso il Controller è responsabile della creazione del Service.

Questo introduce un forte accoppiamento tra le due classi: il Controller conosce direttamente il modo in cui viene istanziato il Service.

Con Spring, invece, il controllo della creazione e della gestione degli oggetti viene delegato al framework.

Spring mette a disposizione un contenitore chiamato **IoC Container**, che si occupa di:

- Creare gli oggetti necessari.
- Gestire il loro ciclo di vita.
- Risolvere le dipendenze tra i componenti.
- Fornire gli oggetti alle classi che ne hanno bisogno.

Gli oggetti gestiti dal contenitore Spring vengono chiamati **Bean**.

### 2.2 Dependency Injection (DI)

La Dependency Injection è il meccanismo attraverso il quale Spring (e altri framework tipo Angular) realizza concretamente l'Inversion of Control.

Vediamo come cambia il nostro esempio:

```java
@RestController
public class PathologiesController {

    private final PathologiesService service;

    public PathologiesController(PathologiesService service) {
        this.service = service;
    }
}
```

Non utilizziamo più `new PathologiesService()`.

Spring riconosce che il Controller necessita di un'istanza di `PathologiesService` e provvede a iniettarla automaticamente.

Questo approccio offre diversi vantaggi:

- Riduce l'accoppiamento tra le classi.
- Facilita i test automatici.
- Migliora la manutenibilità.
- Permette di sostituire facilmente le implementazioni.

**Nota:** la Constructor Injection, mostrata nell'esempio, è generalmente la modalità consigliata per iniettare dipendenze obbligatorie.

---

## 3. Organizzazione del progetto: metodologia By Feature

Il progetto utilizza la metodologia di organizzazione **By Feature**.

Questa metodologia prevede di organizzare il codice raggruppando le classi in base alla funzionalità o al dominio applicativo a cui appartengono, anziché dividerle esclusivamente per tipologia tecnica.

Ad esempio, un'organizzazione tradizionale potrebbe essere:

```text
controller/
service/
repository/
entity/
dto/
```

In un'organizzazione By Feature, invece, troviamo:

```text
resources/
    pathology/
        PathologiesDTO.java
        Pathologies.java
        PathologiesMapper.java
        PathologiesRepository.java
        PathologiesService.java
    arrival_mode/
        ...
    triage_color/
        ...
    ResourcesController.java
```

In questo modo, tutte le classi che collaborano alla gestione delle patologie sono raccolte all'interno dello stesso package.

### 3.1 Vantaggi della metodologia By Feature

- **Coesione:** le classi appartenenti alla stessa funzionalità si trovano nello stesso package.
- **Manutenibilità:** modificare una funzionalità richiede generalmente di intervenire in una porzione circoscritta del progetto.
- **Scalabilità:** aggiungere nuove funzionalità non comporta necessariamente la modifica della struttura esistente.
- **Organizzazione:** il codice risulta più semplice da comprendere, soprattutto nei progetti di grandi dimensioni.

---

## 4. Architettura funzionale dell'applicazione

L'applicazione segue un'architettura a livelli, nella quale ogni componente ha una responsabilità specifica.

Il flusso principale di una richiesta REST è il seguente:

![](/home/pietro/Projects/AFP/AFP2027/SIO/his-afp/backend/docs/spring-architettura-base.png "Architettura SringBoot")

Il Mapper non è necessariamente un livello attraversato in modo sequenziale da tutte le operazioni: viene utilizzato dal Service quando occorre convertire i dati tra Entity e DTO.

#### Diagramma avanzato
![](/home/pietro/Projects/AFP/AFP2027/SIO/his-afp/backend/docs/spring-architect-adv.png)

### 4.1 Flusso completo di una richiesta

Immaginiamo che un client voglia ottenere l'elenco delle patologie presenti nel database.

1. Il client invia una richiesta HTTP al Controller.
2. Il Controller riceve la richiesta e invoca il metodo del Service.
3. Il Service gestisce la logica applicativa.
4. Il Service utilizza il Repository per recuperare i dati.
5. Il Repository interagisce con il database.
6. Il Service utilizza il Mapper per convertire le Entity in DTO.
7. Il Controller restituisce la risposta al client.

La risposta viene rappresentata attraverso un oggetto `ApiResponse`, che uniforma la struttura delle risposte dell'API.

In caso di errore, entra in gioco il `GlobalExceptionHandler`, che intercetta le eccezioni e restituisce una risposta coerente.

---

## 5. Analisi dei componenti

### 5.1 BackendApplication.java – Classe principale

È il punto di ingresso dell'applicazione Spring Boot.

Contiene il metodo `main`, che avvia il framework e inizializza il contesto Spring.

Esempio:

```java
package com.sio.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(
            BackendApplication.class,
            args
        );
    }
}
```

L'annotazione `@SpringBootApplication` racchiude tre funzionalità principali:

- `@SpringBootConfiguration`: identifica la classe come sorgente di configurazione Spring.
- `@EnableAutoConfiguration`: abilita la configurazione automatica.
- `@ComponentScan`: permette a Spring di individuare i componenti da gestire.

È importante posizionare questa classe nel package principale dell'applicazione (`com.sio.backend`), in modo che la scansione dei componenti possa raggiungere i package sottostanti.

### 5.2 Controller – Gestione delle richieste HTTP

Il Controller rappresenta il punto di ingresso delle richieste HTTP all'interno dell'applicazione.

Ha il compito di:

- Esporre gli endpoint REST.
- Ricevere i parametri delle richieste.
- Validare e interpretare gli input.
- Invocare i Service.
- Restituire le risposte al client.

Nel nostro progetto troviamo `ResourcesController.java`.

Le annotazioni più utilizzate sono:

| Annotazione | Descrizione |
|---|---|
| `@RestController` | Identifica una classe come Controller REST. |
| `@RequestMapping` | Definisce il percorso base degli endpoint. |
| `@GetMapping` | Gestisce le richieste HTTP GET. |
| `@PostMapping` | Gestisce le richieste HTTP POST. |
| `@PutMapping` | Gestisce le richieste HTTP PUT. |
| `@DeleteMapping` | Gestisce le richieste HTTP DELETE. |
| `@PathVariable` | Recupera un parametro presente nell'URL. |
| `@RequestBody` | Recupera il corpo della richiesta HTTP. |
| `@RequestParam` | Recupera un parametro della query string. |

Esempio:

```java
@RestController
@RequestMapping("/api/resources")
public class ResourcesController {

    private final PathologiesService service;

    public ResourcesController(PathologiesService service) {
        this.service = service;
    }

    @GetMapping("/pathologies")
    public ApiResponse<List<PathologiesDTO>> getAll() {

        List<PathologiesDTO> result = service.findAll();

        return ApiResponse.success(result);
    }
}
```

In questo esempio viene esposto l'endpoint:

```http
GET /api/resources/pathologies
```

Il Controller non contiene la logica di recupero dei dati, ma delega questa responsabilità al Service.

**Principio fondamentale:** il Controller deve essere il più possibile leggero e limitarsi alla gestione delle richieste e delle risposte HTTP.

---

### 5.3 Service – Logica applicativa

Il Service contiene la logica applicativa dell'applicazione.

Rappresenta il livello intermedio tra Controller e Repository.

Le sue responsabilità principali sono:

- Gestire le operazioni applicative.
- Coordinare l'interazione tra più Repository.
- Applicare eventuali regole di business.
- Gestire le transazioni.
- Utilizzare i Mapper per convertire i dati.

Nel nostro progetto troviamo `PathologiesService.java`.

Esempio:

```java
@Service
public class PathologiesService {

    private final PathologiesRepository repository;
    private final PathologiesMapper mapper;

    public PathologiesService(
        PathologiesRepository repository,
        PathologiesMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<PathologiesDTO> findAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    public PathologiesDTO findById(Long id) {

        Pathologies pathology = repository.findById(id)
            .orElseThrow(() ->
                new RuntimeException("Patologia non trovata")
            );

        return mapper.toDTO(pathology);
    }
}
```

L'annotazione `@Service` comunica a Spring che la classe rappresenta un componente contenente logica applicativa e che deve essere gestita dal contenitore IoC.

Il Service è indipendente dal protocollo HTTP: può essere utilizzato anche da altri componenti dell'applicazione.

**Principio fondamentale:** la logica applicativa deve essere concentrata nel Service e non distribuita all'interno dei Controller.

---

### 5.4 Repository – Accesso ai dati

Il Repository rappresenta il livello di accesso ai dati.

Il suo compito è interagire con il database, permettendo all'applicazione di eseguire operazioni CRUD:

- Create
- Read
- Update
- Delete

In un progetto Spring Boot che utilizza Spring Data JPA, è possibile definire un Repository semplicemente estendendo `JpaRepository`.

Nel nostro progetto troviamo `PathologiesRepository.java`.

Esempio:

```java
@Repository
public interface PathologiesRepository extends JpaRepository<Pathologies, Long> {
    Optional<Pathologies> findByName(String name);
}
```

In questo esempio:

- `Pathologies` rappresenta l'entità gestita.
- `Long` rappresenta il tipo della chiave primaria.
- `findByName` è un metodo derivato dal nome, per il quale Spring Data JPA genera automaticamente la query corrispondente.

Estendendo `JpaRepository` otteniamo già diversi metodi:

| Metodo | Funzionalità |
|---|---|
| `findAll()` | Recupera tutti i record. |
| `findById(id)` | Recupera un record tramite ID. |
| `save(entity)` | Inserisce o aggiorna un record. |
| `deleteById(id)` | Elimina un record tramite ID. |
| `existsById(id)` | Verifica l'esistenza di un record. |
| `count()` | Restituisce il numero di record. |

Non è necessario implementare manualmente questi metodi.

**Principio fondamentale:** il Repository deve occuparsi esclusivamente dell'accesso ai dati, senza contenere logica applicativa.

---

### 5.5 Entity – Rappresentazione dei dati

L'Entity rappresenta un oggetto persistente, generalmente associato a una tabella del database.

Nel nostro progetto troviamo `Pathologies.java`.

Utilizzando JPA, possiamo definire una classe Entity attraverso le relative annotazioni.

Esempio:

```java
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pathologies")
public class Pathologies {
    @Id
    @Size(max = 10)
    @NotBlank
    @Column(name = "code", nullable = false, length = 10)
    private String code;

    @Size(max = 100)
    @NotBlank
    @Column(name = "description", nullable = false, length = 100)
    private String description;

    @CreationTimestamp
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;
}
```

Le annotazioni principali sono:

| Annotazione | Descrizione |
|---|---|
| `@Entity` | Identifica una classe come entità JPA. |
| `@Table` | Specifica la tabella associata. |
| `@Id` | Identifica la chiave primaria. |
| `@GeneratedValue` | Definisce la strategia di generazione dell'ID. |
| `@Column` | Permette di configurare una colonna. |

L'Entity è strettamente legata al modello di persistenza e non dovrebbe essere esposta direttamente attraverso le API REST.

Per questo motivo utilizziamo i DTO.

---

### 5.6 DTO – Data Transfer Object

È un oggetto utilizzato per trasferire informazioni tra differenti livelli dell'applicazione o tra applicazione e client.

Nel nostro progetto troviamo `PathologiesDTO.java`, implementato utilizzando un **Record Java**.

#### Cos'è un Record in Java?

Un Record è una particolare tipologia di classe introdotta in Java 16, progettata per rappresentare oggetti che hanno principalmente lo scopo di contenere e trasportare dati.

Rispetto a una classe tradizionale, un Record permette di definire un oggetto in maniera più compatta, evitando di scrivere manualmente numerosi elementi ripetitivi, come:

- Costruttore.
- Getter (metodi di accesso ai campi).
- `equals()`.
- `hashCode()`.
- `toString()`.

Esempio di implementazione del DTO utilizzando un Record:

```java
public record PathologiesDTO(
    Long id,
    String name,
    String description
) {}
```

Con poche righe di codice definiamo un oggetto completo, senza dover implementare manualmente tutti i metodi necessari.

#### Perché utilizziamo un Record invece di una classe?

Nel nostro caso il DTO ha il semplice compito di trasportare informazioni tra i livelli dell'applicazione.

Non deve gestire uno stato modificabile durante il proprio ciclo di vita né contenere una logica applicativa complessa.

Per questo motivo il Record rappresenta una soluzione particolarmente adatta.

I principali vantaggi sono:

- **Immutabilità:** i componenti di un Record sono final e non possono essere riassegnati dopo la creazione dell'oggetto.
- **Meno codice:** Java genera automaticamente costruttore, metodi di accesso e metodi di confronto.
- **Maggiore leggibilità:** la struttura del DTO è immediatamente comprensibile.
- **Sicurezza:** riduce il rischio di modifiche accidentali ai dati durante il trasferimento.
- **Semplicità:** permette di concentrarsi esclusivamente sui dati che devono essere trasferiti.

#### Come accediamo ai dati di un Record?

A differenza delle classi tradizionali, i Record non utilizzano i classici getter Java (`getName()`).

Per accedere ai componenti utilizziamo direttamente il nome del campo seguito dalle parentesi:

```java
PathologiesDTO dto = new PathologiesDTO(
    1L,
    "Diabete",
    "Patologia metabolica"
);

String name = dto.name();
Long id = dto.id();
```

#### Record ed Entity: perché sono differenti?

È importante distinguere il DTO dall'Entity.

| Caratteristica | Entity | DTO (Record) |
|---|---|---|
| Obiettivo | Rappresentare i dati persistenti | Trasferire dati |
| Persistenza | Associata al database | Non direttamente persistente |
| Mutabilità | Generalmente mutabile | Immutabile |
| Annotazioni | Utilizza annotazioni JPA | Non richiede annotazioni JPA |
| Getter | Metodi getter tradizionali | Accessori del Record |
| Setter | Generalmente presenti | Non disponibili |

**Principio fondamentale:** utilizziamo i Record per rappresentare DTO semplici e immutabili, mentre utilizziamo le Entity per rappresentare oggetti persistenti, gestiti da JPA e associati alle tabelle del database.

---

### 5.7 Mapper – Conversione tra Entity e DTO

Il Mapper è responsabile della conversione degli oggetti tra differenti rappresentazioni.

Nel nostro progetto troviamo `PathologiesMapper.java`.

Il suo compito principale è convertire:

- Entity → DTO
- DTO → Entity

Esempio di implementazione manuale:

```java
@Component
public class PathologiesMapper {
    public PathologiesDTO toDTO(Pathologies entity){
        if (entity == null) return null;

        return new PathologiesDTO(
                entity.getCode(),
                entity.getDescription()
        );
    }

    public Pathologies toEntity(PathologiesDTO dto){
        if (dto == null) return null;

        Pathologies entity = new Pathologies();
        entity.setCode(dto.code());
        entity.setDescription(dto.description());

        return entity;
    }
}
```

L'annotazione `@Component` permette a Spring di gestire il Mapper come Bean.

In progetti più complessi è possibile utilizzare librerie come MapStruct per generare automaticamente le implementazioni dei Mapper.

**Principio fondamentale:** il Mapper deve occuparsi della conversione dei dati, non della logica di business.

---

### 5.8 ApiResponse – Struttura standard delle risposte

All'interno del package `generics` troviamo:

- `ApiResponse.java`
- `ApiResponseStatus.java`

Queste classi permettono di definire un formato standardizzato per le risposte delle API.

L'obiettivo è evitare che ogni endpoint restituisca una struttura differente.

Un possibile esempio di implementazione è:

```java
public class ApiResponse<T> {

    private ApiResponseStatus status;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {

        ApiResponse<T> response = new ApiResponse<>();

        response.setStatus(ApiResponseStatus.SUCCESS);
        response.setMessage("Operazione completata");
        response.setData(data);

        return response;
    }

    // Getter e Setter
}
```

L'utilizzo del tipo generico `T` permette di riutilizzare la stessa classe con differenti tipi di dati.

Ad esempio:

```java
ApiResponse<PathologiesDTO>
```

oppure:

```java
ApiResponse<List<PathologiesDTO>>
```

Un possibile enum:

```java
public enum ApiResponseStatus {
    SUCCESS,
    ERROR
}
```

Il vantaggio dei Generics è quello di poter utilizzare una struttura comune senza perdere la sicurezza dei tipi offerta da Java.

---

### 5.9 GlobalExceptionHandler – Gestione centralizzata degli errori

Un'applicazione backend deve essere in grado di gestire gli errori in modo uniforme.

Immaginiamo che il client richieda una patologia che non esiste nel database.

Senza una gestione centralizzata, ogni Controller potrebbe dover gestire autonomamente le eccezioni.

Spring mette a disposizione `@RestControllerAdvice`, che permette di intercettare le eccezioni generate dai Controller e gestirle in un unico punto.

Nel nostro progetto troviamo `GlobalExceptionHandler.java`.

Esempio:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(
        RuntimeException exception
    ) {

        ApiResponse<Void> response = new ApiResponse<>();

        response.setStatus(ApiResponseStatus.ERROR);
        response.setMessage(exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
}
```

Annotazioni principali:

| Annotazione | Descrizione |
|---|---|
| `@RestControllerAdvice` | Definisce un gestore globale delle eccezioni REST. |
| `@ExceptionHandler` | Identifica il metodo responsabile della gestione di una determinata eccezione. |

È possibile definire gestori differenti per diverse tipologie di errore.

Ad esempio:

- `ResourceNotFoundException`: risorsa non trovata (HTTP 404).
- `MethodArgumentNotValidException`: errore di validazione (HTTP 400).
- `Exception`: errore generico non previsto (HTTP 500).

**Principio fondamentale:** gli errori devono essere gestiti centralmente, evitando di duplicare codice nei Controller.

---

## 6. Configurazione dell'applicazione

Spring Boot permette di gestire la configurazione attraverso file YAML o Properties.

Nel nostro progetto troviamo:

```text
src/main/resources/
    application.yaml
    application-dev.yaml
    application-prod.yaml
    application-test.yaml
```

### 6.1 application.yaml

È il file di configurazione principale.

Contiene le configurazioni comuni dell'applicazione.

Esempio:

```yaml
spring:
  application:
    name: backend

server:
  port: 8080
```

### 6.2 Profili Spring

Spring Boot permette di definire configurazioni differenti in base all'ambiente di esecuzione.

Nel nostro caso abbiamo tre profili:

| Profilo | Utilizzo |
|---|---|
| `dev` | Ambiente di sviluppo. |
| `test` | Ambiente di test. |
| `prod` | Ambiente di produzione. |

Ad esempio, in `application-dev.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/backend_dev
    username: developer
    password: password
```

In `application-prod.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://production-server:5432/backend
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

Il profilo può essere attivato attraverso:

```yaml
spring:
  profiles:
    active: dev
```

Oppure attraverso una variabile d'ambiente:

```bash
SPRING_PROFILES_ACTIVE=prod
```

In questo modo possiamo utilizzare lo stesso codice sorgente in ambienti differenti, modificando esclusivamente la configurazione.

Le credenziali reali non dovrebbero essere salvate nel repository Git.

---

## 7. Logging – logback-spring.xml

Il logging permette di registrare gli eventi che si verificano durante l'esecuzione dell'applicazione.

Spring Boot utilizza normalmente Logback come implementazione di logging predefinita.

Il file `logback-spring.xml` permette di personalizzare:

- Formato dei messaggi.
- Livelli di logging.
- Destinazione dei log.
- Rotazione dei file.
- Configurazioni differenti per ambiente.

I principali livelli di logging sono:

| Livello | Utilizzo |
|---|---|
| TRACE | Informazioni estremamente dettagliate. |
| DEBUG | Informazioni utili durante lo sviluppo. |
| INFO | Informazioni generali sull'esecuzione. |
| WARN | Situazioni anomale che non interrompono l'applicazione. |
| ERROR | Errori che richiedono attenzione. |

Esempio di utilizzo all'interno di un Service:

```java
private static final Logger log =
    LoggerFactory.getLogger(PathologiesService.class);

public List<PathologiesDTO> findAll() {

    log.info("Recupero elenco patologie");

    return repository.findAll()
            .stream()
            .map(mapper::toDTO)
            .toList();
}
```

Il logging è fondamentale per individuare problemi, monitorare il comportamento dell'applicazione e analizzare eventuali malfunzionamenti.

---

## 8. Altri elementi della struttura del progetto

### 8.1 pom.xml

È il file di configurazione di Maven.

Maven è uno strumento di gestione del progetto Java che permette di:

- Gestire le dipendenze.
- Compilare il progetto.
- Eseguire i test.
- Creare il pacchetto eseguibile.
- Gestire il ciclo di build.

Esempio di dipendenze tipiche in un progetto Spring Boot:

```xml
<dependencies>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>

</dependencies>
```

Le dipendenze effettivamente necessarie dipendono dalle funzionalità implementate nel progetto.

### 8.2 Maven Wrapper

I file:

```text
mvnw
mvnw.cmd
```

permettono di eseguire Maven senza richiedere necessariamente un'installazione globale sul computer.

Sono particolarmente utili nei progetti condivisi, perché permettono di utilizzare la versione di Maven definita nel progetto.

### 8.3 static e templates

Sono directory utilizzate da Spring Boot per la gestione delle risorse Web.

- `static`: contiene risorse statiche come CSS, JavaScript, immagini e file HTML.
- `templates`: contiene template HTML elaborati da un motore di template, ad esempio Thymeleaf.

In un backend che espone esclusivamente REST API, queste directory potrebbero non essere utilizzate direttamente.

---

## 9. Riepilogo dell'architettura

Di seguito vengono riepilogate le responsabilità dei componenti principali.

| Componente | Responsabilità |
|---|---|
| BackendApplication | Avvio dell'applicazione. |
| Controller | Gestione delle richieste HTTP. |
| Service | Gestione della logica applicativa. |
| Repository | Accesso al database. |
| Entity | Rappresentazione dei dati persistenti. |
| DTO | Trasferimento dei dati. |
| Mapper | Conversione Entity/DTO. |
| ApiResponse | Struttura standard delle risposte. |
| ApiResponseStatus | Definizione degli stati delle risposte. |
| GlobalExceptionHandler | Gestione centralizzata degli errori. |
| application.yaml | Configurazione generale. |
| application-{profile}.yaml | Configurazioni specifiche per ambiente. |
| logback-spring.xml | Configurazione del logging. |
| pom.xml | Gestione del progetto e delle dipendenze. |

### 9.1 Regole da ricordare

Durante lo sviluppo di un'applicazione Spring Boot organizzata By Feature è importante rispettare alcune regole:

1. Il Controller non deve contenere logica di business.
2. Il Service deve coordinare le operazioni applicative.
3. Il Repository deve occuparsi esclusivamente della persistenza.
4. L'Entity rappresenta il modello del database.
5. Il DTO rappresenta il modello di trasferimento dei dati.
6. Il Mapper converte le differenti rappresentazioni.
7. Le dipendenze tra componenti devono essere gestite attraverso Dependency Injection.
8. Le eccezioni devono essere gestite in maniera centralizzata.
9. Le configurazioni devono essere separate in base all'ambiente di esecuzione.

---

## 10. Conclusioni

Spring Boot permette di sviluppare applicazioni backend Java moderne, modulari e facilmente manutenibili.

L'utilizzo dell'Inversion of Control e della Dependency Injection consente di delegare al framework la gestione degli oggetti e delle relative dipendenze.

L'organizzazione By Feature permette invece di mantenere vicine tutte le classi appartenenti alla stessa funzionalità applicativa.

La combinazione di questi approcci consente di realizzare applicazioni strutturate, nelle quali ogni componente ha una responsabilità ben definita.

**L'obiettivo principale non è semplicemente far funzionare il codice, ma realizzare un'applicazione nella quale ogni componente sia facilmente comprensibile, testabile e modificabile.**