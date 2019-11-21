package ru.fds.tavrzcms3.dictionary;

public enum TypeOfMonitoring {
    DOCUMENTARY{
        public String toStringRU(){
            return "документарный";
        }
    },
    VISUAL{
        public String toStringRU(){
            return "визуальный";
        }
    };

    public abstract String toStringRU();
}
