package com.example.cleancode.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
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
public class LectureApply {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Min(value = 1, message = "lectureApplyId는 1이상의 값이어야 합니다")
	private long id;

	@ManyToOne
	@JoinColumn(name = "member_id")  // 외래 키 컬럼은 "member_id"로 지정
	@NotNull(message = "Member는 null일 수 없습니다.")  // Member는 null이 될 수 없음
	private Member member;

	@ManyToOne
	@JoinColumn(name = "lecture_instance_id")  // 외래 키 컬럼은 "lecture_id"로 지정
	@NotNull(message = "lecture_instance_id null일 수 없습니다.")  // Lecture는 null이 될 수 없음
	private LectureInstance lectureInstance;
}
