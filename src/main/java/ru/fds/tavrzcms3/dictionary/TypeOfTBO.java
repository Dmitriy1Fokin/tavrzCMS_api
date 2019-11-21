package ru.fds.tavrzcms3.dictionary;

public enum TypeOfTBO {
    TRANSPORT{
        public String toStringRU(){
            return "транспорт";
        }
    },
    SPARE_PARTS{
        public String toStringRU(){
            return "запчасти";
        }
    },
    CLOTHES{
        public String toStringRU(){
            return "одежда";
        }
    },
    FOOD{
        public String toStringRU(){
            return "продукты питания";
        }
    },
    ALCOHOL{
        public String toStringRU(){
            return "алкоголь";
        }
    },
    PETROCHEMISTRY{
        public String toStringRU(){
            return "нефтехимия";
        }
    },
    METAL_PRODUCTS{
        public String toStringRU(){
            return "металлопродукция";
        }
    },
    BUILDING_MATERIALS{
        public String toStringRU(){
            return "стройматериалы";
        }
    },
    CATTLE{
        public String toStringRU(){
            return "крс";
        }
    },
    SMALL_CATTLE{
        public String toStringRU(){
            return "мрс";
        }
    },
    MEDICINES{
        public String toStringRU(){
            return "медикаменты";
        }
    },
    PLUMBING{
        public String toStringRU(){
            return "сантехника";
        }
    };

    public abstract String toStringRU();
}
