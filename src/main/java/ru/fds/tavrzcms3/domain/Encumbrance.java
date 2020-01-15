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
import java.time.LocalDate;

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
    private Long encumbranceId;

    @NotBlank(message = "Обязательно для заполнения")
    @Column(name ="name")
    private String nameEncumbrance;

    @NotNull
    @Convert(converter = TypeOfEncumbrance.Converter.class)
    @Column(name ="type_of_encumbrance")
    private TypeOfEncumbrance typeOfEncumbrance;

    @NotBlank(message = "Обязательно для заполнения")
    @Column(name ="in_favor_of_whom")
    private String inFavorOfWhom;

    @NotNull(message = "Обязательно для заполнения")
    @Column(name ="date_begin", columnDefinition = "DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateBegin;

    @NotNull(message = "Обязательно для заполнения")
    @Column(name ="date_end", columnDefinition = "DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateEnd;

    @NotBlank(message = "Обязательно для заполнения")
    @Column(name = "num_of_encumbrance")
    private String numOfEncumbrance;

    @NotNull
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
