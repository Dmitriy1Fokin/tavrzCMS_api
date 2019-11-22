package ru.fds.tavrzcms3.dictionary;

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

    TypeOfAuto(String translate){
        this.translate = translate;
    }

    @Override
    public String getTranslate(){
        return translate;
    }

    public static class Converter extends EnumConverter<TypeOfAuto, String>{
        public Converter(){
            super(TypeOfAuto.class);
        }
    }
}
