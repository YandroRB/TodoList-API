package com.todolist.todo.repository;

import com.todolist.todo.model.Tarea;
import com.todolist.todo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TareaRepository extends JpaRepository<Tarea,Long> {
    @Query("SELECT t FROM Tarea t WHERE t.nivel=0")
    List<Tarea> findAllMainTareas();
    List<Tarea> findByUsuarioUsername(String username);
    boolean existsByIdentificadorAndUsuarioUsername(Long identificador, String username);
    List<Tarea> findByCategoriasNombre(String nombre);
    List<Tarea> findByCategoriasNombreLike(String nombre);
    @Query("SELECT t FROM Tarea t where t.nivel=0 order by t.prioridadValor asc")
    List<Tarea> findAllMainTareaPrioridadAsc();
    @Query("SELECT t FROM Tarea t where t.nivel=0 order by t.prioridadValor desc")
    List<Tarea> findAllMainTareaPrioridadDesc();

    List<Tarea> findByUsuarioUsernameOrderByPrioridadValorAsc(String username);
    List<Tarea> findByUsuarioUsernameOrderByPrioridadValorDesc(String username);

    Optional<Tarea> findByIdentificadorAndSubTareaIdentificador(Long identificador, Long subTarea);
    boolean existsByIdentificadorAndCategoriasIdentificadorAndUsuarioUsername(Long identificador, Long categoriasIdentificador, String username);

    @Query("SELECT t FROM Tarea t JOIN t.usuariosCompartidos u WHERE u=:usuario")
    List<Tarea> findTareasCompartidasConUsuario(@Param("usuario") Usuario usuario);

    @Query("SELECT t FROM Tarea t WHERE t.usuario=:usuario OR :usuario MEMBER OF t.usuariosCompartidos")
    List<Tarea> findTareasAccesibles(@Param("usuario") Usuario usuario);


    @Query("SELECT t FROM Tarea t where t.fechaLimite is not null " +
            "AND t.fechaLimite between :inicio AND :fin " +
            "AND (t.estado ='EN_PROGRESO' OR t.estado = 'PENDIENTE')")
    List<Tarea> findByEntreFechaLimiteYEstado(@Param("inicio") LocalDateTime inicio,
                                              @Param("fin") LocalDateTime fin);

    @Query("SELECT t from Tarea t where t.usuario.username = :username " +
            "AND t.fechaLimite is not null " +
            "AND t.fechaLimite BETWEEN :inicio AND :fin " +
            "AND (t.estado = 'PENDIENTE' OR t.estado='EN_PROGRESO')")
    List<Tarea> findTareasAVencerPorUsuario(@Param("username") String username,
                                            @Param("inicio")LocalDateTime inicio,
                                            @Param("fin") LocalDateTime fin);
    @Query(value = "SELECT t FROM Tarea t where t.recordatorioActivado = true " +
            "AND t.fechaLimite is not null " +
            "AND (t.estado ='PENDIENTE' or  t.estado='EN_PROGRESO') " +
            "AND FUNCTION('timestampdiff',day ,:ahora,t.fechaLimite)<= t.diasAnticipadosRecordatorio " +
            "AND FUNCTION('timestampdiff',day,:ahora,t.fechaLimite)>0")
    List<Tarea> findTareasNecesitanRecordatorio(@Param("ahora") LocalDateTime ahora);

}
