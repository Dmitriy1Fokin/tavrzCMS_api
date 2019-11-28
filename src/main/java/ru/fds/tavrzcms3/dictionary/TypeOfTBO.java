package ru.fds.tavrzcms3.dictionary;

public enum TypeOfTBO implements BasicEnum<String>{
    TRANSPORT("транспорт"),
    SPARE_PARTS("запчасти"),
    CLOTHES("одежда"),
    FOOD("продукты питания"),
    ALCOHOL("алкоголь"),
    PETROCHEMISTRY("нефтехимия"),
    METAL_PRODUCTS("металлопродукция"),
    BUILDING_MATERIALS("стройматериалы"),
    CATTLE("крс"),
    SMALL_CATTLE("мрс"),
    MEDICINES("медикаменты"),
    PLUMBING("сантехника");

    private String translate;

    TypeOfTBO(String translate){
        this.translate = translate;
    }

    @Override
    public String getTranslate() {
        return translate;
    }

    public static class Converter extends EnumConverter<TypeOfTBO, String>{
        public Converter(){
            super(TypeOfTBO.class);
        }
    }
}
