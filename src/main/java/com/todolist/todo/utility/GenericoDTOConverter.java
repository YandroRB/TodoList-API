package com.todolist.todo.utility;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GenericoDTOConverter {
    @Autowired
    private ModelMapper modelMapper;

    public <S,D> D convertirADTO(S origenEntidad, Class<D> claseDTO ){
        return modelMapper.map(origenEntidad, claseDTO);
    }

    public <S,D> S convertirAEntidad(D origenDTO, Class<S> claseEntidad ){
        return modelMapper.map(origenDTO,claseEntidad);
    }
    public <S,D>List<D>convertirListADTO(List<S> origenEntidad, Class<D> claseDTO ){
        return origenEntidad.stream().map(
                entidad->convertirADTO(entidad,claseDTO))
                .collect(Collectors.toList());
    }
    public <S,D> List<S> convertirListAEntidad(List<D> origenEntidad, Class<S> claseEntidad ){
        return origenEntidad.stream().map(
                dto->convertirAEntidad(dto,claseEntidad))
                .collect(Collectors.toList());
    }
}
