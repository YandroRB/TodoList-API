package com.todolist.todo.enumerator;

public enum PrioridadTarea {
    BAJA(0),
    MEDIA(1),
    ALTA(2),
    URGENTE(3);

    private final int valor;
    PrioridadTarea(int valor){
        this.valor = valor;
    }
    public int getValor(){
        return valor;
    }
}
