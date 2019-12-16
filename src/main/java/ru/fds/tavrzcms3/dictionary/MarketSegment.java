package ru.fds.tavrzcms3.dictionary;

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

    MarketSegment(String translate){
        this.translate = translate;
    }

    @Override
    public String getTranslate(){
        return translate;
    }

    public static class Converter extends EnumConverter<MarketSegment, String>{
        public Converter(){
            super(MarketSegment.class);
        }
    }
}
