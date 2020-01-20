package ru.fds.tavrzcms3.dictionary;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum TypeOfEquipment implements BasicEnum<String>{

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
    private static final Map<String, TypeOfEquipment> stringToEnum = Stream.of(values()).collect(Collectors.toMap(Objects::toString, e -> e));

    TypeOfEquipment(String translate){
        this.translate = translate;
    }

    @Override
    public String getTranslate() {
        return translate;
    }

    public static Optional<TypeOfEquipment> valueOfString(String name){
        return Optional.ofNullable(stringToEnum.get(name));
    }

    public static class Converter extends EnumConverter<TypeOfEquipment, String>{
        public Converter(){
            super(TypeOfEquipment.class);
        }
    }
}
