package ru.fds.tavrzcms3.dictionary;

public enum TypeOfPledge implements BasicEnum<String>{
    RETURN("возвратная"),
    LEVER("рычаговая"),
    LIMITING("ограничивающая"),
    INFORMATIONAL("информационная");

    private String translate;

    TypeOfPledge(String translate){
        this.translate = translate;
    }

    @Override
    public String getTranslate() {
        return translate;
    }

    public static class Converter extends EnumConverter<TypeOfPledge, String>{
        public Converter(){
            super(TypeOfPledge.class);
        }
    }
}
