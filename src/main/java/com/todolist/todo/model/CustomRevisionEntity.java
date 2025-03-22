package com.todolist.todo.model;

import com.todolist.todo.listeners.CustomRevisionListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

@EqualsAndHashCode(callSuper = true)
@Entity
@RevisionEntity(CustomRevisionListener.class)
@Table(name = "REVINFO")
@Data
public class CustomRevisionEntity extends DefaultRevisionEntity {
    @Column(name="username")
    private String username;
}
