package ru.fds.tavrzcms3.dictionary;

public enum LandCategory implements BasicEnum<String>{

    LOCALITY("земли населенных пунктов"),
    AGRICULTURAL("земли сельскохозяйственного назначения"),
    INDUSTRIAL("земли промышленности, энергетики, транспорта, связи, радиовещания, телевидения, информатики, земли для обеспечения космической деятельности, земли обороны, безопасности и земли иного специального назначения"),
    WATER("земли водного фонда"),
    FOREST("земли лесного фонда"),
    STOCK_LANDS("земли запаса"),
    SPECIALLY_PROTECTED("земли особо охраняемых территорий и объектов");

    private String translate;

    LandCategory(String translate){
        this.translate = translate;
    }

    @Override
    public String getTranslate(){
        return translate;
    }

    public static class Converter extends EnumConverter<LandCategory, String>{
        public Converter(){
            super(LandCategory.class);
        }
    }
}
