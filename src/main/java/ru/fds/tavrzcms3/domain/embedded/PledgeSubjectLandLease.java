package ru.fds.tavrzcms3.domain.embedded;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.fds.tavrzcms3.dictionary.LandCategory;

import java.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class PledgeSubjectLandLease{

    @NotNull(message = "Обязательно для заполнения")
    @PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
    @Column(name ="area_land_lease", table = "pledge_realty_land_lease")
    private double area;

    @Pattern(regexp = "[0-9]{2}:[0-9]{2}:[0-9]{3,7}:[0-9]+",
            message = "Неверное значение")
    @Column(name ="cadastral_num_land_lease", table = "pledge_realty_land_lease")
    private String cadastralNum;

    @Column(name ="conditional_num_land_lease", table = "pledge_realty_land_lease")
    private String conditionalNum;

    @NotBlank(message = "Обязательно для заполнения")
	@Column(name ="permitted_use_land_lease", table = "pledge_realty_land_lease")
	private String permittedUse;

	@Pattern(regexp = "да|нет", message = "Возможные варианты: да, нет")
	@Column(name ="built_up_land_lease", table = "pledge_realty_land_lease")
	private String builtUp;

	@Pattern(regexp = "^$|([0-9]{2}:[0-9]{2}:[0-9]{3,7}:[0-9]+(( *; *[0-9]{2}:[0-9]{2}:[0-9]{3,7}:[0-9]+)|($))*)+",
			message = "Неверное значение. Если несколько кад№, указывать через \";\"")
	@Column(name ="cadastral_num_of_building_land_lease", table = "pledge_realty_land_lease")
	private String cadastralNumOfBuilding;

	@NotNull(message = "Обязательно для заполнения")
	@Column(name ="begin_date_lease", table = "pledge_realty_land_lease", columnDefinition = "DATE")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateBeginLease;

	@NotNull(message = "Обязательно для заполнения")
	@Column(name ="end_date_lease", table = "pledge_realty_land_lease", columnDefinition = "DATE")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateEndLease;

	@Convert(converter = LandCategory.Converter.class)
    @Column(name ="land_category_land_lease", table = "pledge_realty_land_lease")
	private LandCategory landCategory;
}
