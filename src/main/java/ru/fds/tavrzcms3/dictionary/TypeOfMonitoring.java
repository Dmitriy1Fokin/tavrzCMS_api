package ru.fds.tavrzcms3.dictionary;

public enum TypeOfMonitoring implements BasicEnum<String>{
    DOCUMENTARY("документарный"),
    VISUAL("визуальный");

    private String translate;

    TypeOfMonitoring(String translate){
        this.translate = translate;
    }

    @Override
    public String getTranslate() {
        return translate;
    }

    public static class Converter extends EnumConverter<TypeOfMonitoring, String>{
        public Converter(){
            super(TypeOfMonitoring.class);
        }
    }
}
