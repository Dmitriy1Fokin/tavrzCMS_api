package ru.fds.tavrzcms3.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.fds.tavrzcms3.dictionary.TypeOfEncumbrance;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Convert(converter = TypeOfEncumbrance.Converter.class)
    @Column(name ="type_of_encumbrance")
    private TypeOfEncumbrance typeOfEncumbrance;

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
