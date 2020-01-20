package ru.fds.tavrzcms3.dictionary;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Liquidity implements BasicEnum<String>{
    HIGH("высокая"),
    AVERAGE("средняя"),
    BELOW_AVERAGE("ниже средней"),
    LOW("низкая");

    private String translate;
    private static final Map<String, Liquidity> stringToEnum = Stream.of(values()).collect(Collectors.toMap(Objects::toString, e -> e));

    Liquidity(String translate){
        this.translate = translate;
    }

    @Override
    public String getTranslate() {
        return translate;
    }

    public static Optional<Liquidity> valueOfString(String name){
        return Optional.ofNullable(stringToEnum.get(name));
    }

    public static class Converter extends EnumConverter<Liquidity, String>{
        public Converter(){
            super(Liquidity.class);
        }
    }
}
