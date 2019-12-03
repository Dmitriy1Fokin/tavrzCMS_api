package ru.fds.tavrzcms3.mapper;

import java.util.ArrayList;
import java.util.List;

public interface Mapper<E, D> {

    E toEntity(D dto);
    D toDto(E entity);

    default List<E> toEntity(List<D> dtoList){
        List<E> eList = new ArrayList<>();
        for(D d : dtoList)
            eList.add(toEntity(d));
        return eList;
    }

    default List<D> toDto(List<E> entityList){
        List<D> dList = new ArrayList<>();
        for(E e : entityList)
            dList.add(toDto(e));
        return dList;
    }
}
