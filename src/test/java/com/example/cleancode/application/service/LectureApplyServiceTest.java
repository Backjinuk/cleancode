package com.example.cleancode.application.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.cleancode.adapter.in.dto.LectureApplyDto;
import com.example.cleancode.adapter.in.dto.LectureDto;
import com.example.cleancode.adapter.in.dto.LectureInstanceDto;
import com.example.cleancode.adapter.in.dto.MemberDto;
import com.example.cleancode.application.mapper.LectureApplyMapper;
import com.example.cleancode.application.mapper.LectureInstanceMapper;
import com.example.cleancode.application.mapper.MemberMapper;
import com.example.cleancode.application.useCase.repository.LectureApplyRepository;
import com.example.cleancode.domain.LectureApply;
import com.example.cleancode.domain.LectureStatus;

class LectureApplyServiceTest {

	/*
	 * 성공 케이스:
	 * 1. 강의 신청 성공
	 * 2. 강의 신청 이력 확인
	 * 3. 강의 신청 이력 확인 - 여러 이력
	 * 4. 강의 신청 후 이력 확인
	 * 5. 회원의 강의 신청 이력에서 특정 강의 찾기
	 *
	 * 실패 케이스:
	 * 1. 이미 신청한 강의에 재신청 시 예외 발생
	 * 2. 유효하지 않은 강의 신청 시 예외 발생
	 * 3. 강의 신청 실패 - 회원 정보가 없음
	 * 4. 강의 신청 실패 - 강의 인스턴스 정보가 없음
	 * 5. 강의 신청 이력 확인 - 없을 때
	 * 6. 강의 신청 실패 - 강의가 종료된 경우
	 */

	@Mock
	private LectureApplyRepository lectureApplyRepository;

	@InjectMocks
	private LectureApplyService lectureApplyService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	// 성공 케이스
	@Test
	@DisplayName("강의 신청 성공")
	void 강의_신청_성공() {
		// given
		LectureDto lectureDto = new LectureDto(1L, "Clean Architecture", "John Doe");
		LectureInstanceDto lectureInstanceDto = new LectureInstanceDto(1L, LocalDate.of(2024, 1, 1),
			LocalDate.of(2024, 3, 2), 30, 0, lectureDto, LectureStatus.OPEN); // 강의 인스턴스 DTO 생성
		MemberDto memberDto = new MemberDto(1L, "Jane Doe");
		LectureApplyDto lectureApplyDto = new LectureApplyDto(1L, memberDto, lectureInstanceDto);

		// when
		when(lectureApplyRepository.addLectureApplyInMember(any())).thenReturn(
			LectureApplyMapper.lectureApplyDtoToEntity(lectureApplyDto));
		LectureApplyDto savedApply = lectureApplyService.addLectureApplyInMember(lectureApplyDto);

		// then
		assertThat(savedApply).isNotNull();
		assertThat(savedApply.getId()).isEqualTo(lectureApplyDto.getId());
	}

	@Test
	@DisplayName("강의 신청 이력 확인")
	void 강의_신청_이력_확인() {
		// given
		LectureDto lectureDto = new LectureDto(1L, "Clean Architecture", "John Doe");
		LectureInstanceDto lectureInstanceDto = new LectureInstanceDto(1L, LocalDate.of(2024, 1, 1),
			LocalDate.of(2024, 3, 2), 30, 0, lectureDto, LectureStatus.OPEN);
		MemberDto memberDto = new MemberDto(1L, "Jane Doe");
		LectureApplyDto lectureApplyDto = new LectureApplyDto(1L, memberDto, lectureInstanceDto);

		// when
		when(lectureApplyRepository.getLectureApplyInfoByMember(memberDto.getId())).thenReturn(
			List.of(LectureApplyMapper.lectureApplyDtoToEntity(lectureApplyDto)));
		List<LectureApplyDto> history = lectureApplyService.getLectureApplyInfoByMember(memberDto.getId());

		// then
		assertThat(history).hasSize(1);
		assertThat(history.get(0).getLectureInstanceDto().getId()).isEqualTo(lectureInstanceDto.getId());
	}

	@Test
	@DisplayName("회원이 신청한 특정 강의 이력 확인")
	void 강의_신청한_특정_강의_이력_확인() {
		// given
		LectureDto lectureDto = new LectureDto(1L, "Clean Architecture", "John Doe");
		LectureInstanceDto lectureInstanceDto = new LectureInstanceDto(1L, LocalDate.of(2024, 1, 1),
			LocalDate.of(2024, 3, 2), 30, 0, lectureDto, LectureStatus.OPEN);
		MemberDto memberDto = new MemberDto(1L, "Jane Doe");
		LectureApplyDto lectureApplyDto = new LectureApplyDto(1L, memberDto, lectureInstanceDto);

		// when
		when(lectureApplyRepository.getLectureApplyInfoByMemberAndLectureInstance(memberDto.getId(),
			lectureInstanceDto.getId())).thenReturn(
			List.of(LectureApplyMapper.lectureApplyDtoToEntity(lectureApplyDto)));
		List<LectureApplyDto> history = lectureApplyService.getLectureApplyInfoByMemberAndLectureInstance(
			memberDto.getId(), lectureInstanceDto.getId());

		// then
		assertThat(history).hasSize(1);
		assertThat(history.get(0).getLectureInstanceDto().getId()).isEqualTo(lectureInstanceDto.getId());
	}

