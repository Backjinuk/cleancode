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

import com.example.cleancode.application.useCase.repository.LectureRepository;
import com.example.cleancode.domain.Lecture;

class LectureServiceTest {

	@Mock
	private LectureRepository lectureRepository;  // Mock 객체로 의존성 설정

	@InjectMocks
	private LectureService lectureService;  // InjectMocks로 의존성 주입

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);  // Mock 객체 초기화
	}

	@Test
	@DisplayName("모든 강의 조회 테스트")
	void getAllLecturesTest() {
		// given
		Lecture lecture1 = new Lecture(1L, "Clean Architecture", "향해99");
		Lecture lecture2 = new Lecture(2L, "Domain-Driven Design", "향해99");
		when(lectureRepository.getAllLectureList()).thenReturn(Arrays.asList(lecture1, lecture2));

		// when
		List<Lecture> lectures = lectureService.getAllLectureList();

		// then
		assertThat(lectures).hasSize(2);
		assertThat(lectures).extracting("title").contains("Clean Architecture", "Domain-Driven Design");
	}

	@Test
	@DisplayName("특정 강의 조회 테스트")
	void getLectureByIdTest() {
		// given
		Lecture lecture = new Lecture(1L, "Clean Architecture", "향해99");
		when(lectureRepository.getLectureById(1L)).thenReturn(lecture);

		// when
		Lecture foundLecture = lectureService.getLectureById(1L);

		// then
		assertThat(foundLecture).isNotNull();
		assertThat(foundLecture.getId()).isEqualTo(1L);
		assertThat(foundLecture.getTitle()).isEqualTo("Clean Architecture");
	}

	@Test
	@DisplayName("강의 조회 실패 테스트")
	void lectureNotFoundTest() {
		// given
		when(lectureRepository.getLectureById(1L)).thenReturn(null);

		// when & then
		assertThatThrownBy(() -> lectureService.getLectureById(1L))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("강의를 조회할 수 없습니다.");
	}

}
