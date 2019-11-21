package ru.fds.tavrzcms3.dictionary;

public enum TypeOfCollateral {
    AUTO{
        public String toStringRU(){
            return "Авто/спецтехника";
        }
    },
    EQUIPMENT{
        public String toStringRU(){
            return "Оборудование";
        }
    },
    TBO{
        public String toStringRU(){
            return "ТМЦ";
        }
    },
    SECURITIES{
        public String toStringRU(){
            return "Ценные бумаги";
        }
    },
    LAND_OWNERSHIP{
        public String toStringRU(){
            return "Недвижимость - ЗУ - собственность";
        }
    },
    LAND_LEASE{
        public String toStringRU(){
            return "Недвижимость - ЗУ - право аренды";
        }
    },
    BUILDING{
        public String toStringRU(){
            return "Недвижимость - здание/сооружение";
        }
    },
    PREMISE{
        public String toStringRU(){
            return "Недвижимость - помещение";
        }
    },
    VESSEL{
        public String toStringRU(){
            return "Судно";
        }
    };

    public abstract String toStringRU();
}
