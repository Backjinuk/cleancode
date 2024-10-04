package com.example.cleancode.application.service;

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
import com.example.cleancode.application.mapper.LectureMapper;
import com.example.cleancode.application.useCase.repository.LectureRepository;

class LectureServiceTest {

	/*
	 *
	 * 성공 케이스:
	 * 1. 모든 강의 조회 테스트
	 * 2. 특정 강의 조회 테스트
	 * 3. 강의 조회 시, 비어있는 리스트 확인
	 * 4. 강의 등록 테스트
	 * 5. 강의 업데이트 테스트
	 * 6. 강의 삭제 테스트
	 *
	 * 실패 케이스:
	 * 1. 강의 조회 실패 테스트
	 * 2. 강의 등록 실패 테스트
	 * 3. 강의 등록 시, 이미 존재하는 강의에 대한 예외 발생
	 * 4. 강의 업데이트 시, 존재하지 않는 강의에 대한 예외 발생
	 * 5. 강의 삭제 시, 존재하지 않는 강의에 대한 예외 발생
	 * 6. 강의 등록 시, 제목이 비어 있을 경우 예외 발생
	 */

	@Mock
	private LectureRepository lectureRepository;  // Mock 객체로 의존성 설정

	@InjectMocks
	private LectureService lectureService;  // InjectMocks로 의존성 주입

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);  // Mock 객체 초기화
	}

	// 성공 케이스

	@Test
	@DisplayName("모든 강의 조회 테스트")
	void getAllLecturesTest() {
		// given
		LectureDto lecture1 = new LectureDto(1L, "Clean Architecture", "향해99");
		LectureDto lecture2 = new LectureDto(2L, "Domain-Driven Design", "향해99");
		when(lectureRepository.getAllLectureList()).thenReturn(
			Arrays.asList(LectureMapper.lectureDtoToEntity(lecture1), LectureMapper.lectureDtoToEntity(lecture2)));

		// when
		List<LectureDto> lectures = lectureService.getAllLectureList();

		// then
		assertThat(lectures).hasSize(2);
		assertThat(lectures).extracting("title").contains("Clean Architecture", "Domain-Driven Design");
	}

	@Test
	@DisplayName("특정 강의 조회 테스트")
	void getLectureByIdTest() {
		// given
		LectureDto lecture = new LectureDto(1L, "Clean Architecture", "향해99");
		when(lectureRepository.getLectureById(1L)).thenReturn(LectureMapper.lectureDtoToEntity(lecture));

		// when
		LectureDto foundLecture = lectureService.getLectureById(1L);

		// then
		assertThat(foundLecture).isNotNull();
		assertThat(foundLecture.getId()).isEqualTo(1L);
		assertThat(foundLecture.getTitle()).isEqualTo("Clean Architecture");
	}

	@Test
	@DisplayName("강의 조회 시, 비어있는 리스트 확인")
	void 강의_조회_시_빈리스트_확인() {
		// given
		when(lectureRepository.getAllLectureList()).thenReturn(List.of()); // 빈 리스트 반환

		// when
		List<LectureDto> lectures = lectureService.getAllLectureList();

		// then
		assertThat(lectures).isEmpty(); // 빈 리스트인지 확인
	}

	@Test
	@DisplayName("강의 등록 테스트")
	void 강의_등록_테스트() {
		// given
		LectureDto lecture = new LectureDto(1L, "Clean Architecture", "향해99"); // ID는 null
		when(lectureRepository.addLecture(any())).thenReturn(LectureMapper.lectureDtoToEntity(lecture)); // 강의가 등록된다고 가정

		// when
		LectureDto savedLecture = lectureService.addLecture(lecture);

		// then
		assertThat(savedLecture).isNotNull();
		assertThat(savedLecture.getTitle()).isEqualTo("Clean Architecture");
	}

	@Test
	@DisplayName("강의 업데이트 테스트")
	void 강의_업데이트_테스트() {
		// given
		LectureDto existingLecture = new LectureDto(1L, "Clean Architecture", "향해99");
		when(lectureRepository.getLectureById(1L)).thenReturn(LectureMapper.lectureDtoToEntity(existingLecture));

		// when
		existingLecture.setTitle("Updated Title"); // 강의 제목 업데이트
		when(lectureRepository.updateLecture(any())).thenReturn(LectureMapper.lectureDtoToEntity(existingLecture));

		// then
		LectureDto updatedLecture = lectureService.updateLecture(existingLecture);
		assertThat(updatedLecture.getTitle()).isEqualTo("Updated Title");
	}

	@Test
	@DisplayName("강의 삭제 테스트")
	void 강의_삭제_테스트() {
		// given
		Long lectureId = 1L;
		when(lectureRepository.deleteLecture(lectureId)).thenReturn(true); // 삭제 성공

		// when
		boolean isDeleted = lectureService.deleteLecture(lectureId);

		// then
		assertThat(isDeleted).isTrue(); // 삭제 여부 검증
	}

	// 실패 케이스

	@Test
	@DisplayName("강의 조회 실패 테스트")
	void lectureNotFoundTest() {
		// given
		when(lectureRepository.getLectureById(1L)).thenReturn(null);

		// when & then
		assertThatThrownBy(() -> lectureService.getLectureById(1L)).isInstanceOf(NullPointerException.class)
			.hasMessage("강의를 조회할 수 없습니다.");
	}

	@Test
	@DisplayName("강의 등록 실패 테스트")
	void 강의_등록_실패_테스트() {
		// given
		LectureDto lecture = new LectureDto(1L, null, "향해99"); // 제목이 null인 강의

		// then
		assertThatThrownBy(() -> lectureService.addLecture(lecture)).isInstanceOf(NullPointerException.class)
			.hasMessage("강의 제목은 비어 있을 수 없습니다."); // 예외 메시지 검증
	}

	@Test
	@DisplayName("강의 등록 시, 이미 존재하는 강의에 대한 예외 발생")
	void 강의_등록_시_이미_존재하는_강의에_대한_예외발생() {
		// given
		LectureDto lecture = new LectureDto(1L, "Clean Architecture", "향해99");
		when(lectureRepository.addLecture(any())).thenThrow(new IllegalArgumentException("이미 존재하는 강의입니다."));

		// then
		assertThatThrownBy(() -> lectureService.addLecture(lecture)).isInstanceOf(IllegalArgumentException.class)
			.hasMessage("이미 존재하는 강의입니다."); // 예외 메시지 검증
	}

	@Test
	@DisplayName("강의 업데이트 시, 존재하지 않는 강의에 대한 예외 발생")
	void 강의_업데이트_시_존재하지_않는_강의에_대한_예외발생() {
		// given
		LectureDto lecture = new LectureDto(1L, "Clean Architecture", "향해99");
		when(lectureRepository.getLectureById(1L)).thenReturn(null); // 강의가 존재하지 않음

		// then
		assertThatThrownBy(() -> lectureService.updateLecture(lecture)).isInstanceOf(NullPointerException.class)
			.hasMessage("lecture의 값은 null일수 없습니다."); // 예외 메시지 검증
	}

	@Test
	@DisplayName("강의 삭제 시, 존재하지 않는 강의에 대한 예외 발생")
	void 강의_삭제_시_존재하지_않는_강의에_대한_예외발생() {
		// given
		Long lectureId = 1L;
		when(lectureRepository.deleteLecture(lectureId)).thenReturn(false); // 삭제 실패

		// then
		assertThatThrownBy(() -> lectureService.deleteLecture(lectureId)).isInstanceOf(IllegalArgumentException.class)
			.hasMessage("삭제할 강의가 존재하지 않습니다."); // 예외 메시지 검증
	}

	@Test
	@DisplayName("강의 등록 시, 제목이 비어 있을 경우 예외 발생")
	void 강의_등록_시_제목이_비어있을경우() {
		// given
		LectureDto lecture = new LectureDto(1L, "", "향해99"); // 제목이 빈 문자열인 강의

		// then
		assertThatThrownBy(() -> lectureService.addLecture(lecture)).isInstanceOf(NullPointerException.class)
			.hasMessage("강의 제목은 비어 있을 수 없습니다."); // 예외 메시지 검증
	}
}
