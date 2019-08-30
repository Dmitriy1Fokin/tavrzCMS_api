package ru.fds.tavrzcms3.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "encumbrance")
public class Encumbrance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="encumbrance_id")
    private long encumbranceId;

    @Column(name ="name")
    private String name;

    @Column(name ="type_of_encumbrance")
    private String typeOfEncumbrance;

    @Column(name ="in_favor_of_whom")
    private String inFavorOfWhom;

    @Column(name ="date_begin")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateBegin;

    @Column(name ="date_end")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateEnd;

    @Column(name = "num_of_encumbrance")
    private String numOfEncumbrance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pledgesubject_id")
    private PledgeSubject pledgeSubject;

    public long getEncumbranceId() {
        return encumbranceId;
    }

    public void setEncumbranceId(long encumbranceId) {
        this.encumbranceId = encumbranceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeOfEncumbrance() {
        return typeOfEncumbrance;
    }

    public void setTypeOfEncumbrance(String typeOfEncumbrance) {
        this.typeOfEncumbrance = typeOfEncumbrance;
    }

    public String getInFavorOfWhom() {
        return inFavorOfWhom;
    }

    public void setInFavorOfWhom(String inFavorOfWhom) {
        this.inFavorOfWhom = inFavorOfWhom;
    }

    public Date getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(Date dateBegin) {
        this.dateBegin = dateBegin;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getNumOfEncumbrance() {
        return numOfEncumbrance;
    }

    public void setNumOfEncumbrance(String numOfEncumbrance) {
        this.numOfEncumbrance = numOfEncumbrance;
    }

    public PledgeSubject getPledgeSubject() {
        return pledgeSubject;
    }

    public void setPledgeSubject(PledgeSubject pledgeSubject) {
        this.pledgeSubject = pledgeSubject;
    }

    @Override
    public String toString() {
        return "Encumbrance{" +
                "encumbranceId=" + encumbranceId +
                ", name='" + name + '\'' +
                ", typeOfEncumbrance='" + typeOfEncumbrance + '\'' +
                ", inFavorOfWhom='" + inFavorOfWhom + '\'' +
                ", dateBegin=" + dateBegin +
                ", dateEnd=" + dateEnd +
                ", numOfEncumbrance='" + numOfEncumbrance + '\'' +
                '}';
    }
}
