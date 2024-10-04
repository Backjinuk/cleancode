package com.example.cleancode.application.useCase;

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

import com.example.cleancode.adapter.in.dto.LectureInstanceDto;
import com.example.cleancode.application.service.LectureInstanceService;
import com.example.cleancode.domain.LectureStatus;

class LectureInstanceUseCaseServiceTest {

	@Mock
	private LectureInstanceService lectureInstanceService;

	@InjectMocks
	private LectureInstanceUseCaseService lectureInstanceUseCase;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	/*
	 * 성공 케이스:
	 * - 모든 강의 인스턴스 조회 테스트: 모든_강의_인스턴스_조회_테스트
	 * - 강의 인스턴스 등록 테스트: 강의_인스턴스_등록_테스트
	 * - 강의 인스턴스 수정 테스트: 강의_인스턴스_수정_테스트
	 * - 강의 인스턴스 삭제 테스트: 강의_인스턴스_삭제_테스트
	 *
	 * 실패 케이스:
	 * - 강의 인스턴스 등록 시, 이미 존재하는 경우 예외 발생: 강의_인스턴스_등록_시_이미_존재하는_경우_예외발생
	 * - 존재하지 않는 강의 인스턴스 삭제 시 예외 발생: 존재하지_않는_강의_인스턴스_삭제_시_예외발생
	 */


	@Test
	@DisplayName("모든 강의 인스턴스 조회 테스트")
	void 모든_강의_인스턴스_조회_테스트() {
		// given
		LectureInstanceDto lectureInstance1 = new LectureInstanceDto(1L, LocalDate.of(2024, 11, 1),
			LocalDate.of(2024, 12, 2), 30, 0, null, LectureStatus.OPEN);
		LectureInstanceDto lectureInstance2 = new LectureInstanceDto(2L, LocalDate.of(2024, 11, 1),
			LocalDate.of(2024, 12, 2), 30, 0, null, LectureStatus.OPEN);

		when(lectureInstanceService.getAllLectureInstance()).thenReturn(Arrays.asList(lectureInstance1, lectureInstance2));

		// when
		List<LectureInstanceDto> allInstances = lectureInstanceUseCase.getAllLectureInstances();

		// then
		assertThat(allInstances).hasSize(2);
	}

	@Test
	@DisplayName("강의 인스턴스 등록 테스트")
	void 강의_인스턴스_등록_테스트() {
		// given
		LectureInstanceDto lectureInstanceDto = new LectureInstanceDto(1L, LocalDate.of(2024, 11, 1),
			LocalDate.of(2024, 12, 2), 30, 0, null, LectureStatus.OPEN);

		when(lectureInstanceService.getLectureInstance(1L)).thenReturn(null); // 이미 존재하지 않음
		when(lectureInstanceService.addLectureInLectureInstance(any())).thenReturn(true);

		// when
		LectureInstanceDto result = lectureInstanceUseCase.addLectureInstance(lectureInstanceDto);

		// then
		assertThat(result).isEqualTo(lectureInstanceDto);
	}

	@Test
	@DisplayName("강의 인스턴스 등록 시, 이미 존재하는 경우 예외 발생")
	void 강의_인스턴스_등록_시_이미_존재하는_경우_예외발생() {
		// given
		LectureInstanceDto lectureInstanceDto = new LectureInstanceDto(1L, LocalDate.of(2024, 11, 1),
			LocalDate.of(2024, 12, 2), 30, 0, null, LectureStatus.OPEN);

		when(lectureInstanceService.getLectureInstance(1L)).thenReturn(lectureInstanceDto); // 이미 존재함

		// then
		assertThatThrownBy(() -> lectureInstanceUseCase.addLectureInstance(lectureInstanceDto))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("이미 존재하는 강의입니다.");
	}

	@Test
	@DisplayName("강의 인스턴스 수정 테스트")
	void 강의_인스턴스_수정_테스트() {
		// given
		LectureInstanceDto lectureInstanceDto = new LectureInstanceDto(1L, LocalDate.of(2024, 11, 1),
			LocalDate.of(2024, 12, 2), 30, 0, null, LectureStatus.OPEN);

		// when
		lectureInstanceUseCase.updateLectureInstance(lectureInstanceDto);

		// then
		verify(lectureInstanceService).updateLectureInLectureInstance(lectureInstanceDto);
	}

	@Test
	@DisplayName("강의 인스턴스 삭제 테스트")
	void 강의_인스턴스_삭제_테스트() {
		// given
		Long lectureInstanceId = 1L;
		when(lectureInstanceService.deleteLectureInLectureInstance(lectureInstanceId)).thenReturn(true); // 삭제 성공

		// when
		boolean result = lectureInstanceUseCase.deleteLectureInstance(lectureInstanceId);

		// then
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("존재하지 않는 강의 인스턴스 삭제 시 예외 발생")
	void 존재하지_않는_강의_인스턴스_삭제_시_예외발생() {
		// given
		Long lectureInstanceId = 1L;
		when(lectureInstanceService.deleteLectureInLectureInstance(any())).thenThrow(new NullPointerException("해당 강의 인스턴스는 존재하지 않습니다.")); // 삭제 실패

		// then
		assertThatThrownBy(() -> lectureInstanceUseCase.deleteLectureInstance(lectureInstanceId))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("해당 강의 인스턴스는 존재하지 않습니다."); // 예외 메시지 검증
	}
}
