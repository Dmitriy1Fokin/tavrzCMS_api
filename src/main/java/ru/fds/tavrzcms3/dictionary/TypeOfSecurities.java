package ru.fds.tavrzcms3.dictionary;


import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum TypeOfSecurities implements BasicEnum<String>{
    SHARE_IN_AUTHORIZED_CAPITAL("доли в ук"),
    STOCK("акции"),
    BILL("вексель"),
    SHARES("паи");

    private String translate;
    private static final Map<String, TypeOfSecurities> stringToEnum = Stream.of(values()).collect(Collectors.toMap(Objects::toString, e -> e));

    TypeOfSecurities(String translate){
        this.translate = translate;
    }

    @Override
    public String getTranslate() {
        return translate;
    }

    public static Optional<TypeOfSecurities> valueOfString(String name){
        return Optional.ofNullable(stringToEnum.get(name));
    }

    public static class Converter extends EnumConverter<TypeOfSecurities, String>{
        public Converter(){
            super(TypeOfSecurities.class);
        }
    }
}
