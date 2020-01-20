package ru.fds.tavrzcms3.dictionary;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum TypeOfCollateral implements BasicEnum<String>{
    AUTO("Авто/спецтехника"),
    EQUIPMENT("Оборудование"),
    TBO("ТМЦ"),
    SECURITIES("Ценные бумаги"),
    LAND_OWNERSHIP("Недвижимость - ЗУ - собственность"),
    LAND_LEASE("Недвижимость - ЗУ - право аренды"),
    BUILDING("Недвижимость - здание/сооружение"),
    PREMISE("Недвижимость - помещение"),
    VESSEL("Судно");

    private String translate;
    private static final Map<String, TypeOfCollateral> stringToEnum = Stream.of(values()).collect(Collectors.toMap(Objects::toString, e -> e));

    TypeOfCollateral(String translate){
        this.translate = translate;
    }

    @Override
    public String getTranslate() {
        return translate;
    }

    public static Optional<TypeOfCollateral> valueOfString(String name){
        return Optional.ofNullable(stringToEnum.get(name));
    }

    public static class Converter extends EnumConverter<TypeOfCollateral, String>{
        public Converter(){
            super(TypeOfCollateral.class);
        }
    }

}
