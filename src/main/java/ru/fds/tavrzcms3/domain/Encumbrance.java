package ru.fds.tavrzcms3.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Entity
@Table(name = "encumbrance")
public class Encumbrance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="encumbrance_id")
    private long encumbranceId;

    @NotBlank(message = "Обязательно для заполнения")
    @Column(name ="name")
    private String nameEncumbrance;

    @Pattern(regexp = "залог|арест|аренда|сервитут|доверительное управление",
            message = "Возможные варианты: залог, арест, аренда, сервитут, доверительное управление")
    @Column(name ="type_of_encumbrance")
    private String typeOfEncumbrance;

    @NotBlank(message = "Обязательно для заполнения")
    @Column(name ="in_favor_of_whom")
    private String inFavorOfWhom;

    @NotNull(message = "Обязательно для заполнения")
    @Column(name ="date_begin")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateBegin;

    @NotNull(message = "Обязательно для заполнения")
    @Column(name ="date_end")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateEnd;

    @NotBlank(message = "Обязательно для заполнения")
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

    public String getNameEncumbrance() {
        return nameEncumbrance;
    }

    public void setNameEncumbrance(String nameEncumbrance) {
        this.nameEncumbrance = nameEncumbrance;
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
                ", nameEncumbrance='" + nameEncumbrance + '\'' +
                ", typeOfEncumbrance='" + typeOfEncumbrance + '\'' +
                ", inFavorOfWhom='" + inFavorOfWhom + '\'' +
                ", dateBegin=" + dateBegin +
                ", dateEnd=" + dateEnd +
                ", numOfEncumbrance='" + numOfEncumbrance + '\'' +
                '}';
    }
}
