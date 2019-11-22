package ru.fds.tavrzcms3.dictionary;

import javax.persistence.AttributeConverter;

public abstract class EnumConverter<T extends Enum<T> & BasicEnum<E>, E> implements AttributeConverter<T, E> {
    private final Class<T> clazz;

    public EnumConverter(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public E convertToDatabaseColumn(T attribute) {

        if(attribute == null){
            throw new NullPointerException();
        }
        return attribute.getTranslate();
    }

    @Override
    public T convertToEntityAttribute(E dbData) {
        if(dbData == null){
            throw new NullPointerException();
        }

        T[] enums = clazz.getEnumConstants();

        for (T e : enums) {
            if (e.getTranslate().equals(dbData)) {
                return e;
            }
        }

        throw new UnsupportedOperationException();
    }
}