	@Test
	@DisplayName("강의 신청 이력 확인 - 여러 이력")
	void 강의_신청_이력_확인_여러_이력() {
		// given
		LectureDto lectureDto = new LectureDto(1L, "Clean Architecture", "John Doe");
		MemberDto memberDto = new MemberDto(1L, "Jane Doe");
		LectureInstanceDto lectureInstanceDto = new LectureInstanceDto(1L, LocalDate.of(2024, 1, 1),
			LocalDate.of(2024, 3, 2), 30, 0, lectureDto, LectureStatus.OPEN);

		LectureApplyDto lectureApplyDto1 = new LectureApplyDto(1L, memberDto, lectureInstanceDto);
		LectureApplyDto lectureApplyDto2 = new LectureApplyDto(2L, memberDto, lectureInstanceDto);

		// when
		when(lectureApplyRepository.getLectureApplyInfoByMember(memberDto.getId())).thenReturn(
			Arrays.asList(LectureApplyMapper.lectureApplyDtoToEntity(lectureApplyDto1),
				LectureApplyMapper.lectureApplyDtoToEntity(lectureApplyDto2)));
		List<LectureApplyDto> history = lectureApplyService.getLectureApplyInfoByMember(memberDto.getId());

		// then
		assertThat(history).hasSize(2);
	}

	// 실패 케이스
	@Test
	@DisplayName("이미 신청한 강의에 재신청 시 예외 발생")
	void 이미_신청한_강의에_재신청_시_예외발생() {
		// given
		LectureDto lectureDto = new LectureDto(1L, "Clean Architecture", "John Doe");
		LectureInstanceDto lectureInstanceDto = new LectureInstanceDto(1L, LocalDate.of(2024, 1, 1),
			LocalDate.of(2024, 3, 2), 30, 0, lectureDto, LectureStatus.OPEN);
		MemberDto memberDto = new MemberDto(1L, "Jane Doe");
		LectureApplyDto lectureApplyDto = new LectureApplyDto(1L, memberDto, lectureInstanceDto);

		// when
		when(lectureApplyRepository.validateLectureApply(any(), any())).thenReturn(true);

		// then
		assertThatThrownBy(() -> lectureApplyService.validateLectureApply(lectureApplyDto)).isInstanceOf(
			IllegalArgumentException.class).hasMessage("이미 신청한 강의입니다.");
	}

	@Test
	@DisplayName("유효하지 않은 강의 신청 시 예외 발생")
	void 유효하지_않은_강의_신청_시_예외발생() {
		// given
		LectureInstanceDto lectureInstanceDto = new LectureInstanceDto(1L, LocalDate.of(2024, 1, 1),
			LocalDate.of(2024, 3, 2), 30, 0, null, LectureStatus.OPEN); // 강의 정보가 없음
		MemberDto memberDto = new MemberDto(1L, "Jane Doe");
		LectureApplyDto lectureApplyDto = new LectureApplyDto(1L, memberDto, lectureInstanceDto); // 강의 정보가 없음

		// then
		assertThatThrownBy(() -> lectureApplyService.addLectureApplyInMember(lectureApplyDto)).isInstanceOf(
			NullPointerException.class).hasMessage("lectureDto의 값은 null일 수 없습니다.");
	}

	@Test
	@DisplayName("강의 신청 실패 - 회원 정보가 없음")
	void 강의_신청_실패_회원_정보가_없음() {
		// given
		LectureDto lectureDto = new LectureDto(1L, "Clean Architecture", "John Doe");
		LectureInstanceDto lectureInstanceDto = new LectureInstanceDto(1L, LocalDate.of(2024, 1, 1),
			LocalDate.of(2024, 3, 2), 30, 0, lectureDto, LectureStatus.OPEN);
		LectureApplyDto lectureApplyDto = new LectureApplyDto(1L, null, lectureInstanceDto); // 회원 정보 없음

		// then
		assertThatThrownBy(() -> lectureApplyService.addLectureApplyInMember(lectureApplyDto)).isInstanceOf(
			NullPointerException.class).hasMessage("memberDto의 값은 null일수 없습니다.");
	}

