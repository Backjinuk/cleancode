package com.example.cleancode.adapter.in.dto;

import static org.assertj.core.api.Assertions.*;
import com.example.cleancode.application.validation.DtoValidation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberDtoValidationTest {

	/*
	 * MemberDtoEntity
	 *
	 * 성공 케이스:
	 * - ID 성공 경계값 테스트 (최소값)
	 * - ID 성공 경계값 테스트 (최대값)
	 * - 이름 성공 경계값 테스트 (최소 길이)
	 * - 이름 성공 경계값 테스트 (최대 길이)
	 *
	 * 실패 케이스:
	 * - ID 실패 경계값 테스트 (ID가 0일 때)
	 * - ID 실패 경계값 테스트 (ID가 음수일 때)
	 * - 이름 실패 경계값 테스트 (빈 문자열일 때)
	 * - 이름 실패 경계값 테스트 (이름이 6자일 때)
	 */

	// 성공 케이스
	@Test
	@DisplayName("ID 성공 경계값 테스트 (최소값)")
	void MemberDtoId_성공_경계값_최소값_테스트() {
		// given
		MemberDto MemberDtoMin = new MemberDto(1L, "John Doe");

		// when & then
		DtoValidation.validationMemberDto(MemberDtoMin); // 유효성 검증 성공
	}

	@Test
	@DisplayName("ID 성공 경계값 테스트 (최대값)")
	void MemberDtoId_성공_경계값_최대값_테스트() {
		// given
		MemberDto MemberDtoMax = new MemberDto(Long.MAX_VALUE, "Jane Doe");

		// when & then
		DtoValidation.validationMemberDto(MemberDtoMax); // 유효성 검증 성공
	}

	@Test
	@DisplayName("이름 성공 경계값 테스트 (최소 길이)")
	void MemberDtoName_성공_경계값_최소값_테스트() {
		// given
		MemberDto MemberDtoMinName = new MemberDto(1L, "A");

		// when & then
		DtoValidation.validationMemberDto(MemberDtoMinName); // 유효성 검증 성공
	}

	@Test
	@DisplayName("이름 성공 경계값 테스트 (최대 길이)")
	void MemberDtoName_성공_경계값_최대값_테스트() {
		// given
		String validName = "ABCDE";
		MemberDto MemberDtoMaxName = new MemberDto(1L, validName);

		// when & then
		DtoValidation.validationMemberDto(MemberDtoMaxName); // 유효성 검증 성공
	}

	// 실패 케이스
	@Test
	@DisplayName("ID 실패 경계값 테스트 (ID가 0일 때)")
	void MemberDtoId_실패_경계값_테스트_0() {
		// given
		MemberDto invalidMemberDto = new MemberDto(0L, "John Doe");

		// when & then
		assertThatThrownBy(() -> DtoValidation.validationMemberDto(invalidMemberDto))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("MemberDtoId는 1이상의 값이어야 합니다.");
	}

	@Test
	@DisplayName("ID 실패 경계값 테스트 (ID가 음수일 때)")
	void MemberDtoId_실패_경계값_테스트_음수() {
		// given
		MemberDto invalidMemberDto = new MemberDto(-1L, "John Doe");

		// when & then
		assertThatThrownBy(() -> DtoValidation.validationMemberDto(invalidMemberDto))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("MemberDtoId는 1이상의 값이어야 합니다.");
	}

	@Test
	@DisplayName("이름 실패 경계값 테스트 (빈 문자열일 때)")
	void MemberDtoName_실패_경계값_테스트_빈_문자열() {
		// given
		MemberDto invalidMemberDto = new MemberDto(1L, "");

		// when & then
		assertThatThrownBy(() -> DtoValidation.validationMemberDto(invalidMemberDto))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("이름은 10자를 초과할 수 없습니다.");
	}

	@Test
	@DisplayName("이름 실패 경계값 테스트 (이름이 6자일 때)")
	void MemberDtoName_실패_경계값_테스트_6자() {
		// given
		String tooLongName = "A".repeat(11);
		MemberDto invalidMemberDto = new MemberDto(1L, tooLongName);

		// when & then
		assertThatThrownBy(() -> DtoValidation.validationMemberDto(invalidMemberDto))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("이름은 10자를 초과할 수 없습니다.");
	}
}
