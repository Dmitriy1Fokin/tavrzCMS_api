package ru.fds.tavrzcms3.dictionary;

public enum TypeOfClient implements BasicEnum<String>{
    INDIVIDUAL("фл"),
    LEGAL_ENTITY("юл");

    private String translate;

    TypeOfClient(String translate){
        this.translate = translate;
    }

    @Override
    public String getTranslate(){
        return translate;
    }

    public static class Converter extends EnumConverter<TypeOfClient, String>{
        public Converter(){
            super(TypeOfClient.class);
        }
    }
}
