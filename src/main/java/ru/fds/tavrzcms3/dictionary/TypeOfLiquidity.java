package ru.fds.tavrzcms3.dictionary;

public enum  TypeOfLiquidity {
    HIGH{
        public String toStringRU(){
            return "высокая";
        }
    },
    AVERAGE{
        public String toStringRU(){
            return "средняя";
        }
    },
    BELOW_AVERAGE{
        public String toStringRU(){
            return "ниже средней";
        }
    },
    LOW{
        public String toStringRU(){
            return "низкая";
        }
    };

    public abstract String toStringRU();
}
