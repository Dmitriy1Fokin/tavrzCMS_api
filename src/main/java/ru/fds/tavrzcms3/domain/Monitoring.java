package ru.fds.tavrzcms3.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "monitoring")
public class Monitoring {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="monitoring_id")
	private long monitoringId;
	
	@Column(name ="date")
	@Temporal(TemporalType.DATE)
	private Date dateMonitoring;
	
	@Column(name ="status")
	private String statusMonitoring;
	
	@Column(name ="employee")
	private String employee;
	
	@Column(name ="type")
	private String typeOfMonitoring;

	@Column(name = "notice")
	private String notice;

	@Column(name = "collateral_value")
	private Double collateralValue;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pledgeSubject_id")
	private PledgeSubject pledgeSubject;


}
