package com.example.cleancode.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lecture {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "강의 제목은 비어 있을 수 없습니다.")
	@Size(max = 255, message = "강의 제목은 255자를 초과할 수 없습니다.")
	private String title;

	@Min(value = 1, message = "참가자 수는 1 이상이어야 합니다.")
	private long maxParticipants;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "강의 상태는 반드시 필요합니다.")
	private LectureStatus status;

}
