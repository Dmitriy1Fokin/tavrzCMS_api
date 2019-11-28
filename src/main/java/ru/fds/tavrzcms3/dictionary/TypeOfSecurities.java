package ru.fds.tavrzcms3.dictionary;


public enum TypeOfSecurities implements BasicEnum<String>{
    SHARE_IN_AUTHORIZED_CAPITAL("доли в ук"),
    STOCK("акции"),
    BILL("вексель"),
    SHARES("паи");

    private String translate;

    TypeOfSecurities(String translate){
        this.translate = translate;
    }

    @Override
    public String getTranslate() {
        return translate;
    }

    public static class Converter extends EnumConverter<TypeOfSecurities, String>{
        public Converter(){
            super(TypeOfSecurities.class);
        }
    }
}
