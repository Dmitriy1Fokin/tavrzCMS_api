package ru.fds.tavrzcms3.dictionary;

public enum TypeOfVessel {
    GDC{
        public String toStringRU(){
            return "general cargo (gcd)";
        }
    },
    CON{
        public String toStringRU(){
            return "container ship (con)";
        }
    },
    LOG{
        public String toStringRU(){
            return "log carrier/timber (log)";
        }
    },
    R_R{
        public String toStringRU(){
            return "ro-ro (r/r)";
        }
    },
    B_C{
        public String toStringRU(){
            return "bulk carrier (b/c)";
        }
    },
    O_O{
        public String toStringRU(){
            return "ore/oil carrier (o/o)";
        }
    },
    OBO{
        public String toStringRU(){
            return "oil/bulk/ore carrier (obo)";
        }
    },
    TNP{
        public String toStringRU(){
            return "tanker product (tnp)";
        }
    },
    TNC{
        public String toStringRU(){
            return "tanker crude (tnc)";
        }
    },
    TNS{
        public String toStringRU(){
            return "tanker storage (tns)";
        }
    },
    TNV{
        public String toStringRU(){
            return "tanker vlcc/ulcc (tnv)";
        }
    },
    CHM{
        public String toStringRU(){
            return "chemical tanker (chm)";
        }
    },
    GAS{
        public String toStringRU(){
            return "lpg/lng carrier (gas)";
        }
    },
    OSV{
        public String toStringRU(){
            return "offshore supply vessel (osv)";
        }
    },
    HVL{
        public String toStringRU(){
            return "heavy lift vessel (hvl)";
        }
    },
    SRV{
        public String toStringRU(){
            return "survey vessel (srv)";
        }
    },
    PAS{
        public String toStringRU(){
            return "passenger ship (pas)";
        }
    },
    RFG{
        public String toStringRU(){
            return "reefer (rfg)";
        }
    },
    LIV{
        public String toStringRU(){
            return "livestock carrier (liv)";
        }
    },
    TUG{
        public String toStringRU(){
            return "tug";
        }
    },
    FSH{
        public String toStringRU(){
            return "fishing trawler (fsh)";
        }
    },
    DRG{
        public String toStringRU(){
            return "dredger (drg)";
        }
    },
    M{
        public String toStringRU(){
            return "м";
        }
    },
    M_SP{
        public String toStringRU(){
            return "м-сп";
        }
    },
    O{
        public String toStringRU(){
            return "о";
        }
    },
    R{
        public String toStringRU(){
            return "р";
        }
    },
    L{
        public String toStringRU(){
            return "л";
        }
    };

    public abstract String toStringRU();
}
