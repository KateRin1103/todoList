package com.example.todo.mappers;

import java.util.List;

public interface MapperTemplate<T,M> {

    T toDTO(M obj);

    M toModel(T obj);

    List<T> toDTO(List<M> obj);

    List<M> toModel(List<T> obj);
}
