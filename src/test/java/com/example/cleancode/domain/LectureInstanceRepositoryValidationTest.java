package com.example.cleancode.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.cleancode.application.validation.EntityValidation;

@SpringBootTest
class LectureInstanceRepositoryValidationTest {

	/*
	 * LectureInstanceEntity
	 *
	 * 성공 케이스:
	 * - ID 성공 경계값 테스트 (최소값)
	 * - 시작일과 종료일 성공 경계값 테스트
	 * - 최대 및 현재 수강 인원 성공 테스트
	 *
	 * 실패 케이스:
	 * - ID 실패 경계값 테스트 (ID가 0일 때)
	 * - 종료일 실패 경계값 테스트 (종료일이 시작일보다 이전일 때)
	 * - 최대 수강 인원 실패 테스트 (1 미만일 때)
	 * - 현재 수강 인원 실패 테스트 (0 미만일 때)
	 * - 현재 수강 인원이 최대 수강 인원을 초과할 때
	 */

	// 성공 케이스
	@Test
	@DisplayName("ID 성공 경계값 테스트 (최소값)")
	void lectureInstanceId_성공_경계값_최소값_테스트() {
		// given
		Lecture lecture = new Lecture(1L, "Clean Architecture", "John Doe");
		LectureInstance lectureInstance = new LectureInstance(1L, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 3, 1),
			30, 10, lecture, LectureStatus.OPEN);

		// when & then
		EntityValidation.validateLectureInstance(lectureInstance); // 유효성 검증 성공
	}

	@Test
	@DisplayName("시작일과 종료일 성공 테스트")
	void lectureInstanceDates_성공_테스트() {
		// given
		Lecture lecture = new Lecture(1L, "Clean Architecture", "John Doe");
		LectureInstance lectureInstance = new LectureInstance(1L, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 3, 1),
			30, 10, lecture, LectureStatus.OPEN);

		// when & then
		EntityValidation.validateLectureInstance(lectureInstance); // 유효성 검증 성공
	}

	@Test
	@DisplayName("최대 및 현재 수강 인원 성공 테스트")
	void lectureInstanceParticipants_성공_테스트() {
		// given
		Lecture lecture = new Lecture(1L, "Clean Architecture", "John Doe");
		LectureInstance lectureInstance = new LectureInstance(1L, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 3, 1),
			30, 25, lecture, LectureStatus.OPEN);

		// when & then
		EntityValidation.validateLectureInstance(lectureInstance); // 유효성 검증 성공
	}

	// 실패 케이스
	@Test
	@DisplayName("ID 실패 경계값 테스트 (ID가 0일 때)")
	void lectureInstanceId_실패_경계값_테스트_0() {
		// given
		Lecture lecture = new Lecture(1L, "Clean Architecture", "John Doe");
		LectureInstance invalidLectureInstance = new LectureInstance(0L, LocalDate.of(2024, 1, 1),
			LocalDate.of(2024, 3, 1), 30, 10, lecture, LectureStatus.OPEN);

		// when & then
		assertThatThrownBy(() -> EntityValidation.validateLectureInstance(invalidLectureInstance)).isInstanceOf(
			IllegalArgumentException.class).hasMessageContaining("LectureInstanceRepository ID는 1 이상의 값이어야 합니다.");
	}

	@Test
	@DisplayName("종료일 실패 경계값 테스트 (종료일이 시작일보다 이전일 때)")
	void lectureInstanceDates_실패_경계값_테스트() {
		// given
		Lecture lecture = new Lecture(1L, "Clean Architecture", "John Doe");
		LectureInstance invalidLectureInstance = new LectureInstance(1L, LocalDate.of(2024, 1, 1),
			LocalDate.of(2023, 12, 31), 30, 10, lecture, LectureStatus.OPEN);

		// when & then
		assertThatThrownBy(() -> EntityValidation.validateLectureInstance(invalidLectureInstance)).isInstanceOf(
			IllegalArgumentException.class).hasMessageContaining("강의 종료일은 시작일보다 이후여야 합니다.");
	}

	@Test
	@DisplayName("최대 수강 인원 실패 테스트 (1 미만일 때)")
	void lectureInstanceMaxParticipants_실패_테스트() {
		// given
		Lecture lecture = new Lecture(1L, "Clean Architecture", "John Doe");
		LectureInstance invalidLectureInstance = new LectureInstance(1L, LocalDate.of(2024, 1, 1),
			LocalDate.of(2024, 3, 1), 0, 10, lecture, LectureStatus.OPEN);

		// when & then
		assertThatThrownBy(() -> EntityValidation.validateLectureInstance(invalidLectureInstance)).isInstanceOf(
			IllegalArgumentException.class).hasMessageContaining("최대 수강 인원은 1 이상이어야 합니다.");
	}

	@Test
	@DisplayName("현재 수강 인원 실패 테스트 (0 미만일 때)")
	void lectureInstanceCurrentParticipants_실패_테스트() {
		// given
		Lecture lecture = new Lecture(1L, "Clean Architecture", "John Doe");
		LectureInstance invalidLectureInstance = new LectureInstance(1L, LocalDate.of(2024, 1, 1),
			LocalDate.of(2024, 3, 1), 30, -5, lecture, LectureStatus.OPEN);

		// when & then
		assertThatThrownBy(() -> EntityValidation.validateLectureInstance(invalidLectureInstance)).isInstanceOf(
			IllegalArgumentException.class).hasMessageContaining("현재 수강 인원은 0 이상이어야 합니다.");
	}

	@Test
	@DisplayName("현재 수강 인원이 최대 수강 인원을 초과할 때")
	void lectureInstanceParticipantsExceeded_실패_테스트() {
		// given
		Lecture lecture = new Lecture(1L, "Clean Architecture", "John Doe");
		LectureInstance invalidLectureInstance = new LectureInstance(1L, LocalDate.of(2024, 1, 1),
			LocalDate.of(2024, 3, 1), 30, 40, lecture, LectureStatus.OPEN);

		// when & then
		assertThatThrownBy(() -> EntityValidation.validateLectureInstance(invalidLectureInstance)).isInstanceOf(
			IllegalArgumentException.class).hasMessageContaining("현재 수강 인원은 최대 수강 인원을 초과할 수 없습니다.");
	}
}
