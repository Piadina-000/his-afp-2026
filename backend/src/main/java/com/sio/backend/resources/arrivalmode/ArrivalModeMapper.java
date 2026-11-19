package com.sio.backend.resources.arrivalmode;

import org.springframework.stereotype.Component;

/**
 * Componente Mapper per la conversione bidirezionale tra l'entità JPA {@link ArrivalMode}
 * e il relativo Data Transfer Object {@link ArrivalModeDTO}.
 */
@Component
public class ArrivalModeMapper {

    /**
     * Converte un'entità {@link ArrivalMode} nel rispettivo {@link ArrivalModeDTO}.
     *
     * @param entity l'entità di persistenza da convertire
     * @return il DTO popolato con i dati dell'entità, oppure null se l'input è null
     */
    public ArrivalModeDTO toDTO(ArrivalMode entity) {
        if (entity == null) return null;

        return new ArrivalModeDTO(
                entity.getCode(),
                entity.getDescription()
        );
    }

    /**
     * Converte un {@link ArrivalModeDTO} nella rispettiva entità JPA {@link ArrivalMode}.
     *
     * @param dto il DTO in ingresso da convertire
     * @return l'entità popolata con i dati del DTO, oppure null se l'input è null
     */
    public ArrivalMode toEntity(ArrivalModeDTO dto) {
        if (dto == null) return null;

        ArrivalMode entity = new ArrivalMode();
        entity.setCode(dto.code());
        entity.setDescription(dto.description());

        return entity;
    }
}