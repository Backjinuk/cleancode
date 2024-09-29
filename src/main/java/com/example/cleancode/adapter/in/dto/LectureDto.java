package com.example.cleancode.adapter.in.dto;

import com.example.cleancode.domain.LectureStatus;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class LectureDto {

	@NotNull(message = "ID는 반드시 필요합니다.")
	long id;

	@NotBlank(message = "강의 제목은 비어 있을 수 없습니다.")
	@Size(max = 255, message = "강의 제목은 255자를 초과할 수 없습니다.")
	String title;

	@Min(value = 1, message = "참가자 수는 1이상이여야 합니다.")
	long maxParticipants;

	@NotNull(message = "강의 상태는 반드시 필요합니다.")
	LectureStatus status;
}
