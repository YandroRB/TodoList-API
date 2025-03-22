package com.todolist.todo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.todolist.todo.enumerator.EstadoTarea;
import com.todolist.todo.enumerator.PrioridadTarea;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Audited
@NoArgsConstructor
@EqualsAndHashCode(of={"identificador","titulo","descripcion","estado","fechaLimite","recordatorioActivado","diasAnticipadosRecordatorio"})
public class Tarea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long identificador;
    @NotBlank
    private String titulo;
    @NotBlank
    private String descripcion;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTarea estado;
    @Column
    private LocalDateTime fechaLimite;
    @Column(nullable = false)
    private boolean recordatorioActivado=false;
    @Column(nullable = false)
    private Integer diasAnticipadosRecordatorio=1;
    @ManyToOne()
    @NotAudited
    //@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @JoinColumn(name="usuario_id")
    private Usuario usuario;

    @ManyToMany
    @NotAudited
    @JoinTable(
            name = "tarea_usuario_compartida",
            joinColumns = @JoinColumn(name = "tarea_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private Set<Usuario> usuariosCompartidos=new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrioridadTarea prioridad;
    @Column(nullable = false)
    private int prioridadValor;

    @Column(nullable = false)
    private Integer nivel=0;
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "tarea_padre_id")
    private List<Tarea> subTarea=new ArrayList<>();


    public void setPrioridadTarea(PrioridadTarea prioridad) {
        this.prioridad = prioridad;
        this.prioridadValor=prioridad.getValor();
    }

    @ManyToMany
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @JoinTable(
            name = "categorias_tareas",
            joinColumns = @JoinColumn(name = "tarea_id"),
            inverseJoinColumns = @JoinColumn(name="categoria_id")
    )
    private Set<Categoria> categorias=new HashSet<>();
    @PrePersist
    protected void onCreate() {
        if(estado==null)estado=EstadoTarea.PENDIENTE;
        if(prioridad==null){
            prioridad=PrioridadTarea.MEDIA;
            prioridadValor=PrioridadTarea.MEDIA.getValor();
        }
    }

    public void compartirCon(Usuario usuario){
        usuariosCompartidos.add(usuario);
    }

    public void dejarDeCompartirCon(Usuario usuario){
        usuariosCompartidos.remove(usuario);
    }

    public boolean esCompartida(Usuario usuario){
        return usuariosCompartidos.contains(usuario);
    }

    public void addCategoria(Categoria categoria) {
        categorias.add(categoria);
    }
    public void removeCategoria(Categoria categoria) {
        categorias.remove(categoria);
    }
    public void agregarSubTarea(Tarea subTarea) {
        if(this.esSubTarea()){
            throw new RuntimeException("No se puede agregar subtareas a una subtarea");
        }
        subTarea.setNivel(this.nivel+1);
        this.subTarea.add(subTarea);
    }
    public boolean esSubTarea(){
        return this.nivel>=1;
    }
}
