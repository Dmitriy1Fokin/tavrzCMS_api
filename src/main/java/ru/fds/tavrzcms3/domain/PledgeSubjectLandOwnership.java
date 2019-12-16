package ru.fds.tavrzcms3.domain;

import lombok.*;
import ru.fds.tavrzcms3.dictionary.LandCategory;

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
public class PledgeSubjectLandOwnership {

    @NotNull(message = "Обязательно для заполнения")
    @PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
    @Column(name ="area_land_own", table = "pledge_realty_land_ownership")
    private double area;

    @Pattern(regexp = "[0-9]{2}:[0-9]{2}:[0-9]{3,7}:[0-9]+",
            message = "Неверное значение")
    @Column(name ="cadastral_num_land_own", table = "pledge_realty_land_ownership")
    private String cadastralNum;

    @Column(name ="conditional_num_land_own", table = "pledge_realty_land_ownership")
    private String conditionalNum;

    @NotBlank(message = "Обязательно для заполнения")
	@Column(name ="permitted_use_land_own", table = "pledge_realty_land_ownership")
	private String permittedUse;

	@Pattern(regexp = "да|нет", message = "Возможные варианты: да, нет")
	@Column(name ="built_up_land_own", table = "pledge_realty_land_ownership")
	private String builtUp;

    @Pattern(regexp = "^$|([0-9]{2}:[0-9]{2}:[0-9]{3,7}:[0-9]+(( *; *[0-9]{2}:[0-9]{2}:[0-9]{3,7}:[0-9]+)|($))*)+",
            message = "Неверное значение. Если несколько кад№, указывать через \";\"")
	@Column(name ="cadastral_num_of_building_land_own", table = "pledge_realty_land_ownership")
	private String cadastralNumOfBuilding;

    @Convert(converter = LandCategory.Converter.class)
    @Column(name ="land_category_land_own", table = "pledge_realty_land_ownership")
	private LandCategory landCategory;
}
