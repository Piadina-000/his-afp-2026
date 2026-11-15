package com.sio.backend.resources.pathology;

import org.springframework.stereotype.Component;

/**
 * Componente Mapper per la conversione bidirezionale tra l'entità JPA {@link Pathologies}
 * e il relativo Data Transfer Object {@link PathologiesDTO}.
 */
@Component
public class PathologiesMapper {

    /**
     * Converte un'entità {@link Pathologies} nel rispettivo {@link PathologiesDTO}.
     *
     * @param entity l'entità di persistenza da convertire
     * @return il DTO popolato con i dati dell'entità, oppure null se l'input è null
     */
    public PathologiesDTO toDTO(Pathologies entity){
        if (entity == null) return null;

        return new PathologiesDTO(
                entity.getCode(),
                entity.getDescription()
        );
    }

    /**
     * Converte un {@link PathologiesDTO} nella rispettiva entità JPA {@link Pathologies}.
     *
     * @param dto il DTO in ingresso da convertire
     * @return l'entità popolata con i dati del DTO, oppure null se l'input è null
     */
    public Pathologies toEntity(PathologiesDTO dto){
        if (dto == null) return null;

        Pathologies entity = new Pathologies();
        entity.setCode(dto.code());
        entity.setDescription(dto.description());

        return entity;
    }
}