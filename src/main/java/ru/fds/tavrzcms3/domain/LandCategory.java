package ru.fds.tavrzcms3.domain;

import lombok.*;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@Table(name = "land_category")
public class LandCategory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="land_category_id")
	private int landCategoryid;
	
	@Column(name ="name")
	private String name;

	@OneToMany(mappedBy = "landCategory")
	private List<PledgeSubjectLandLease> pledgeSubjectLandLeases;

	@OneToMany(mappedBy = "landCategory")
	private List<PledgeSubjectLandOwnership> pledgeSubjectLandOwnerships;

	@Override
	public String toString() {
		return "LandCategory{" +
				"landCategoryid=" + landCategoryid +
				", name='" + name + '\'' +
				'}';
	}
}
