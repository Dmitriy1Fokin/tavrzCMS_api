package ru.fds.tavrzcms3.dictionary;

public enum StatusOfAgreement {
    OPEN{
        public String toStringRU(){
            return "открыт";
        }
    },
    CLOSED{
        public String toStringRU(){
            return "закрыт";
        }
    };

    public abstract String toStringRU();
}
