package com.example.cleancode.application.useCase;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Collections;
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
import com.example.cleancode.application.service.LectureApplyService;
import com.example.cleancode.application.service.LectureInstanceService;
import com.example.cleancode.application.service.LectureService;
import com.example.cleancode.domain.LectureApply;
import com.example.cleancode.domain.LectureStatus;

class LectureApplyUseCaseServiceTest {

	@Mock
	private LectureService lectureService;

	@Mock
	private LectureInstanceService lectureInstanceService;

	@Mock
	private LectureApplyService lectureApplyService;

	@InjectMocks
	private LectureApplyUseCaseService lectureApplyUseCaseService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	/*
	 *
	 * 회원이 강의를 신청
	 *  1. 회원이 해당 강의 신청한 내역이 있는지 확인
	 *  2. 해당 강의의 현재 인원수 check
	 *  3. 해당 강의의 status 확인
	 *  4. 해당 강의의 날짜 check
	 *  5. 해당 강의 신청
	 *  6. 해당 강의 신청한 내역 검증
	 *  7. 완료
	 *
	 */

	@Test
	@DisplayName("회원이 강의를 신청")
	void 회원이_강의를_신청() {
		// given
		MemberDto memberDto = new MemberDto(1L, "Jane Doe");
		LectureDto lectureDto = new LectureDto(1L, "Clean Architecture", "John Doe");
		LectureInstanceDto lectureInstanceDto = new LectureInstanceDto(1L, LocalDate.of(2024, 11, 1),
			LocalDate.of(2024, 12, 2),
			30, 0, lectureDto, LectureStatus.OPEN); // 강의 인스턴스 생성

		// Mock repository response for existing application
		when(lectureApplyService.getLectureApplyInfoByMemberAndLectureInstance(memberDto.getId(),
			lectureInstanceDto.getId()))
			.thenReturn(Collections.emptyList()); // 신청 내역이 없다고 설정

		// Mock for the lecture instance
		when(lectureInstanceService.getLectureInstance(lectureInstanceDto.getId()))
			.thenReturn(lectureInstanceDto); // 강의 인스턴스를 반환하도록 설정

		// when
		lectureApplyUseCaseService.applyForLecture(memberDto, lectureInstanceDto); // 강의 신청 시도

		// then
		// 강의 신청이 완료되었으므로, 해당 신청 내역이 생성되었는지 확인
		verify(lectureApplyService).addLectureApplyInMember(any(LectureApplyDto.class)); // 강의 신청이 호출되었는지 확인
	}

	@Test
	@DisplayName("회원이 이미 신청한 강의에 재신청 시 예외 발생")
	void 회원이_이미_신청한_강의에_재신청_예외발생() {
		// given
		MemberDto memberDto = new MemberDto(1L, "Jane Doe");
		LectureDto lectureDto = new LectureDto(1L, "Clean Architecture", "John Doe");
		LectureInstanceDto lectureInstanceDto = new LectureInstanceDto(1L, LocalDate.of(2024, 11, 1),
			LocalDate.of(2024, 12, 2),
			30, 0, lectureDto, LectureStatus.OPEN); // 강의 인스턴스 생성

		// Mock repository response for existing application
		LectureApplyDto lectureApplyDto = new LectureApplyDto(1L, memberDto, lectureInstanceDto);
		when(lectureApplyService.getLectureApplyInfoByMemberAndLectureInstance(memberDto.getId(),
			lectureInstanceDto.getId()))
			.thenReturn(Collections.singletonList(lectureApplyDto)); // 신청 내역이 있다고 설정

		// when & then
		assertThatThrownBy(() -> lectureApplyUseCaseService.applyForLecture(memberDto, lectureInstanceDto))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("이미 신청한 강의입니다."); // 예외 메시지 검증
	}


