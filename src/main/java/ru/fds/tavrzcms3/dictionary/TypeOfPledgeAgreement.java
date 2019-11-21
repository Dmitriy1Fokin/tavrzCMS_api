package ru.fds.tavrzcms3.dictionary;

public enum  TypeOfPledgeAgreement {
    PERV{
        public String toStringRU(){
            return "перв";
        }
    },
    POSL{
        public String toStringRU(){
            return "посл";
        }
    };

    public abstract String toStringRU();
}
