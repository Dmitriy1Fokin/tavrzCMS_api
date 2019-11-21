package ru.fds.tavrzcms3.dictionary;

public enum TypeOfSecurities {
    SHARE_IN_AUTHORIZED_CAPITAL{
        public String toStringRU(){
            return "доли в ук";
        }
    },
    STOCK{
        public String toStringRU(){
            return "акции";
        }
    },
    BILL{
        public String toStringRU(){
            return "вексель";
        }
    },
    SHARES{
        public String toStringRU(){
            return "паи";
        }
    };

    public abstract String toStringRU();
}