	@Test
	@DisplayName("강의 신청 실패 - 강의 인스턴스 정보가 없음")
	void 강의_신청_실패_강의_인스턴스_정보가_없음() {
		// given
		MemberDto memberDto = new MemberDto(1L, "Jane Doe");
		LectureApplyDto lectureApplyDto = new LectureApplyDto(1L, memberDto, null); // 강의 정보 없음

		// then
		assertThatThrownBy(() -> lectureApplyService.addLectureApplyInMember(lectureApplyDto)).isInstanceOf(
			NullPointerException.class).hasMessage("LectureInstanceDto는 null일 수 없습니다.");
	}

	@Test
	@DisplayName("강의 신청 이력 확인 - 없을 때")
	void 강의_신청_이력_확인_없을때() {
		// given
		MemberDto memberDto = new MemberDto(1L, "Jane Doe");

		// when
		when(lectureApplyRepository.getLectureApplyInfoByMember(memberDto.getId())).thenReturn(List.of());
		List<LectureApplyDto> history = lectureApplyService.getLectureApplyInfoByMember(memberDto.getId());

		// then
		assertThat(history).isEmpty();
	}

	@Test
	@DisplayName("강의 신청 실패 - 강의가 종료된 경우")
	void 강의_신청_실패_강의가_종료된_경우() {
		// given
		LectureDto lectureDto = new LectureDto(1L, "Clean Architecture", "John Doe");
		LectureInstanceDto lectureInstanceDto = new LectureInstanceDto(1L, LocalDate.of(2023, 1, 1),
			LocalDate.of(2023, 3, 2), // 이미 종료된 강의
			30, 0, lectureDto, LectureStatus.CLOSE); // 강의 종료 상태
		MemberDto memberDto = new MemberDto(1L, "Jane Doe");
		LectureApplyDto lectureApplyDto = new LectureApplyDto(1L, memberDto, lectureInstanceDto);

		// then
		assertThatThrownBy(() -> lectureApplyService.addLectureApplyInMember(lectureApplyDto)).isInstanceOf(
			IllegalArgumentException.class).hasMessage("종료된 강의에는 신청할 수 없습니다.");
	}

	@Test
	@DisplayName("강의 신청 후 이력 확인")
	void 강의_신청_후_이력_확인() {
		// given
		LectureDto lectureDto = new LectureDto(1L, "Clean Architecture", "John Doe");
		LectureInstanceDto lectureInstanceDto = new LectureInstanceDto(1L, LocalDate.of(2024, 1, 1),
			LocalDate.of(2024, 3, 2), 30, 0, lectureDto, LectureStatus.OPEN);
		MemberDto memberDto = new MemberDto(1L, "Jane Doe");
		LectureApplyDto lectureApplyDto = new LectureApplyDto(1L, memberDto, lectureInstanceDto);

		LectureApply lectureApply = new LectureApply(1L, MemberMapper.memberDtoToEntity(memberDto),
			LectureInstanceMapper.lectureInstanceDtoToEntity(lectureInstanceDto));

		// when
		// 예시: Mock 설정
		when(lectureApplyRepository.addLectureApplyInMember(any())).thenReturn(lectureApply);

		//when(lectureApplyRepository.addLectureApplyInMember(LectureApplyMapper.lectureApplyDtoToEntity(lectureApplyDto))).thenReturn(lectureApply);
		lectureApplyService.addLectureApplyInMember(lectureApplyDto); // 신청 성공

		// 신청 후 이력 확인
		when(lectureApplyRepository.getLectureApplyInfoByMember(memberDto.getId())).thenReturn(
			List.of(LectureApplyMapper.lectureApplyDtoToEntity(lectureApplyDto)));
		List<LectureApplyDto> history = lectureApplyService.getLectureApplyInfoByMember(memberDto.getId());

		// then
		assertThat(history).hasSize(1);
		assertThat(history.get(0).getLectureInstanceDto().getId()).isEqualTo(lectureInstanceDto.getId());
	}

	@Test
	@DisplayName("회원의 강의 신청 이력에서 특정 강의 찾기")
	void 회원의_강의_신청_이력에서_특정_강의_찾기() {
		// given
		LectureDto lectureDto = new LectureDto(1L, "Clean Architecture", "John Doe");
		LectureInstanceDto lectureInstanceDto = new LectureInstanceDto(1L, LocalDate.of(2024, 1, 1),
			LocalDate.of(2024, 3, 2), 30, 0, lectureDto, LectureStatus.OPEN);
		MemberDto memberDto = new MemberDto(1L, "Jane Doe");
		LectureApplyDto lectureApplyDto = new LectureApplyDto(1L, memberDto, lectureInstanceDto);

		// when
		when(lectureApplyRepository.getLectureApplyInfoByMember(memberDto.getId())).thenReturn(
			List.of(LectureApplyMapper.lectureApplyDtoToEntity(lectureApplyDto)));
		List<LectureApplyDto> history = lectureApplyService.getLectureApplyInfoByMember(memberDto.getId());

		// then
		assertThat(history).hasSize(1);
		assertThat(history.get(0).getLectureInstanceDto().getId()).isEqualTo(lectureInstanceDto.getId());
	}
}
