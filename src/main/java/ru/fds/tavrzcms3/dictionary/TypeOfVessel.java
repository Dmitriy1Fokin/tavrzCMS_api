package ru.fds.tavrzcms3.dictionary;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum TypeOfVessel implements BasicEnum<String>{
    GDC("general cargo (gcd)"),
    CON("container ship (con)"),
    LOG("log carrier/timber (log)"),
    R_R("ro-ro (r/r)"),
    B_C("bulk carrier (b/c)"),
    O_O("ore/oil carrier (o/o)"),
    OBO("oil/bulk/ore carrier (obo)"),
    TNP("tanker product (tnp)"),
    TNC("tanker crude (tnc)"),
    TNS("tanker storage (tns)"),
    TNV("tanker vlcc/ulcc (tnv)"),
    CHM("chemical tanker (chm)"),
    GAS("lpg/lng carrier (gas)"),
    OSV("offshore supply vessel (osv)"),
    HVL("heavy lift vessel (hvl)"),
    SRV("survey vessel (srv)"),
    PAS("passenger ship (pas)"),
    RFG("reefer (rfg)"),
    LIV("livestock carrier (liv)"),
    TUG("tug"),
    FSH("fishing trawler (fsh)"),
    DRG("dredger (drg)"),
    M("м"),
    M_SP("м-сп"),
    O("о"),
    R("р"),
    L("л");

    private String translate;
    private static final Map<String, TypeOfVessel> stringToEnum = Stream.of(values()).collect(Collectors.toMap(Objects::toString, e -> e));

    TypeOfVessel(String translate){
        this.translate = translate;
    }

    @Override
    public String getTranslate() {
        return translate;
    }

    public static Optional<TypeOfVessel> valueOfString(String name){
        return Optional.ofNullable(stringToEnum.get(name));
    }

    public static class Converter extends EnumConverter<TypeOfVessel, String>{
        public Converter(){
            super(TypeOfVessel.class);
        }
    }
}
