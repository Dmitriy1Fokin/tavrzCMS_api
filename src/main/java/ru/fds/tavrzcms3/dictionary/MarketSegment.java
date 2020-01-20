package ru.fds.tavrzcms3.dictionary;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum MarketSegment implements BasicEnum<String>{

    OFFICE("Офисное"),
    TRADING("Торговое"),
    TRADING_OFFICE("Торгово-Офисное"),
    INDUSTRIAL("Производственное"),
    WAREHOUSE("Складское"),
    INDUSTRIAL_WAREHOUSE("Производственно-складсткое"),
    FLAT("Квартира"),
    HOUSE("Жилой дом"),
    SUBSIDIARY("Вспомогательное"),
    ENGINEERING("Инженерное"),
    HOTEL("Гостиничное"),
    RECREATIONAL("Рекреационное"),
    AGRICULTURAL("с/х");

    private String translate;
    private static final Map<String, MarketSegment> stringToEnum = Stream.of(values()).collect(Collectors.toMap(Objects::toString, e -> e));

    MarketSegment(String translate){
        this.translate = translate;
    }

    @Override
    public String getTranslate(){
        return translate;
    }

    public static Optional<MarketSegment> valueOfString(String name){
        return Optional.ofNullable(stringToEnum.get(name));
    }

    public static class Converter extends EnumConverter<MarketSegment, String>{
        public Converter(){
            super(MarketSegment.class);
        }
    }
}
