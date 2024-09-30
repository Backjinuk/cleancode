package com.example.cleancode.adapter.in.dto;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.cleancode.domain.LectureStatus;
import com.example.cleancode.application.validation.DtoValidation;

class LectureDtoValidationTest {
	/*
	 * LectureDtoEntity
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
	void LectureDtoId_성공_경계값_최소값_테스트() {
		// given
		LectureDto LectureDto = new LectureDto(1L, "Clean Architecture", 30, LectureStatus.OPEN);

		// when & then
		DtoValidation.validationLectureDto(LectureDto); // 유효성 검증 성공
	}

	@Test
	@DisplayName("제목 성공 경계값 테스트 (최소 길이)")
	void LectureDtoTitle_성공_경계값_최소값_테스트() {
		// given
		LectureDto LectureDto = new LectureDto(1L, "A", 30, LectureStatus.OPEN);

		// when & then
		DtoValidation.validationLectureDto(LectureDto); // 유효성 검증 성공
	}

	@Test
	@DisplayName("제목 성공 경계값 테스트 (최대 길이)")
	void LectureDtoTitle_성공_경계값_최대값_테스트() {
		// given
		String validTitle = "A".repeat(255);
		LectureDto LectureDto = new LectureDto(1L, validTitle, 30, LectureStatus.OPEN);

		// when & then
		DtoValidation.validationLectureDto(LectureDto); // 유효성 검증 성공
	}

	@Test
	@DisplayName("참가자 수 성공 경계값 테스트 (최소값)")
	void LectureDtoMaxParticipants_성공_경계값_최소값_테스트() {
		// given
		LectureDto LectureDto = new LectureDto(1L, "Clean Architecture", 1, LectureStatus.OPEN);

		// when & then
		DtoValidation.validationLectureDto(LectureDto); // 유효성 검증 성공
	}

	// 실패 케이스
	@Test
	@DisplayName("ID 실패 경계값 테스트 (ID가 0일 때)")
	void LectureDtoId_실패_경계값_테스트_0() {
		// given
		LectureDto invalidLectureDto = new LectureDto(0L, "Clean Architecture", 30, LectureStatus.OPEN);

		// when & then
		assertThatThrownBy(() -> DtoValidation.validationLectureDto(invalidLectureDto))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("LectureDtoId는 1이상의 값이어야 합니다");
	}

	@Test
	@DisplayName("제목 실패 경계값 테스트 (빈 문자열일 때)")
	void LectureDtoTitle_실패_경계값_테스트_빈_문자열() {
		// given
		LectureDto invalidLectureDto = new LectureDto(1L, "", 30, LectureStatus.OPEN);

		// when & then
		assertThatThrownBy(() -> DtoValidation.validationLectureDto(invalidLectureDto))
			.isInstanceOf(NullPointerException.class)
			.hasMessageContaining("강의 제목은 비어 있을 수 없습니다.");
	}

	@Test
	@DisplayName("제목 실패 경계값 테스트 (제목이 255자를 초과할 때)")
	void LectureDtoTitle_실패_경계값_테스트_초과() {
		// given
		String tooLongTitle = "A".repeat(256);
		LectureDto invalidLectureDto = new LectureDto(1L, tooLongTitle, 30, LectureStatus.OPEN);

		// when & then
		assertThatThrownBy(() -> DtoValidation.validationLectureDto(invalidLectureDto))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("강의 제목은 255자를 초과할 수 없습니다.");
	}

	@Test
	@DisplayName("참가자 수 실패 경계값 테스트 (참가자 수가 0일 때)")
	void LectureDtoMaxParticipants_실패_경계값_테스트_0() {
		// given
		LectureDto invalidLectureDto = new LectureDto(1L, "Clean Architecture", 0, LectureStatus.OPEN);

		// when & then
		assertThatThrownBy(() -> DtoValidation.validationLectureDto(invalidLectureDto))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("참가자 수는 1 이상이어야 합니다.");
	}
}