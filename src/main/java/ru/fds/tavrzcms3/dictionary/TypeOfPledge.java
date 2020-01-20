package ru.fds.tavrzcms3.dictionary;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum TypeOfPledge implements BasicEnum<String>{
    RETURN("возвратная"),
    LEVER("рычаговая"),
    LIMITING("ограничивающая"),
    INFORMATIONAL("информационная");

    private String translate;
    private static final Map<String, TypeOfPledge> stringToEnum = Stream.of(values()).collect(Collectors.toMap(Objects::toString, e -> e));

    TypeOfPledge(String translate){
        this.translate = translate;
    }

    @Override
    public String getTranslate() {
        return translate;
    }

    public static Optional<TypeOfPledge> valueOfString(String name){
        return Optional.ofNullable(stringToEnum.get(name));
    }

    public static class Converter extends EnumConverter<TypeOfPledge, String>{
        public Converter(){
            super(TypeOfPledge.class);
        }
    }
}
