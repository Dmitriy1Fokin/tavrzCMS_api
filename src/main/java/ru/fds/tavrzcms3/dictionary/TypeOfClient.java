package ru.fds.tavrzcms3.dictionary;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum TypeOfClient implements BasicEnum<String>{
    INDIVIDUAL("фл"),
    LEGAL_ENTITY("юл");

    private String translate;
    private static final Map<String, TypeOfClient> stringToEnum = Stream.of(values()).collect(Collectors.toMap(Objects::toString, e -> e));

    TypeOfClient(String translate){
        this.translate = translate;
    }

    @Override
    public String getTranslate(){
        return translate;
    }

    public static Optional<TypeOfClient> valueOfString(String name){
        return Optional.ofNullable(stringToEnum.get(name));
    }

    public static class Converter extends EnumConverter<TypeOfClient, String>{
        public Converter(){
            super(TypeOfClient.class);
        }
    }
}
