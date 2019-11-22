package ru.fds.tavrzcms3.dictionary;

public enum  TypeOfLiquidity implements BasicEnum<String>{
    HIGH("высокая"),
    AVERAGE("средняя"),
    BELOW_AVERAGE("ниже средней"),
    LOW("низкая");

    private String translate;

    TypeOfLiquidity(String translate){
        this.translate = translate;
    }

    @Override
    public String getTranslate() {
        return translate;
    }

    public static class Converter extends EnumConverter<TypeOfLiquidity, String>{
        public Converter(){
            super(TypeOfLiquidity.class);
        }
    }
}
