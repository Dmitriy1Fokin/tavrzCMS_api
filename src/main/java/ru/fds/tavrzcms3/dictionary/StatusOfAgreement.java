package ru.fds.tavrzcms3.dictionary;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum StatusOfAgreement implements BasicEnum<String>{
    OPEN("открыт"),
    CLOSED("закрыт");

    private String translate;
    private static final Map<String, StatusOfAgreement> stringToEnum = Stream.of(values()).collect(Collectors.toMap(Objects::toString, e -> e));

    StatusOfAgreement(String translate){
        this.translate = translate;
    }

    @Override
    public String getTranslate() {
        return translate;
    }

    public static Optional<StatusOfAgreement> valueOfString(String name){
        return Optional.ofNullable(stringToEnum.get(name));
    }

    public static class Converter extends EnumConverter<StatusOfAgreement, String>{
        public Converter(){
            super(StatusOfAgreement.class);
        }
    }
}
