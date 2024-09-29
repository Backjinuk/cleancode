package com.example.cleancode.adapter.in.dto;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.cleancode.domain.LectureStatus;
import com.example.cleancode.util.validation.DtoValidation;

class LectureApplyDtoValidationTest {

	/*
	 * LectureApplyDtoEntity
	 *
	 * 성공 케이스:
	 * - ID 성공 경계값 테스트 (최소값)
	 * - MemberDto 유효성 검증 성공
	 * - LectureDto 유효성 검증 성공
	 *
	 * 실패 케이스:
	 * - ID 실패 경계값 테스트 (ID가 0일 때)
	 * - MemberDto 실패 유효성 검증 (null일 때)
	 * - LectureDto 실패 유효성 검증 (null일 때)
	 */

	// 성공 케이스
	@Test
	@DisplayName("ID 성공 경계값 테스트 (최소값)")
	void LectureApplyDtoId_성공_경계값_최소값_테스트() {
		// given
		MemberDto validMemberDto = new MemberDto(1L, "John Doe");
		LectureDto validLectureDto = new LectureDto(1L, "Clean Architecture", 30, LectureStatus.OPEN);
		LectureApplyDto LectureApplyDto = new LectureApplyDto(1L, validMemberDto, validLectureDto);

		// when & then
		DtoValidation.validationLectureApplyDto(LectureApplyDto); // 유효성 검증 성공
	}

	// 실패 케이스
	@Test
	@DisplayName("ID 실패 경계값 테스트 (ID가 0일 때)")
	void LectureApplyDtoId_실패_경계값_테스트_0() {
		// given
		MemberDto validMemberDto = new MemberDto(1L, "John Doe");
		LectureDto validLectureDto = new LectureDto(1L, "Clean Architecture", 30, LectureStatus.OPEN);
		LectureApplyDto invalidLectureApplyDto = new LectureApplyDto(0L, validMemberDto, validLectureDto);

		// when & then
		assertThatThrownBy(() -> DtoValidation.validationLectureApplyDto(invalidLectureApplyDto))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("LectureApplyDtoId는 1이상의 값이어야 합니다");
	}

	@Test
	@DisplayName("MemberDto 실패 유효성 검증 (null일 때)")
	void LectureApplyDtoMemberDto_실패_유효성_검증_null() {
		// given
		LectureDto validLectureDto = new LectureDto(1L, "Clean Architecture", 30, LectureStatus.OPEN);
		LectureApplyDto invalidLectureApplyDto = new LectureApplyDto(1L, null, validLectureDto);

		// when & then
		assertThatThrownBy(() -> DtoValidation.validationLectureApplyDto(invalidLectureApplyDto))
			.isInstanceOf(NullPointerException.class)
			.hasMessageContaining("MemberDto는 null일 수 없습니다.");
	}

	@Test
	@DisplayName("LectureDto 실패 유효성 검증 (null일 때)")
	void LectureApplyDtoLectureDto_실패_유효성_검증_null() {
		// given
		MemberDto validMemberDto = new MemberDto(1L, "John Doe");
		LectureApplyDto invalidLectureApplyDto = new LectureApplyDto(1L, validMemberDto, null);

		// when & then
		assertThatThrownBy(() -> DtoValidation.validationLectureApplyDto(invalidLectureApplyDto))
			.isInstanceOf(NullPointerException.class)
			.hasMessageContaining("LectureDto는 null일 수 없습니다.");
	}
}
