package ru.fds.tavrzcms3.dictionary;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class TypeOfAutoConverter implements AttributeConverter<TypeOfAuto, String> {
    @Override
    public String convertToDatabaseColumn(TypeOfAuto typeOfAuto) {
        switch (typeOfAuto){
            case BULLDOZER:
                return "бульдозер";
            case EXCAVATOR:
                return "экскаватор";
            case TRAILER:
                return "прицеп";
            case LOADER:
                return "погрузчик";
            case CRANE:
                return "кран";
            case ROAD_CONSTRUCTION:
                return "дорожно-строительная";
            case COMBINE:
                return "комбайн";
            case TRACTOR:
                return "трактор";
            case PASSENGER:
                return "пассажирский транспорт";
            case CARGO:
                return "грузовой транспорт";
            case PERSONAL:
                return "легковой транспорт";
            case RAILWAY:
                return "ж/д транспорт";
            case OTHER:
                return "иное";
            default:
                throw new IllegalStateException("Неизвестный тип транспорта: " + typeOfAuto);
        }
    }

    @Override
    public TypeOfAuto convertToEntityAttribute(String s) {
        switch (s){
            case "бульдозер":
                return TypeOfAuto.BULLDOZER;
            case "экскаватор":
                return TypeOfAuto.EXCAVATOR;
            case "прицеп":
                return TypeOfAuto.TRAILER;
            case "погрузчик":
                return TypeOfAuto.LOADER;
            case "кран":
                return TypeOfAuto.CRANE;
            case "дорожно-строительная":
                return TypeOfAuto.ROAD_CONSTRUCTION;
            case "комбайн":
                return TypeOfAuto.COMBINE;
            case "трактор":
                return TypeOfAuto.TRACTOR;
            case "пассажирский транспорт":
                return TypeOfAuto.PASSENGER;
            case "грузовой транспорт":
                return TypeOfAuto.CARGO;
            case "легковой транспорт":
                return TypeOfAuto.PERSONAL;
            case "ж/д транспорт":
                return TypeOfAuto.RAILWAY;
            case "иное":
                return TypeOfAuto.OTHER;
            default:
                throw new IllegalStateException("Неизвестный тип транспорта: " + s);
        }
    }
}
