package com.bestarch.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

	private String username;
	private String doctorName;
	private String appointmentId;
	private String contactNo;
	private Long appointmentDateTime;
	private String appointmentDateStr;
	private String status;
	private String description;
	private Long createdTime;
	private Long updatedTime;
}