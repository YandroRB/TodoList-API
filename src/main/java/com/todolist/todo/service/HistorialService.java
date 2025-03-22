package com.todolist.todo.service;

import com.todolist.todo.dto.response.HistorialResponseDTO;
import com.todolist.todo.model.CustomRevisionEntity;
import com.todolist.todo.model.Tarea;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistorialService {
    private final AuditReader auditReader;

    public List<HistorialResponseDTO> obtenerHistorialTareas(Long tareaId){
        List<HistorialResponseDTO> historial = new ArrayList<>();
        try{
            @SuppressWarnings("unchecked")
            List<Object[]> revisiones=auditReader.createQuery().
                    forRevisionsOfEntity(Tarea.class,false,true).
                    add(AuditEntity.id().eq(tareaId)).
                    addOrder(AuditEntity.revisionNumber().asc()).
                    getResultList();
            Tarea tareaPrevia=null;
            for(Object[] revision:revisiones){
                Tarea tareaActual=(Tarea)revision[0];
                CustomRevisionEntity revisionEntity=(CustomRevisionEntity)revision[1];
                RevisionType tipoRevision = (RevisionType) revision[2];

                HistorialResponseDTO hDTO=new HistorialResponseDTO();
                hDTO.setId(tareaId);
                hDTO.setRevisionId((long) revisionEntity.getId());
                hDTO.setFechaCambio(LocalDateTime.
                        ofInstant(Instant.ofEpochMilli(revisionEntity.getTimestamp()), ZoneId.systemDefault()));
                hDTO.setUsuario(revisionEntity.getUsername());

                switch(tipoRevision){
                    case ADD -> hDTO.setTipoRvision("CREACION");
                    case MOD -> hDTO.setTipoRvision("MODIFICACION");
                    case DEL -> hDTO.setTipoRvision("ELIMINACION");
                }

                Map<String,HistorialResponseDTO.CambioCampoDTO> cambiosCampo=new HashMap<>();
                if(tareaPrevia!=null&&tipoRevision!=RevisionType.DEL){

                    if(!Objects.equals(tareaPrevia.getTitulo(),tareaActual.getTitulo())){
                        HistorialResponseDTO.CambioCampoDTO campoDTO=new HistorialResponseDTO.CambioCampoDTO();
                        campoDTO.setValorAnterior(tareaPrevia.getTitulo());
                        campoDTO.setValorNuevo(tareaActual.getTitulo());
                        cambiosCampo.put("titulo",campoDTO);
                    }

                    if(!Objects.equals(tareaPrevia.getDescripcion(),tareaActual.getDescripcion())){
                        HistorialResponseDTO.CambioCampoDTO campoDTO=new HistorialResponseDTO.CambioCampoDTO();
                        campoDTO.setValorAnterior(tareaPrevia.getDescripcion());
                        campoDTO.setValorNuevo(tareaActual.getDescripcion());
                        cambiosCampo.put("descripcion",campoDTO);
                    }

                    if(!Objects.equals(tareaPrevia.getEstado(),tareaActual.getEstado())){
                        HistorialResponseDTO.CambioCampoDTO campoDTO=new HistorialResponseDTO.CambioCampoDTO();
                        campoDTO.setValorAnterior(tareaPrevia.getEstado().toString());
                        campoDTO.setValorNuevo(tareaActual.getEstado().toString());
                        cambiosCampo.put("estado",campoDTO);
                    }

                    if(!Objects.equals(tareaPrevia.getFechaLimite(),tareaActual.getFechaLimite())){
                        HistorialResponseDTO.CambioCampoDTO campoDTO=new HistorialResponseDTO.CambioCampoDTO();
                        campoDTO.setValorAnterior(tareaPrevia.getFechaLimite()!=null?tareaPrevia.getFechaLimite().toString():"Sin fecha limite");
                        campoDTO.setValorNuevo(tareaActual.getFechaLimite()!=null?tareaActual.getFechaLimite().toString():"Sin fecha limite");
                        cambiosCampo.put("fechaLimite",campoDTO);
                    }
                    if(!Objects.equals(tareaPrevia.getDiasAnticipadosRecordatorio(),tareaActual.getDiasAnticipadosRecordatorio())){
                        HistorialResponseDTO.CambioCampoDTO campoDTO=new HistorialResponseDTO.CambioCampoDTO();
                        campoDTO.setValorAnterior(tareaPrevia.getDiasAnticipadosRecordatorio().toString());
                        campoDTO.setValorNuevo(tareaActual.getDiasAnticipadosRecordatorio().toString());
                        cambiosCampo.put("diasAnticipadosRecordatorio",campoDTO);
                    }

                    if(!Objects.equals(tareaPrevia.getSubTarea(),tareaActual.getSubTarea())){
                        HistorialResponseDTO.CambioCampoDTO campoDTO=new HistorialResponseDTO.CambioCampoDTO();
                        String subTareasAnteriores = tareaPrevia.getSubTarea()!=null?
                                tareaPrevia.getSubTarea().stream().
                                        map(t->t.getIdentificador()+" : {"+t.getTitulo()+" , "+t.getDescripcion()+"}").
                                        collect(Collectors.joining(" ; ")):"Sin subTarea";
                        String subTareasNuevas = tareaActual.getSubTarea()!=null?
                                tareaActual.getSubTarea().stream().
                                        map(t->t.getIdentificador()+" : {"+t.getTitulo()+" , "+t.getDescripcion()+"}").
                                        collect(Collectors.joining(" ; ")):"Sin subTarea";
                        campoDTO.setValorAnterior(subTareasAnteriores);
                        campoDTO.setValorNuevo(subTareasNuevas);
                        cambiosCampo.put("subTarea",campoDTO);
                    }
                    if(!Objects.equals(tareaPrevia.getCategorias(),tareaActual.getCategorias())){
                        HistorialResponseDTO.CambioCampoDTO campoDTO=new HistorialResponseDTO.CambioCampoDTO();
                        String categoriasAnteriores = tareaPrevia.getCategorias()!=null?
                                tareaPrevia.getCategorias().stream().
                                        map(c->c.getIdentificador()+" : "+c.getNombre()).
                                        collect(Collectors.joining(",")):"Sin categorias";
                        String categoriasNuevas = tareaActual.getCategorias()!=null?
                                tareaActual.getCategorias().stream().
                                        map(c->c.getIdentificador()+" : "+c.getNombre()).
                                        collect(Collectors.joining(",")):"Sin categorias";
                        campoDTO.setValorAnterior(categoriasAnteriores);
                        campoDTO.setValorNuevo(categoriasNuevas);
                        cambiosCampo.put("categorias",campoDTO);
                    }
                }
                hDTO.setCambios(cambiosCampo);
                historial.add(hDTO);
                tareaPrevia=tareaActual;
            }

        }catch (Exception e){
            throw new RuntimeException("Error al obtener el historial "+e.getMessage());
        }
        return historial;
    }
}
