package com.example.cleancode.adapter.in.dto;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.cleancode.application.validation.DtoValidation;
import com.example.cleancode.domain.LectureStatus;

class LectureInstanceRepositoryDtoValidationTest {

	/*
	 * LectureInstanceDtoEntity
	 *
	 * 성공 케이스:
	 * - ID 성공 경계값 테스트 (최소값)
	 * - 종료일이 시작일 이후일 때
	 * - 현재 수강 인원이 유효할 때
	 * - 상태가 정상적으로 설정되었을 때
	 *
	 * 실패 케이스:
	 * - ID 실패 경계값 테스트 (ID가 0일 때)
	 * - 종료일이 시작일보다 이전일 때
	 * - 현재 수강 인원이 음수일 때
	 * - 현재 수강 인원이 최대 수강 인원을 초과했을 때
	 * - 상태가 null일 때
	 * - Lecture ID가 0일 때
	 */

	// 성공 케이스
	@Test
	@DisplayName("LectureInstanceDto 유효성 검증 성공")
	void lectureInstanceDto_유효성_검증_성공() {
		// given
		LectureDto validLectureDto = new LectureDto(1L, "lecture101", "향해99");
		LectureInstanceDto lectureInstanceDto = new LectureInstanceDto(1L, LocalDate.of(2024, 1, 1),
			LocalDate.of(2024, 3, 1), 30, 10, validLectureDto, LectureStatus.OPEN);

		// when & then
		DtoValidation.validateLectureInstanceDto(lectureInstanceDto); // 성공적으로 통과
	}

	// 실패 케이스
	@Test
	@DisplayName("LectureInstanceDto 유효성 검증 실패 - ID가 0일 때")
	void lectureInstanceDto_유효성_검증_실패_ID_0() {
		// given
		LectureDto validLectureDto = new LectureDto(1L, "lecture101", "향해99");
		LectureInstanceDto invalidLectureInstanceDto = new LectureInstanceDto(0L, LocalDate.of(2024, 1, 1),
			LocalDate.of(2024, 3, 1), 30, 10, validLectureDto, LectureStatus.OPEN);

		// when & then
		assertThatThrownBy(() -> DtoValidation.validateLectureInstanceDto(invalidLectureInstanceDto)).isInstanceOf(
			IllegalArgumentException.class).hasMessageContaining("LectureInstanceDto ID는 1 이상의 값이어야 합니다.");
	}

	@Test
	@DisplayName("LectureInstanceDto 유효성 검증 실패 - 종료일이 시작일보다 이전일 때")
	void lectureInstanceDto_유효성_검증_실패_종료일_시작일_이전() {
		// given
		LectureDto validLectureDto = new LectureDto(1L, "lecture101", "향해99");
		LectureInstanceDto invalidLectureInstanceDto = new LectureInstanceDto(1L, LocalDate.of(2024, 3, 1),
			LocalDate.of(2024, 1, 1), 30, 10, validLectureDto, LectureStatus.OPEN);

		// when & then
		assertThatThrownBy(() -> DtoValidation.validateLectureInstanceDto(invalidLectureInstanceDto)).isInstanceOf(
			IllegalArgumentException.class).hasMessageContaining("강의 종료일은 시작일보다 이후여야 합니다.");
	}

	@Test
	@DisplayName("LectureInstanceDto 유효성 검증 실패 - 현재 수강 인원이 음수일 때")
	void lectureInstanceDto_유효성_검증_실패_현재_수강_인원_음수() {
		// given
		LectureDto validLectureDto = new LectureDto(1L, "lecture101", "향해99");
		LectureInstanceDto invalidLectureInstanceDto = new LectureInstanceDto(1L, LocalDate.of(2024, 1, 1),
			LocalDate.of(2024, 3, 1), 30, -5, validLectureDto, LectureStatus.OPEN);

		// when & then
		assertThatThrownBy(() -> DtoValidation.validateLectureInstanceDto(invalidLectureInstanceDto)).isInstanceOf(
			IllegalArgumentException.class).hasMessageContaining("현재 수강 인원은 0 이상이어야 합니다.");
	}

	@Test
	@DisplayName("LectureInstanceDto 유효성 검증 실패 - 현재 수강 인원이 최대 수강 인원을 초과했을 때")
	void lectureInstanceDto_유효성_검증_실패_참가자_초과() {
		// given
		LectureDto validLectureDto = new LectureDto(1L, "lecture101", "향해99");
		LectureInstanceDto invalidLectureInstanceDto = new LectureInstanceDto(1L, LocalDate.of(2024, 1, 1),
			LocalDate.of(2024, 3, 1), 30, 35, validLectureDto, LectureStatus.OPEN);

		// when & then
		assertThatThrownBy(() -> DtoValidation.validateLectureInstanceDto(invalidLectureInstanceDto)).isInstanceOf(
			IllegalArgumentException.class).hasMessageContaining("현재 수강 인원은 최대 수강 인원을 초과할 수 없습니다.");
	}

	@Test
	@DisplayName("LectureInstanceDto 유효성 검증 실패 - LectureDto가 null일 때")
	void lectureInstanceDto_유효성_검증_실패_LectureDto_null() {
		// given
		LectureInstanceDto invalidLectureInstanceDto = new LectureInstanceDto(1L, LocalDate.of(2024, 1, 1),
			LocalDate.of(2024, 3, 1), 30, 10, null, LectureStatus.OPEN);

		// when & then
		assertThatThrownBy(() -> DtoValidation.validateLectureInstanceDto(invalidLectureInstanceDto)).isInstanceOf(
			NullPointerException.class).hasMessageContaining("LectureDto는 null일 수 없습니다.");
	}

	@Test
	@DisplayName("LectureInstanceDto 유효성 검증 실패 - 상태가 null일 때")
	void lectureInstanceDto_유효성_검증_실패_Status_null() {
		// given
		LectureDto validLectureDto = new LectureDto(1L, "lecture101", "향해99");
		LectureInstanceDto invalidLectureInstanceDto = new LectureInstanceDto(1L, LocalDate.of(2024, 1, 1),
			LocalDate.of(2024, 3, 1), 30, 10, validLectureDto, null);  // Status가 null

		// when & then
		assertThatThrownBy(() -> DtoValidation.validateLectureInstanceDto(invalidLectureInstanceDto)).isInstanceOf(
			NullPointerException.class).hasMessageContaining("강의 상태는 반드시 필요합니다.");
	}
}
