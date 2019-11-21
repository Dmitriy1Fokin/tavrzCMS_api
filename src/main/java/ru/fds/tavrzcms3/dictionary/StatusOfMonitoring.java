package ru.fds.tavrzcms3.dictionary;

public enum StatusOfMonitoring {
    IN_STOCK{
        public String toStringRU(){
            return "в наличии";
        }
    },
    LOSING{
        public String toStringRU(){
            return "утрата";
        }
    },
    PARTIAL_LOSS{
        public String toStringRU(){
            return "частичная утрата";
        }
    };

    public abstract String toStringRU();
}
