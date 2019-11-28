package ru.fds.tavrzcms3.dictionary;

public enum TypeOfEquip implements BasicEnum<String>{

    METALWORKING("металлообработка"),
    FOREST_PROCCESSING("лесообработка"),
    TRADING("торговое"),
    OFFICE("офисное"),
    ENGINEERING_NETWORKS("сети ито"),
    ADVERTISIING("рекламное"),
    FOOD("пищевое"),
    AUTOMOBILE("автомобильное"),
    GAS_STATION("азс"),
    CHEMICAL("химическое"),
    METRICAL("измерительное"),
    MEDICAL("медицинское"),
    OIL_GAS("нефте-газовое"),
    MINING("карьерное и горное"),
    LIFTING("подъемное"),
    AVIATION("авиационное"),
    BUILDING("строительое"),
    RESTAURANT("ресторанное"),
    TRANSPORTATION("транспортировка"),
    PACKAGING("упаковачное"),
    STORAGE("хранение"),
    AGRICULTURAL("с/х назначения"),
    OTHER("иное");

    private String translate;

    TypeOfEquip(String translate){
        this.translate = translate;
    }

    @Override
    public String getTranslate() {
        return translate;
    }

    public static class Converter extends EnumConverter<TypeOfEquip, String>{
        public Converter(){
            super(TypeOfEquip.class);
        }
    }
}
