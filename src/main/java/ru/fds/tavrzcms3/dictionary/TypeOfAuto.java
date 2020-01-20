package ru.fds.tavrzcms3.dictionary;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum TypeOfAuto implements BasicEnum<String>{
    BULLDOZER("бульдозер"),
    EXCAVATOR("экскаватор"),
    TRAILER("прицеп"),
    LOADER("погрузчик"),
    CRANE("кран"),
    ROAD_CONSTRUCTION("дорожно-строительная"),
    COMBINE("комбайн"),
    TRACTOR("трактор"),
    PASSENGER("пассажирский транспорт"),
    CARGO("грузовой транспорт"),
    PERSONAL("легковой транспорт"),
    RAILWAY("ж/д транспорт"),
    OTHER("иное");

    private String translate;
    private static final Map<String, TypeOfAuto> stringToEnum = Stream.of(values()).collect(Collectors.toMap(Objects::toString, e -> e));

    TypeOfAuto(String translate){
        this.translate = translate;
    }

    @Override
    public String getTranslate(){
        return translate;
    }

    public static Optional<TypeOfAuto> valueOfString(String name){
        return Optional.ofNullable(stringToEnum.get(name));
    }

    public static class Converter extends EnumConverter<TypeOfAuto, String>{
        public Converter(){
            super(TypeOfAuto.class);
        }
    }
}
