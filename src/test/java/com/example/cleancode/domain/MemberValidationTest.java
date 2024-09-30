package com.example.cleancode.domain;

import static org.assertj.core.api.Assertions.*;

import com.example.cleancode.application.validation.EntityValidation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberValidationTest {

	/*
	 * MemberEntity
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
	void memberId_성공_경계값_최소값_테스트() {
		// given
		Member memberMin = new Member(1L, "John Doe");

		// when & then
		EntityValidation.validationMember(memberMin); // 유효성 검증 성공
	}

	@Test
	@DisplayName("ID 성공 경계값 테스트 (최대값)")
	void memberId_성공_경계값_최대값_테스트() {
		// given
		Member memberMax = new Member(Long.MAX_VALUE, "Jane Doe");

		// when & then
		EntityValidation.validationMember(memberMax); // 유효성 검증 성공
	}

	@Test
	@DisplayName("이름 성공 경계값 테스트 (최소 길이)")
	void memberName_성공_경계값_최소값_테스트() {
		// given
		Member memberMinName = new Member(1L, "A");

		// when & then
		EntityValidation.validationMember(memberMinName); // 유효성 검증 성공
	}

	@Test
	@DisplayName("이름 성공 경계값 테스트 (최대 길이)")
	void memberName_성공_경계값_최대값_테스트() {
		// given
		String validName = "ABCDE";
		Member memberMaxName = new Member(1L, validName);

		// when & then
		EntityValidation.validationMember(memberMaxName); // 유효성 검증 성공
	}

	// 실패 케이스
	@Test
	@DisplayName("ID 실패 경계값 테스트 (ID가 0일 때)")
	void memberId_실패_경계값_테스트_0() {
		// given
		Member invalidMember = new Member(0L, "John Doe");

		// when & then
		assertThatThrownBy(() -> EntityValidation.validationMember(invalidMember))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("memberId는 1이상의 값이어야 합니다.");
	}

	@Test
	@DisplayName("ID 실패 경계값 테스트 (ID가 음수일 때)")
	void memberId_실패_경계값_테스트_음수() {
		// given
		Member invalidMember = new Member(-1L, "John Doe");

		// when & then
		assertThatThrownBy(() -> EntityValidation.validationMember(invalidMember))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("memberId는 1이상의 값이어야 합니다.");
	}

	@Test
	@DisplayName("이름 실패 경계값 테스트 (빈 문자열일 때)")
	void memberName_실패_경계값_테스트_빈_문자열() {
		// given
		Member invalidMember = new Member(1L, "");

		// when & then
		assertThatThrownBy(() -> EntityValidation.validationMember(invalidMember))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("이름은 10자를 초과할 수 없습니다.");
	}

	@Test
	@DisplayName("이름 실패 경계값 테스트 (이름이 6자일 때)")
	void memberName_실패_경계값_테스트_6자() {
		// given
		String tooLongName = "A".repeat(11);
		Member invalidMember = new Member(1L, tooLongName);

		// when & then
		assertThatThrownBy(() -> EntityValidation.validationMember(invalidMember))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("이름은 10자를 초과할 수 없습니다.");
	}
}
