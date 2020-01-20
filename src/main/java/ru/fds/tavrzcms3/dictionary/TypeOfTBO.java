package ru.fds.tavrzcms3.dictionary;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum TypeOfTBO implements BasicEnum<String>{
    TRANSPORT("транспорт"),
    SPARE_PARTS("запчасти"),
    CLOTHES("одежда"),
    FOOD("продукты питания"),
    ALCOHOL("алкоголь"),
    PETROCHEMISTRY("нефтехимия"),
    METAL_PRODUCTS("металлопродукция"),
    BUILDING_MATERIALS("стройматериалы"),
    CATTLE("крс"),
    SMALL_CATTLE("мрс"),
    MEDICINES("медикаменты"),
    PLUMBING("сантехника");

    private String translate;
    private static final Map<String, TypeOfTBO> stringToEnum = Stream.of(values()).collect(Collectors.toMap(Objects::toString, e -> e));

    TypeOfTBO(String translate){
        this.translate = translate;
    }

    @Override
    public String getTranslate() {
        return translate;
    }

    public static Optional<TypeOfTBO> valueOfString(String name){
        return Optional.ofNullable(stringToEnum.get(name));
    }

    public static class Converter extends EnumConverter<TypeOfTBO, String>{
        public Converter(){
            super(TypeOfTBO.class);
        }
    }
}
