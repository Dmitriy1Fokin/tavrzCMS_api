package ru.fds.tavrzcms3.dictionary;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum TypeOfEncumbrance implements BasicEnum<String>{
    PLEDGE_OUR("залог Банка"),
    PLEDGE_OTHER("залог третьих лиц"),
    ARREST("арест"),
    LEASE("аренда"),
    SERVITUDE("сервитут"),
    TRUST_MANAGEMENT("доверительное управление");

    private String translate;
    private static final Map<String, TypeOfEncumbrance> stringToEnum = Stream.of(values()).collect(Collectors.toMap(Objects::toString, e -> e));

    TypeOfEncumbrance(String translate){
        this.translate = translate;
    }

    @Override
    public String getTranslate() {
        return translate;
    }

    public static Optional<TypeOfEncumbrance> valueOfString(String name){
        return Optional.ofNullable(stringToEnum.get(name));
    }

    public static class Converter extends EnumConverter<TypeOfEncumbrance, String>{
        public Converter(){
            super(TypeOfEncumbrance.class);
        }
    }
}
