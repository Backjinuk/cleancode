package com.example.cleancode.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.cleancode.application.validation.EntityValidation;

@SpringBootTest
class LectureApplyValidationTest {

	/*
	 * LectureApplyEntity
	 *
	 * 성공 케이스:
	 * - ID 성공 경계값 테스트 (최소값)
	 * - Member 유효성 검증 성공
	 * - LectureInstanceRepository 유효성 검증 성공
	 *
	 * 실패 케이스:
	 * - ID 실패 경계값 테스트 (ID가 0일 때)
	 * - Member 실패 유효성 검증 (null일 때)
	 * - LectureInstanceRepository 실패 유효성 검증 (null일 때)
	 */

	// 성공 케이스
	@Test
	@DisplayName("ID 성공 경계값 테스트 (최소값)")
	void lectureApplyId_성공_경계값_최소값_테스트() {
		// given
		Member validMember = new Member(1L, "John Doe");
		Lecture lecture = new Lecture(1L, "Clean Architecture", "Teacher");
		LectureInstance validLectureInstance = new LectureInstance(1L, LocalDate.of(2024, 1, 1),
			LocalDate.of(2024, 3, 1), 30, 10, lecture, LectureStatus.OPEN);
		LectureApply lectureApply = new LectureApply(1L, validMember, validLectureInstance);

		// when & then
		EntityValidation.validationLectureApply(lectureApply); // 유효성 검증 성공
	}

	// 실패 케이스
	@Test
	@DisplayName("ID 실패 경계값 테스트 (ID가 0일 때)")
	void lectureApplyId_실패_경계값_테스트_0() {
		// given
		Member validMember = new Member(1L, "John Doe");
		Lecture lecture = new Lecture(1L, "Clean Architecture", "Teacher");
		LectureInstance validLectureInstance = new LectureInstance(1L, LocalDate.of(2024, 1, 1),
			LocalDate.of(2024, 3, 1), 30, 10, lecture, LectureStatus.OPEN);
		LectureApply invalidLectureApply = new LectureApply(0L, validMember, validLectureInstance);

		// when & then
		assertThatThrownBy(() -> EntityValidation.validationLectureApply(invalidLectureApply)).isInstanceOf(
			IllegalArgumentException.class).hasMessageContaining("lectureApplyId는 1이상의 값이어야 합니다");
	}

	@Test
	@DisplayName("Member 실패 유효성 검증 (null일 때)")
	void lectureApplyMember_실패_유효성_검증_null() {
		// given
		Lecture lecture = new Lecture(1L, "Clean Architecture", "Teacher");
		LectureInstance validLectureInstance = new LectureInstance(1L, LocalDate.of(2024, 1, 1),
			LocalDate.of(2024, 3, 1), 30, 10, lecture, LectureStatus.OPEN);
		LectureApply invalidLectureApply = new LectureApply(1L, null, validLectureInstance);

		// when & then
		assertThatThrownBy(() -> EntityValidation.validationLectureApply(invalidLectureApply)).isInstanceOf(
			NullPointerException.class).hasMessageContaining("Member는 null일 수 없습니다.");
	}

	@Test
	@DisplayName("LectureInstanceRepository 실패 유효성 검증 (null일 때)")
	void lectureApplyLectureInstance_실패_유효성_검증_null() {
		// given
		Member validMember = new Member(1L, "John Doe");
		LectureApply invalidLectureApply = new LectureApply(1L, validMember, null);

		// when & then
		assertThatThrownBy(() -> EntityValidation.validationLectureApply(invalidLectureApply)).isInstanceOf(
			NullPointerException.class).hasMessageContaining("LectureInstance는 null일 수 없습니다.");
	}
}
