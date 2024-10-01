package com.example.cleancode.adapter.in.dto;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.cleancode.application.validation.DtoValidation;
import com.example.cleancode.domain.LectureStatus;

class LectureApplyDtoValidationTest {

	/*
	 * LectureApplyDtoEntity
	 *
	 * 성공 케이스:
	 * - ID 성공 경계값 테스트 (최소값)
	 * - MemberDto 유효성 검증 성공
	 * - LectureInstanceDto 유효성 검증 성공
	 *
	 * 실패 케이스:
	 * - ID 실패 경계값 테스트 (ID가 0일 때)
	 * - MemberDto 실패 유효성 검증 (null일 때)
	 * - LectureInstanceDto 실패 유효성 검증 (null일 때)
	 * - LectureInstanceDto 상태 실패 검증 (status가 null일 때)
	 */

	// 성공 케이스
	@Test
	@DisplayName("LectureApplyDto - ID 성공 경계값 테스트 (최소값)")
	void LectureApplyDtoId_성공_경계값_최소값_테스트() {
		// given
		MemberDto validMemberDto = new MemberDto(1L, "John Doe");
		LectureDto validLectureDto = new LectureDto(1L, "lecture101", "향해99");
		LectureInstanceDto validLectureInstanceDto = new LectureInstanceDto(1L, LocalDate.of(2024, 1, 1),
			LocalDate.of(2024, 3, 1), 30, 10, validLectureDto, LectureStatus.OPEN);
		LectureApplyDto lectureApplyDto = new LectureApplyDto(1L, validMemberDto, validLectureInstanceDto);

		// when & then
		DtoValidation.validationLectureApplyDto(lectureApplyDto); // 유효성 검증 성공
	}

	// 실패 케이스
	@Test
	@DisplayName("LectureApplyDto - ID 실패 경계값 테스트 (ID가 0일 때)")
	void LectureApplyDtoId_실패_경계값_테스트_0() {
		// given
		MemberDto validMemberDto = new MemberDto(1L, "John Doe");
		LectureDto validLectureDto = new LectureDto(1L, "lecture101", "향해99");
		LectureInstanceDto validLectureInstanceDto = new LectureInstanceDto(1L, LocalDate.of(2024, 1, 1),
			LocalDate.of(2024, 3, 1), 30, 10, validLectureDto, LectureStatus.OPEN);
		LectureApplyDto invalidLectureApplyDto = new LectureApplyDto(0L, validMemberDto, validLectureInstanceDto);

		// when & then
		assertThatThrownBy(() -> DtoValidation.validationLectureApplyDto(invalidLectureApplyDto)).isInstanceOf(
			IllegalArgumentException.class).hasMessageContaining("LectureApplyDto ID는 1 이상의 값이어야 합니다.");
	}

	@Test
	@DisplayName("LectureApplyDto - MemberDto 실패 유효성 검증 (null일 때)")
	void LectureApplyDtoMemberDto_실패_유효성_검증_null() {
		// given
		LectureDto validLectureDto = new LectureDto(1L, "lecture101", "향해99");
		LectureInstanceDto validLectureInstanceDto = new LectureInstanceDto(1L, LocalDate.of(2024, 1, 1),
			LocalDate.of(2024, 3, 1), 30, 10, validLectureDto, LectureStatus.OPEN);
		LectureApplyDto invalidLectureApplyDto = new LectureApplyDto(1L, null, validLectureInstanceDto);

		// when & then
		assertThatThrownBy(() -> DtoValidation.validationLectureApplyDto(invalidLectureApplyDto)).isInstanceOf(
			NullPointerException.class).hasMessageContaining("MemberDto는 null일 수 없습니다.");
	}

	@Test
	@DisplayName("LectureApplyDto - LectureInstanceDto 실패 유효성 검증 (null일 때)")
	void LectureApplyDtoLectureInstanceDto_실패_유효성_검증_null() {
		// given
		MemberDto validMemberDto = new MemberDto(1L, "John Doe");
		LectureApplyDto invalidLectureApplyDto = new LectureApplyDto(1L, validMemberDto, null);

		// when & then
		assertThatThrownBy(() -> DtoValidation.validationLectureApplyDto(invalidLectureApplyDto)).isInstanceOf(
			NullPointerException.class).hasMessageContaining("LectureInstanceDto는 null일 수 없습니다.");
	}

}
