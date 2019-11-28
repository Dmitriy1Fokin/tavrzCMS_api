package ru.fds.tavrzcms3.dictionary;

public enum StatusOfAgreement implements BasicEnum<String>{
    OPEN("открыт"),
    CLOSED("закрыт");

    private String translate;

    StatusOfAgreement(String translate){
        this.translate = translate;
    }

    @Override
    public String getTranslate() {
        return translate;
    }

    public static class Converter extends EnumConverter<StatusOfAgreement, String>{
        public Converter(){
            super(StatusOfAgreement.class);
        }
    }
}
