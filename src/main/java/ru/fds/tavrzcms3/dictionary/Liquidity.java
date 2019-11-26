package ru.fds.tavrzcms3.dictionary;

public enum Liquidity implements BasicEnum<String>{
    HIGH("высокая"),
    AVERAGE("средняя"),
    BELOW_AVERAGE("ниже средней"),
    LOW("низкая");

    private String translate;

    Liquidity(String translate){
        this.translate = translate;
    }

    @Override
    public String getTranslate() {
        return translate;
    }

    public static class Converter extends EnumConverter<Liquidity, String>{
        public Converter(){
            super(Liquidity.class);
        }
    }
}
