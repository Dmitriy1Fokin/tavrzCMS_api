package ru.fds.tavrzcms3.dictionary;

public enum TypeOfAuto {
    BULLDOZER{
        public String toStringRU(){
            return "бульдозер";
        }
    },
    EXCAVATOR{
        public String toStringRU(){
            return "экскаватор";
        }
    },
    TRAILER{
        public String toStringRU(){
            return "прицеп";
        }
    },
    LOADER{
        public String toStringRU(){
            return "погрузчик";
        }
    },
    CRANE{
        public String toStringRU(){
            return "кран";
        }
    },
    ROAD_CONSTRUCTION {
        public String toStringRU(){
            return "дорожно-строительная";
        }
    },
    COMBINE{
        public String toStringRU(){
            return "комбайн";
        }
    },
    TRACTOR{
        public String toStringRU(){
            return "трактор";
        }
    },
    PASSENGER{
        public String toStringRU(){
            return "пассажирский транспорт";
        }
    },
    CARGO{
        public String toStringRU(){
            return "грузовой транспорт";
        }
    },
    PERSONAL{
        public String toStringRU(){
            return "легковой транспорт";
        }
    },
    RAILWAY{
        public String toStringRU(){
            return "ж/д транспорт";
        }
    },
    OTHER{
        public String toStringRU(){
            return "иное";
        }
    };

    public abstract String toStringRU();
}
