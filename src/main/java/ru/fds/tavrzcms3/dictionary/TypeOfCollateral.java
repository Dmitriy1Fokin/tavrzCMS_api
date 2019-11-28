package ru.fds.tavrzcms3.dictionary;

public enum TypeOfCollateral implements BasicEnum<String>{
    AUTO("Авто/спецтехника"),
    EQUIPMENT("Оборудование"),
    TBO("ТМЦ"),
    SECURITIES("Ценные бумаги"),
    LAND_OWNERSHIP("Недвижимость - ЗУ - собственность"),
    LAND_LEASE("Недвижимость - ЗУ - право аренды"),
    BUILDING("Недвижимость - здание/сооружение"),
    PREMISE("Недвижимость - помещение"),
    VESSEL("Судно");

    private String translate;

    TypeOfCollateral(String translate){
        this.translate = translate;
    }

    @Override
    public String getTranslate() {
        return translate;
    }

    public static class Converter extends EnumConverter<TypeOfCollateral, String>{
        public Converter(){
            super(TypeOfCollateral.class);
        }
    }

}
