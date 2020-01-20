package ru.fds.tavrzcms3.dictionary;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum TypeOfMonitoring implements BasicEnum<String> {
    DOCUMENTARY("документарный"),
    VISUAL("визуальный");

    private String translate;
    private static final Map<String, TypeOfMonitoring> stringToEnum = Stream.of(values()).collect(Collectors.toMap(Objects::toString, e -> e));

    TypeOfMonitoring(String translate){
        this.translate = translate;
    }

    @Override
    public String getTranslate() {
        return translate;
    }

    public static Optional<TypeOfMonitoring> valueOfString(String name){
        return Optional.ofNullable(stringToEnum.get(name));
    }

    public static class Converter extends EnumConverter<TypeOfMonitoring, String>{
        public Converter(){
            super(TypeOfMonitoring.class);
        }
    }
}
