package ru.fds.tavrzcms3.dictionary;

public enum TypeOfEncumbrance {
    PLEDGE{
        public String toStringRU(){
            return "залог";
        }
    },
    ARREST{
        public String toStringRU(){
            return "арест";
        }
    },
    LEASE{
        public String toStringRU(){
            return "аренда";
        }
    },
    SERVITUDE{
        public String toStringRU(){
            return "сервитут";
        }
    },
    TRUST_MANAGEMENT{
        public String toStringRU(){
            return "доверительное управление";
        }
    };

    public abstract String toStringRU();
}
