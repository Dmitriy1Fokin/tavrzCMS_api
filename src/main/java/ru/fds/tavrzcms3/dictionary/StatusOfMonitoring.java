package ru.fds.tavrzcms3.dictionary;

public enum StatusOfMonitoring implements BasicEnum<String>{
    IN_STOCK("в наличии"),
    LOSING("утрата"),
    PARTIAL_LOSS("частичная утрата");

    private String translate;

    StatusOfMonitoring(String translate){
        this.translate = translate;
    }

    @Override
    public String getTranslate(){
        return translate;
    }

    public static class Converter extends EnumConverter<StatusOfMonitoring, String>{
        public Converter(){
            super(StatusOfMonitoring.class);
        }
    }
}
