package ru.fds.tavrzcms3.dictionary;

public enum  TypeOfPledgeAgreement implements BasicEnum<String>{
    PERV("перв"),
    POSL("посл");

    private String translate;

    TypeOfPledgeAgreement(String translate){
        this.translate = translate;
    }

    @Override
    public String getTranslate() {
        return translate;
    }

    public static class Converter extends EnumConverter<TypeOfPledgeAgreement, String>{
        public Converter(){
            super(TypeOfPledgeAgreement.class);
        }
    }
}
