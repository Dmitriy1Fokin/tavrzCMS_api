package ru.fds.tavrzcms3.dictionary;

public enum TypeOfEquipment {

    METALWORKING{
        public String toStringRU(){
            return "металлообработка";
        }
    },
    FOREST_PROCCESSING{
        public String toStringRU(){
            return "лесообработка";
        }
    },
    TRADING{
        public String toStringRU(){
            return "торговое";
        }
    },
    OFFICE{
        public String toStringRU(){
            return "офисное";
        }
    },
    ENGINEERING_NETWORKS{
        public String toStringRU(){
            return "сети ито";
        }
    },
    ADVERTISIING{
        public String toStringRU(){
            return "рекламное";
        }
    },
    FOOD{
        public String toStringRU(){
            return "пищевое";
        }
    },
    AUTOMOBILE{
        public String toStringRU(){
            return "автомобильное";
        }
    },
    GAS_STATION{
        public String toStringRU(){
            return "азс";
        }
    },
    CHEMICAL{
        public String toStringRU(){
            return "химическое";
        }
    },
    METRICAL{
        public String toStringRU(){
            return "измерительное";
        }
    },
    MEDICAL{
        public String toStringRU(){
            return "медицинское";
        }
    },
    OIL_GAS{
        public String toStringRU(){
            return "нефте-газовое";
        }
    },
    MINING{
        public String toStringRU(){
            return "карьерное и горное";
        }
    },
    LIFTING{
        public String toStringRU(){
            return "подъемное";
        }
    },
    AVIATION{
        public String toStringRU(){
            return "авиационное";
        }
    },
    BUILDING{
        public String toStringRU(){
            return "строительое";
        }
    },
    RESTAURANT{
        public String toStringRU(){
            return "ресторанное";
        }
    },
    TRANSPORTATION{
        public String toStringRU(){
            return "транспортировка";
        }
    },
    PACKAGING{
        public String toStringRU(){
            return "упаковачное";
        }
    },
    STORAGE{
        public String toStringRU(){
            return "хранение";
        }
    },
    AGRICULTURAL{
        public String toStringRU(){
            return "с/х назначения";
        }
    },
    OTHER{
        public String toStringRU(){
            return "иное";
        }
    };

    public abstract String toStringRU();
}
