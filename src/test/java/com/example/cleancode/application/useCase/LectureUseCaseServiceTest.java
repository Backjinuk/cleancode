package com.example.cleancode.application.useCase;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.cleancode.adapter.in.dto.LectureDto;
import com.example.cleancode.application.service.LectureService;

class LectureUseCaseServiceTest {

	@Mock
	private LectureService lectureService;

	@InjectMocks
	private LectureUseCaseService lectureUseCaseService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	/*
	 * 성공 케이스:
	 * - 강의 등록 성공: 강의_등록_성공
	 * - 모든 강의 조회 성공: 모든_강의_조회_성공
	 * - 특정 강의 조회 성공: 특정_강의_조회_성공
	 * - 강의 삭제 성공: 강의_삭제_성공
	 *
	 * 실패 케이스:
	 * - 강의 조회 실패 시 예외 발생: 강의_조회_실패_시_예외발생
	 * - 강의 삭제 시, 존재하지 않는 강의에 대한 예외 발생: 강의_삭제_시_존재하지_않는_강의에_대한_예외발생
	 * - 강의 등록 시, 이미 존재하는 강의에 대한 예외 발생: 강의_등록_시_이미_존재하는_강의에_대한_예외발생
	 * - 강의 업데이트 시, 존재하지 않는 강의에 대한 예외 발생: 강의_업데이트_시_존재하지_않는_강의에_대한_예외발생
	 */

	@Test
	@DisplayName("강의 등록 성공")
	void 강의_등록_성공() {
		// given
		LectureDto lectureDto = new LectureDto(1L, "Clean Architecture", "John Doe"); // Mock lecture data
		when(lectureService.addLecture(lectureDto)).thenReturn(lectureDto); // Mocking the service call

		// when
		LectureDto savedLecture = lectureUseCaseService.addLecture(lectureDto);

		// then
		assertThat(savedLecture).isNotNull(); // Ensure the returned object is not null
		assertThat(savedLecture.getId()).isEqualTo(1L); // Check ID
		assertThat(savedLecture.getTitle()).isEqualTo("Clean Architecture"); // Check title
		assertThat(savedLecture.getInstructor()).isEqualTo("John Doe"); // Check instructor name
		verify(lectureService).addLecture(lectureDto); // Verify service method was called
	}

	@Test
	@DisplayName("모든 강의 조회 성공")
	void 모든_강의_조회_성공() {
		// given
		LectureDto lecture1 = new LectureDto(1L, "Clean Architecture", "John Doe");
		LectureDto lecture2 = new LectureDto(2L, "Domain-Driven Design", "Jane Doe");
		when(lectureService.getAllLectureList()).thenReturn(Arrays.asList(lecture1, lecture2)); // Mocking

		// when
		List<LectureDto> lectures = lectureUseCaseService.getAllLectureList();

		// then
		assertThat(lectures).hasSize(2); // Ensure there are two lectures
		assertThat(lectures).extracting("title").contains("Clean Architecture", "Domain-Driven Design"); // Check titles
		verify(lectureService).getAllLectureList(); // Verify service method was called
	}

	@Test
	@DisplayName("특정 강의 조회 성공")
	void 특정_강의_조회_성공() {
		// given
		LectureDto lectureDto = new LectureDto(1L, "Clean Architecture", "John Doe");
		when(lectureService.getLectureById(1L)).thenReturn(lectureDto); // Mocking

		// when
		LectureDto foundLecture = lectureUseCaseService.getLectureById(1L);

		// then
		assertThat(foundLecture).isNotNull(); // Ensure the returned object is not null
		assertThat(foundLecture.getId()).isEqualTo(1L); // Check ID
		assertThat(foundLecture.getTitle()).isEqualTo("Clean Architecture"); // Check title
		assertThat(foundLecture.getInstructor()).isEqualTo("John Doe"); // Check instructor name
		verify(lectureService).getLectureById(1L); // Verify service method was called
	}

	@Test
	@DisplayName("강의 조회 실패 시 예외 발생")
	void 강의_조회_실패_시_예외발생() {
		// given
		when(lectureService.getLectureById(1L)).thenThrow(new NullPointerException("강의를 조회할 수 없습니다.")); // 강의가 존재하지 않음

		// when & then
		assertThatThrownBy(() -> lectureUseCaseService.getLectureById(1L))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("강의를 조회할 수 없습니다."); // 예외 메시지 검증
	}

	@Test
	@DisplayName("강의 삭제 성공")
	void 강의_삭제_성공() {
		// given
		long lectureId = 1L;
		when(lectureService.deleteLecture(lectureId)).thenReturn(true); // Mocking

		// when
		boolean isDeleted = lectureUseCaseService.deleteLecture(lectureId);

		// then
		assertThat(isDeleted).isTrue(); // Ensure the delete operation was successful
		verify(lectureService).deleteLecture(lectureId); // Verify service method was called
	}

	@Test
	@DisplayName("강의 삭제 시, 존재하지 않는 강의에 대한 예외 발생")
	void 강의_삭제_시_존재하지_않는_강의에_대한_예외발생() {
		// given
		Long lectureId = 1L;
		when(lectureService.deleteLecture(any())).thenThrow(new IllegalArgumentException("삭제할 강의가 존재하지 않습니다.")); // 강의가 존재하지 않음
		 // Mocking delete failure

		// then
		assertThatThrownBy(() -> lectureUseCaseService.deleteLecture(lectureId))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("삭제할 강의가 존재하지 않습니다."); // 예외 메시지 검증
	}

	@Test
	@DisplayName("강의 등록 시, 이미 존재하는 강의에 대한 예외 발생")
	void 강의_등록_시_이미_존재하는_강의에_대한_예외발생() {
		// given
		LectureDto lectureDto = new LectureDto(1L, "Clean Architecture", "John Doe");
		when(lectureService.addLecture(lectureDto)).thenThrow(new IllegalArgumentException("이미 존재하는 강의입니다."));

		// then
		assertThatThrownBy(() -> lectureUseCaseService.addLecture(lectureDto))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("이미 존재하는 강의입니다."); // 예외 메시지 검증
	}

	@Test
	@DisplayName("강의 업데이트 시, 존재하지 않는 강의에 대한 예외 발생")
	void 강의_업데이트_시_존재하지_않는_강의에_대한_예외발생() {
		// given
		LectureDto lectureDto = new LectureDto(1L, "Clean Architecture", "John Doe");
		when(lectureService.updateLecture(lectureDto)).thenThrow(new NullPointerException("lecture의 값은 null일수 없습니다.")); // 강의가 존재하지 않음

		// then
		assertThatThrownBy(() -> lectureUseCaseService.updateLecture(lectureDto))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("lecture의 값은 null일수 없습니다."); // 예외 메시지 검증
	}
}
