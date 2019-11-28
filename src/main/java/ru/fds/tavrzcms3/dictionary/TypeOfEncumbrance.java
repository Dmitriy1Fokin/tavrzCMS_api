package ru.fds.tavrzcms3.dictionary;

public enum TypeOfEncumbrance implements BasicEnum<String>{
    PLEDGE("залог"),
    ARREST("арест"),
    LEASE("аренда"),
    SERVITUDE("сервитут"),
    TRUST_MANAGEMENT("доверительное управление");

    private String translate;

    TypeOfEncumbrance(String translate){
        this.translate = translate;
    }

    @Override
    public String getTranslate() {
        return translate;
    }

    public static class Converter extends EnumConverter<TypeOfEncumbrance, String>{
        public Converter(){
            super(TypeOfEncumbrance.class);
        }
    }
}
