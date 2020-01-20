package ru.fds.tavrzcms3.dictionary;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum  TypeOfPledgeAgreement implements BasicEnum<String>{
    PERV("перв"),
    POSL("посл");

    private String translate;
    private static final Map<String, TypeOfPledgeAgreement> stringToEnum = Stream.of(values()).collect(Collectors.toMap(Objects::toString, e -> e));

    TypeOfPledgeAgreement(String translate){
        this.translate = translate;
    }

    @Override
    public String getTranslate() {
        return translate;
    }

    public static Optional<TypeOfPledgeAgreement> valueOfString(String name){
        return Optional.ofNullable(stringToEnum.get(name));
    }

    public static class Converter extends EnumConverter<TypeOfPledgeAgreement, String>{
        public Converter(){
            super(TypeOfPledgeAgreement.class);
        }
    }
}
