
package com.sio.backend.resources.triagecolor;

import org.springframework.stereotype.Component;

/**
 * Componente Mapper per la conversione bidirezionale tra l'entità JPA {@link TriageColor}
 * e il relativo Data Transfer Object {@link TriageColorDTO}.
 */
@Component
public class TriageColorMapper {

    /**
     * Converte un'entità {@link TriageColor} nel rispettivo {@link TriageColorDTO}.
     *
     * @param entity l'entità di persistenza da convertire
     * @return il DTO popolato con i dati dell'entità, oppure null se l'input è null
     */
    public TriageColorDTO toDTO(TriageColor entity) {
        if (entity == null) return null;

        return new TriageColorDTO(
                entity.getCode(),
                entity.getDisplayName(),
                entity.getPriority(),
                entity.getHexValue()
        );
    }

    /**
     * Converte un {@link TriageColorDTO} nella rispettiva entità JPA {@link TriageColor}.
     *
     * @param dto il DTO in ingresso da convertire
     * @return l'entità popolata con i dati del DTO, oppure null se l'input è null
     */
    public TriageColor toEntity(TriageColorDTO dto) {
        if (dto == null) return null;

        TriageColor entity = new TriageColor();
        entity.setCode(dto.code());
        entity.setDisplayName(dto.displayName());
        entity.setPriority(dto.priority());
        entity.setHexValue(dto.hexValue());

        return entity;
    }
}