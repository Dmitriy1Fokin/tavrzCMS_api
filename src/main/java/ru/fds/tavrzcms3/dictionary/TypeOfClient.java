package ru.fds.tavrzcms3.dictionary;

public enum TypeOfClient {
    INDIVIDUAL{
        public String toStringRU(){
            return "фл";
        }
    },
    LEGAL_ENTITY{
        public String toStringRU(){
            return "юл";
        }
    };

    public abstract String toStringRU();
}
