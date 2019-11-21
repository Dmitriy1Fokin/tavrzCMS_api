package ru.fds.tavrzcms3.dictionary;

public enum TypeOfPledge {
    RETURN{
        public String toStringRU(){
            return "возвратная";
        }
    },
    LEVER{
        public String toStringRU(){
            return "рычаговая";
        }
    },
    LIMITING{
        public String toStringRU(){
            return "ограничивающая";
        }
    },
    INFORMATIONAL{
        public String toStringRU(){
            return "информационная";
        }
    };

    public abstract String toStringRU();
}
