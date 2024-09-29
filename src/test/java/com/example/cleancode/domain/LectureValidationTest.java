package com.example.cleancode.domain;

import static org.assertj.core.api.Assertions.*;

import com.example.cleancode.util.validation.EntityValidation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LectureValidationTest {

	/*
	 * LectureEntity
	 *
	 * 성공 케이스:
	 * - ID 성공 경계값 테스트 (최소값)
	 * - 제목 성공 경계값 테스트 (최소 길이)
	 * - 제목 성공 경계값 테스트 (최대 길이)
	 * - 참가자 수 성공 경계값 테스트 (최소값)
	 *
	 * 실패 케이스:
	 * - ID 실패 경계값 테스트 (ID가 0일 때)
	 * - 제목 실패 경계값 테스트 (빈 문자열일 때)
	 * - 제목 실패 경계값 테스트 (제목이 255자를 초과할 때)
	 * - 참가자 수 실패 경계값 테스트 (참가자 수가 0일 때)
	 */

	// 성공 케이스
	@Test
	@DisplayName("ID 성공 경계값 테스트 (최소값)")
	void lectureId_성공_경계값_최소값_테스트() {
		// given
		Lecture lecture = new Lecture(1L, "Clean Architecture", 30, LectureStatus.OPEN);

		// when & then
		EntityValidation.validationLecture(lecture); // 유효성 검증 성공
	}

	@Test
	@DisplayName("제목 성공 경계값 테스트 (최소 길이)")
	void lectureTitle_성공_경계값_최소값_테스트() {
		// given
		Lecture lecture = new Lecture(1L, "A", 30, LectureStatus.OPEN);

		// when & then
		EntityValidation.validationLecture(lecture); // 유효성 검증 성공
	}

	@Test
	@DisplayName("제목 성공 경계값 테스트 (최대 길이)")
	void lectureTitle_성공_경계값_최대값_테스트() {
		// given
		String validTitle = "A".repeat(255);
		Lecture lecture = new Lecture(1L, validTitle, 30, LectureStatus.OPEN);

		// when & then
		EntityValidation.validationLecture(lecture); // 유효성 검증 성공
	}

	@Test
	@DisplayName("참가자 수 성공 경계값 테스트 (최소값)")
	void lectureMaxParticipants_성공_경계값_최소값_테스트() {
		// given
		Lecture lecture = new Lecture(1L, "Clean Architecture", 1, LectureStatus.OPEN);

		// when & then
		EntityValidation.validationLecture(lecture); // 유효성 검증 성공
	}

	// 실패 케이스
	@Test
	@DisplayName("ID 실패 경계값 테스트 (ID가 0일 때)")
	void lectureId_실패_경계값_테스트_0() {
		// given
		Lecture invalidLecture = new Lecture(0L, "Clean Architecture", 30, LectureStatus.OPEN);

		// when & then
		assertThatThrownBy(() -> EntityValidation.validationLecture(invalidLecture))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("lectureId는 1이상의 값이어야 합니다");
	}

	@Test
	@DisplayName("제목 실패 경계값 테스트 (빈 문자열일 때)")
	void lectureTitle_실패_경계값_테스트_빈_문자열() {
		// given
		Lecture invalidLecture = new Lecture(1L, "", 30, LectureStatus.OPEN);

		// when & then
		assertThatThrownBy(() -> EntityValidation.validationLecture(invalidLecture))
			.isInstanceOf(NullPointerException.class)
			.hasMessageContaining("강의 제목은 비어 있을 수 없습니다.");
	}

	@Test
	@DisplayName("제목 실패 경계값 테스트 (제목이 255자를 초과할 때)")
	void lectureTitle_실패_경계값_테스트_초과() {
		// given
		String tooLongTitle = "A".repeat(256);
		Lecture invalidLecture = new Lecture(1L, tooLongTitle, 30, LectureStatus.OPEN);

		// when & then
		assertThatThrownBy(() -> EntityValidation.validationLecture(invalidLecture))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("강의 제목은 255자를 초과할 수 없습니다.");
	}

	@Test
	@DisplayName("참가자 수 실패 경계값 테스트 (참가자 수가 0일 때)")
	void lectureMaxParticipants_실패_경계값_테스트_0() {
		// given
		Lecture invalidLecture = new Lecture(1L, "Clean Architecture", 0, LectureStatus.OPEN);

		// when & then
		assertThatThrownBy(() -> EntityValidation.validationLecture(invalidLecture))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("참가자 수는 1 이상이어야 합니다.");
	}
}
