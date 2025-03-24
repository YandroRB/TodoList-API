package com.todolist.todo.service;

import com.todolist.todo.enumerator.EstadoTarea;
import com.todolist.todo.enumerator.PrioridadTarea;
import com.todolist.todo.exception.EntradaInvalidaException;
import com.todolist.todo.exception.RecursoNoEncontradoException;
import com.todolist.todo.model.Tarea;
import com.todolist.todo.model.Usuario;
import com.todolist.todo.repository.TareaRepository;
import com.todolist.todo.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TareaService {
    private final TareaRepository tareaRepository;
    private final UsuarioRepository usuarioRepository;

    public TareaService(TareaRepository tareaRepository, UsuarioRepository usuarioRepository) {
        this.tareaRepository = tareaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Tarea> obtenerTodasTareasOrderBy(boolean esAsc){
        if(esAsc){
            return tareaRepository.findAllMainTareaPrioridadAsc();
        }
        return tareaRepository.findAllMainTareaPrioridadDesc();
    }

    public List<Tarea> obtenerTareasOrderBy(boolean esAsc,String username){
        Usuario usuario=usuarioRepository.findByUsername(username).orElseThrow(()->new RecursoNoEncontradoException("No se encontró el usuario"));
        if(esAsc){
            return tareaRepository.findTareasAccesiblesAsc(usuario);
        }
        return tareaRepository.findTareasAccesiblesDesc(usuario);
    }

    public Tarea establecerPrioridad(Long id, PrioridadTarea prioridad){
        return tareaRepository.findById(id).
                map(tarea->{
                    if(prioridad==null) throw new EntradaInvalidaException("Prioridad no encontrada, prioridades validas "+Arrays.toString(PrioridadTarea.values()));
                    tarea.setPrioridad(prioridad);
                    return tareaRepository.save(tarea);
                }).
                orElseThrow(()->new RecursoNoEncontradoException("No se ha encontrado la tarea con el id: "+id));
    }

    public List<Tarea> obtenerTodasTareas() {
        return tareaRepository.findAllMainTareas();
    }

    public List<Tarea> obtenerTareas(String username){
        Usuario usuario=usuarioRepository.findByUsername(username).orElseThrow(()->new RuntimeException("Usuario no encontrado"));
        return tareaRepository.findTareasAccesibles(usuario);
    }

    public Tarea obtenerTarea(Long id){
        return tareaRepository.findById(id).orElseThrow(()->new RecursoNoEncontradoException("No se encontró la tarea con el id "+id));
    }


    public Tarea guardarTarea(String username,Tarea tarea){
        Optional<Usuario> usuario=usuarioRepository.findByUsername(username);
        tarea.setUsuario(usuario.orElse(null));
        return tareaRepository.save(tarea);
    }

    public void eliminarTarea(Long id){
        if(!tareaRepository.existsById(id)) throw new RecursoNoEncontradoException("No se encontro la tarea con el id "+id);
        tareaRepository.deleteById(id);
    }

    public Tarea compartirTarea(Long tareaId, Long usuarioId){
        Tarea tarea=tareaRepository.findById(tareaId).orElseThrow(()->new RecursoNoEncontradoException("Tarea con el id "+tareaId+" no encontrada"));
        Usuario usuario=usuarioRepository.findById(usuarioId).orElseThrow(()->new RecursoNoEncontradoException("Usuario con el id "+usuarioId+" no encontrado"));
        if(tarea.getUsuario()==null) throw new RuntimeException("No se puede compartir una sub tarea");
        if(tarea.getUsuario().equals(usuario)) throw new RuntimeException("No puedes compartir contigo mismo");
        tarea.compartirCon(usuario);
        return tareaRepository.save(tarea);
    }

    public Tarea dejarCompartir(Long tareaId,Long usuarioId){
        Tarea tarea=tareaRepository.findById(tareaId).orElseThrow(()->new RecursoNoEncontradoException("Tarea con el id "+tareaId+" no encontrada"));
        Usuario usuario=usuarioRepository.findById(usuarioId).orElseThrow(()->new RecursoNoEncontradoException("Usuario con el id "+usuarioId+" no encontrado"));
        if(!tarea.esCompartida(usuario)) throw new RuntimeException("Esta tarea no esta compartida con este usuario");
        tarea.dejarDeCompartirCon(usuario);
        return tareaRepository.save(tarea);
    }

    public List<Tarea> obtenerTareasCompartidas(String username){
        Usuario usuario=usuarioRepository.findByUsername(username).orElseThrow(()->new RecursoNoEncontradoException("Usuario no encontrado"));
        return tareaRepository.findTareasCompartidasConUsuario(usuario);
    }


    public Tarea guardarSubTarea(Long idTarea,Tarea subTarea){
        Tarea tareaPadre=tareaRepository.findById(idTarea).orElseThrow(()->new RecursoNoEncontradoException("No se encontro la tarea con el id "+idTarea));
        tareaPadre.agregarSubTarea(subTarea);
        return tareaRepository.save(tareaPadre);
    }

    public Tarea actualizarSubTarea(Long idTarea,Tarea subTarea){
       return tareaRepository.findByIdentificadorAndSubTareaIdentificador(idTarea,subTarea.getIdentificador()).
        map(tarea->{
            for(Tarea t:tarea.getSubTarea()){
                if (t.getIdentificador().equals(subTarea.getIdentificador())){
                    t.setTitulo(subTarea.getTitulo()!=null?subTarea.getTitulo():t.getTitulo());
                    t.setDescripcion(subTarea.getDescripcion()!=null?subTarea.getDescripcion():t.getDescripcion());
                    t.setEstado(subTarea.getEstado()!=null?subTarea.getEstado():t.getEstado());
                    t.setPrioridad(subTarea.getPrioridad()!=null?subTarea.getPrioridad():t.getPrioridad());
                    break;
                }
            }
            return tareaRepository.save(tarea);
        }).orElseThrow(()->new RecursoNoEncontradoException("No se encontró la tarea"));
    }

    public void eliminarsubTarea(Long idTarea, Long idSubTarea){
       tareaRepository.findByIdentificadorAndSubTareaIdentificador(idTarea,idSubTarea).
                map(tarea -> {
                    Tarea subTarea=tarea.getSubTarea().stream().filter(t->t.getIdentificador().equals(idSubTarea)).findFirst().
                            orElseThrow(()->new RecursoNoEncontradoException("No se encontro la subtarea con el id "+idSubTarea));
                    tarea.getSubTarea().remove(subTarea);
                    return tareaRepository.save(tarea);
                }).
                orElseThrow(()->new RecursoNoEncontradoException("No se encontro la tarea"));
    }

    public Tarea actualizarTarea(Long id,Tarea tarea){
        return tareaRepository.findById(id).map(_tarea->{
            _tarea.setTitulo(tarea.getTitulo()==null? _tarea.getTitulo():tarea.getTitulo());
            _tarea.setDescripcion(tarea.getDescripcion()==null? _tarea.getDescripcion():tarea.getDescripcion());
            _tarea.setEstado(tarea.getEstado()==null?_tarea.getEstado():tarea.getEstado());
            _tarea.setPrioridad(tarea.getPrioridad()==null?_tarea.getPrioridad():tarea.getPrioridad());
            return tareaRepository.save(_tarea);
        }).orElseThrow(()-> new RecursoNoEncontradoException("No se encontro la tarea con el id "+id+" para actualizar los datos") );
    }

    public Tarea actualizarEstado(Long id, EstadoTarea estado){
        Optional<Tarea> tarea = tareaRepository.findById(id);
        return tarea.map(_tarea->{
            if(estado==null)  throw new EntradaInvalidaException("Estado invalida. Estados permitidos: "+ Arrays.toString(EstadoTarea.values()));
            _tarea.setEstado(estado);
            return tareaRepository.save(_tarea);
        }).orElseThrow(()->new RecursoNoEncontradoException("No se encontro la tarea con el id "+id+" para actualizar el estado"));
    }

    public Tarea establecerFechaLimite(Tarea tarea){
        if(tarea.getFechaLimite().isBefore(LocalDateTime.now())) throw new RuntimeException("La fecha limite está antes que la actual");
        return tareaRepository.findById(tarea.getIdentificador()).map(_tarea->{
            _tarea.setRecordatorioActivado(tarea.isRecordatorioActivado());
            _tarea.setFechaLimite(tarea.getFechaLimite());
            _tarea.setDiasAnticipadosRecordatorio(tarea.getDiasAnticipadosRecordatorio()!=null?tarea.getDiasAnticipadosRecordatorio(): _tarea.getDiasAnticipadosRecordatorio());
            return tareaRepository.save(_tarea);
        }).orElseThrow(()->new RecursoNoEncontradoException("No se encontro la tarea"));
    }

    public List<Tarea> obtenerTareasPorVencer(Integer dias){
        LocalDateTime ahora=LocalDateTime.now();
        LocalDateTime limite=ahora.plusDays(dias);
        return tareaRepository.findByEntreFechaLimiteYEstado(ahora,limite);
    }

    public List<Tarea> obtenerTareasPorVencerPorUsuario(String username, int dias){
        LocalDateTime ahora=LocalDateTime.now();
        LocalDateTime limite=ahora.plusDays(dias);
        Usuario usuario=usuarioRepository.findByUsername(username).orElseThrow(()->new RecursoNoEncontradoException("No se encontro la tarea"));
        return tareaRepository.findTareasAVencerPorUsuario(usuario,ahora,limite);
    }

    public boolean existeTarea(Long id){
        return tareaRepository.existsById(id);
    }
}