	@Test
	@DisplayName("현재 인원수가 최대 수용 인원을 초과할 때 예외 발생")
	void 현재_인원수가_최대_수용_인원을_초과할때_예외발생() {
		// given
		MemberDto memberDto = new MemberDto(1L, "Jane Doe");
		LectureDto lectureDto = new LectureDto(1L, "Clean Architecture", "John Doe");
		LectureInstanceDto lectureInstanceDto = new LectureInstanceDto(1L, LocalDate.of(2024, 11, 1),
			LocalDate.of(2024, 12, 2),
			30, 30, lectureDto, LectureStatus.OPEN); // 강의 인스턴스 생성 (현재 인원수가 최대 수용 인원수와 같음)

		// Mock repository response for existing application
		when(lectureApplyService.getLectureApplyInfoByMemberAndLectureInstance(memberDto.getId(),
			lectureInstanceDto.getId()))
			.thenReturn(Collections.emptyList()); // 신청 내역이 없다고 설정

		// Mock for the lecture instance
		when(lectureInstanceService.getLectureInstance(lectureInstanceDto.getId()))
			.thenReturn(lectureInstanceDto); // 강의 인스턴스를 반환하도록 설정

		// when & then
		assertThatThrownBy(() -> lectureApplyUseCaseService.applyForLecture(memberDto, lectureInstanceDto))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("현재 인원수가 최대 수강 인원을 초과할 수 없습니다."); // 예외 메시지 검증
	}


	@Test
	@DisplayName("강의가 종료된 경우 예외 발생")
	void 강의가_종료된_경우_예외발생() {
		// given
		MemberDto memberDto = new MemberDto(1L, "Jane Doe");
		LectureDto lectureDto = new LectureDto(1L, "Clean Architecture", "John Doe");
		LectureInstanceDto lectureInstanceDto = new LectureInstanceDto(1L, LocalDate.of(2024, 11, 1),
			LocalDate.of(2024, 12, 2),
			30, 0, lectureDto, LectureStatus.CLOSE); // 강의 인스턴스 생성 (상태가 CLOSE)

		// Mock repository response for existing application
		when(lectureApplyService.getLectureApplyInfoByMemberAndLectureInstance(memberDto.getId(),
			lectureInstanceDto.getId()))
			.thenReturn(Collections.emptyList()); // 신청 내역이 없다고 설정

		// Mock for the lecture instance
		when(lectureInstanceService.getLectureInstance(lectureInstanceDto.getId()))
			.thenReturn(lectureInstanceDto); // 강의 인스턴스를 반환하도록 설정

		// when & then
		assertThatThrownBy(() -> lectureApplyUseCaseService.applyForLecture(memberDto, lectureInstanceDto))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("현재 신청할 수 없는 강의입니다."); // 예외 메시지 검증
	}

	@Test
	@DisplayName("강의 시작일이 오늘보다 이전일 때 예외 발생")
	void 강의시작일이오늘보다이전일때예외발생() {
		// given
		MemberDto memberDto = new MemberDto(1L, "Jane Doe");
		LectureDto lectureDto = new LectureDto(1L, "Clean Architecture", "John Doe");
		LectureInstanceDto lectureInstanceDto = new LectureInstanceDto(1L, LocalDate.of(2023, 9, 1),
			LocalDate.of(2024, 12, 2),
			30, 0, lectureDto, LectureStatus.OPEN); // 강의 인스턴스 생성 (시작일이 오늘보다 이전)

		// Mock repository response for existing application
		when(lectureApplyService.getLectureApplyInfoByMemberAndLectureInstance(memberDto.getId(),
			lectureInstanceDto.getId()))
			.thenReturn(Collections.emptyList()); // 신청 내역이 없다고 설정

		// Mock for the lecture instance
		when(lectureInstanceService.getLectureInstance(lectureInstanceDto.getId()))
			.thenReturn(lectureInstanceDto); // 강의 인스턴스를 반환하도록 설정

		// when & then
		assertThatThrownBy(() -> lectureApplyUseCaseService.applyForLecture(memberDto, lectureInstanceDto))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("강의 시작일은 오늘 이후여야 합니다."); // 예외 메시지 검증
	}

	@Test
	@DisplayName("강의 인스턴스 정보가 유효하지 않은 경우 예외 발생")
	void 강의인스턴스정보가유효하지않은경우예외발생() {
		// given
		MemberDto memberDto = new MemberDto(1L, "Jane Doe");
		LectureDto lectureDto = new LectureDto(1L, "Clean Architecture", "John Doe");
		LectureInstanceDto lectureInstanceDto = null; // 강의 인스턴스 정보가 null인 경우

		// then
		assertThatThrownBy(() -> lectureApplyUseCaseService.applyForLecture(memberDto, lectureInstanceDto))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("LectureInstance는 null일 수 없습니다."); // 예외 메시지 검증
	}


}