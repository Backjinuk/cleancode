package com.example.cleancode.domain;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LectureInstance {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDate startDate;  // 강의 시작일
	private LocalDate endDate;    // 강의 종료일
	private int maxParticipants;  // 최대 수용 인원
	private int currentParticipants; // 현재 수강 인원

	@ManyToOne
	@JoinColumn(name = "lecture_id")
	private Lecture lecture;  // 해당 강의와 연관 관계


	@Enumerated(EnumType.STRING)
	@NotNull(message = "강의 상태는 반드시 필요합니다.")
	private LectureStatus status;


}
