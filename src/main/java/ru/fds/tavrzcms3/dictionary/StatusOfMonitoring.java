package ru.fds.tavrzcms3.dictionary;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum StatusOfMonitoring implements BasicEnum<String>{
    IN_STOCK("в наличии"),
    LOSING("утрата"),
    PARTIAL_LOSS("частичная утрата");

    private String translate;
    private static final Map<String, StatusOfMonitoring> stringToEnum = Stream.of(values()).collect(Collectors.toMap(Objects::toString, e -> e));

    StatusOfMonitoring(String translate){
        this.translate = translate;
    }

    @Override
    public String getTranslate(){
        return translate;
    }

    public static Optional<StatusOfMonitoring> valueOfString(String name){
        return Optional.ofNullable(stringToEnum.get(name));
    }

    public static class Converter extends EnumConverter<StatusOfMonitoring, String>{
        public Converter(){
            super(StatusOfMonitoring.class);
        }
    }
}
