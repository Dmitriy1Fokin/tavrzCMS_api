package ru.fds.tavrzcms3.dictionary;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum LandCategory implements BasicEnum<String>{

    LOCALITY("земли населенных пунктов"),
    AGRICULTURAL("земли сельскохозяйственного назначения"),
    INDUSTRIAL("земли промышленности, энергетики, транспорта, связи, радиовещания, телевидения, информатики, земли для обеспечения космической деятельности, земли обороны, безопасности и земли иного специального назначения"),
    WATER("земли водного фонда"),
    FOREST("земли лесного фонда"),
    STOCK_LANDS("земли запаса"),
    SPECIALLY_PROTECTED("земли особо охраняемых территорий и объектов");

    private String translate;
    private static final Map<String, LandCategory> stringToEnum = Stream.of(values()).collect(Collectors.toMap(Objects::toString, e -> e));

    LandCategory(String translate){
        this.translate = translate;
    }

    @Override
    public String getTranslate(){
        return translate;
    }

    public static Optional<LandCategory> valueOfString(String name){
        return Optional.ofNullable(stringToEnum.get(name));
    }

    public static class Converter extends EnumConverter<LandCategory, String>{
        public Converter(){
            super(LandCategory.class);
        }
    }
}
