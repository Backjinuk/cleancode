package com.example.cleancode.adapter.in.dto;

import java.time.LocalDate;

import com.example.cleancode.domain.LectureStatus;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectureInstanceDto {
	private Long id;
	private LocalDate startDate;
	private LocalDate endDate;
	private int maxParticipants;
	private int currentParticipants;
	private LectureDto lectureDto;

	@NotNull(message = "강의 상태는 반드시 필요합니다.")
	private LectureStatus status;

	public LectureInstanceDto(Long lectureInstanceId) {
		this.id = lectureInstanceId;
	}
}
