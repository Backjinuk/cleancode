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

import com.example.cleancode.adapter.in.dto.LectureInstanceDto;
import com.example.cleancode.application.useCase.repository.LectureInstanceRepository;
import com.example.cleancode.domain.Lecture;
import com.example.cleancode.domain.LectureInstance;
import com.example.cleancode.domain.LectureStatus;

class LectureInstanceServiceTest {

	@Mock
	LectureInstanceRepository lectureInstanceRepository;

	@InjectMocks
	LectureInstanceService lectureInstanceService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	/*
	 * 성공 케이스:
	 * 강의 목록 불러오기: 모든 강의 인스턴스를 성공적으로 불러오기.
	 * 강의 등록하기: 새로운 강의 인스턴스를 등록하는 경우.
	 * 강의의 현재 인원수 증가: 강의 인스턴스의 현재 수강 인원수를 증가시키는 경우.
	 * 강의 인스턴스 조회하기: 특정 강의 인스턴스를 ID로 조회하는 경우.
	 *
	 * 없는 강의 불러오기: 존재하지 않는 ID로 강의 인스턴스를 조회할 경우.
	 * 현재 인원수가 최대 인원수를 초과: 강의 인스턴스의 현재 인원수가 최대 수용 인원을 초과하려는 경우.
	 * 강의 인스턴스 등록 실패: 필수 값이 누락된 상태로 강의 인스턴스를 등록하려 할 경우 (예: 시작일 또는 종료일이 null인 경우).
	 * 강의 종료일이 시작일보다 이전일 때: 종료일이 시작일보다 이전일 때 강의 인스턴스를 등록하려는 경우.
	 *
	 * */

	@Test
	@DisplayName("강의 목록 불러오기")
	void 강의_목록_불러오기() {
		// given
		Lecture lecture1 = new Lecture(1L, "Clean Architecture1", "향해99");
		Lecture lecture2 = new Lecture(2L, "Clean Architecture2", "향해99");

		LectureInstance lectureInstance1 = new LectureInstance(1L, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 3, 2),
			30, 0, lecture1, LectureStatus.OPEN);

		LectureInstance lectureInstance2 = new LectureInstance(2L, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 3, 2),
			30, 0, lecture1, LectureStatus.OPEN);

		LectureInstance lectureInstance3 = new LectureInstance(3L, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 3, 2),
			30, 0, lecture2, LectureStatus.OPEN);

		LectureInstance lectureInstance4 = new LectureInstance(4L, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 3, 2),
			30, 0, lecture2, LectureStatus.OPEN);

		when(lectureInstanceRepository.getAllLectureInstance()).thenReturn(
			Arrays.asList(lectureInstance1, lectureInstance2, lectureInstance3, lectureInstance4));

		// when
		List<LectureInstanceDto> allLectureInstance = lectureInstanceService.getAllLectureInstance();

		// then
		assertThat(allLectureInstance).hasSize(4);

		// Verify each LectureInstanceDto
		assertThat(allLectureInstance).extracting("id").containsExactlyInAnyOrder(1L, 2L, 3L, 4L);
		assertThat(allLectureInstance).extracting("startDate")
			.containsExactlyInAnyOrder(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 1),
				LocalDate.of(2024, 1, 1));
		assertThat(allLectureInstance).extracting("endDate")
			.containsExactlyInAnyOrder(LocalDate.of(2024, 3, 2), LocalDate.of(2024, 3, 2), LocalDate.of(2024, 3, 2),
				LocalDate.of(2024, 3, 2));
		assertThat(allLectureInstance).extracting("maxParticipants").containsExactlyInAnyOrder(30, 30, 30, 30);
		assertThat(allLectureInstance).extracting("currentParticipants").containsExactlyInAnyOrder(0, 0, 0, 0);
		assertThat(allLectureInstance).extracting("lectureDto.id").containsExactlyInAnyOrder(1L, 1L, 2L, 2L);
	}

	@Test
	@DisplayName("강의를 강의목록에 등록하기")
	void 강의를_강의목록에_등록하기() {
		// given
		Lecture lecture1 = new Lecture(1L, "Clean Architecture", "향해99");

		LectureInstance lectureInstance = new LectureInstance(1L, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 3, 2),
			30, 0, lecture1, LectureStatus.OPEN);

		// when
		when(lectureInstanceRepository.addLectureInLectureInstance(lectureInstance)).thenReturn(true);
		boolean saveCheck = lectureInstanceService.getLectureInLectureInstance(lectureInstance);

		// then
		assertThat(saveCheck).isEqualTo(true);

	}

	@Test
	@DisplayName("특정_강의목록에_현재_인원수_증가")
	void test() {
		// given
		Lecture lecture1 = new Lecture(1L, "Clean Architecture1", "향해99");
		LectureInstance lectureInstance1 = new LectureInstance(1L, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 3, 2),
			30, 0, lecture1, LectureStatus.OPEN);


		LectureInstance updatedLectureInstance = new LectureInstance(1L, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 3, 2),
			30, 1, lecture1, LectureStatus.OPEN);

		when(lectureInstanceRepository.increnentCurrentParticpants(lectureInstance1)).thenReturn(updatedLectureInstance);

		// when
		LectureInstanceDto lectureInstanceDto = lectureInstanceService.incenentCurrentParticpants(lectureInstance1);

		// then
		assertThat(lectureInstanceDto).isNotNull();
		assertThat(lectureInstanceDto.getId()).isEqualTo(updatedLectureInstance.getId());
		assertThat(lectureInstanceDto.getCurrentParticipants()).isEqualTo(updatedLectureInstance.getCurrentParticipants());
	}

	@Test
	@DisplayName("특정 강의 인스턴스 불러오기")
	void 특정_강의_인스턴스_불러오기() {
		// given
		Lecture lecture1 = new Lecture(1L, "Clean Architecture1", "향해99");
		LectureInstance lectureInstance = new LectureInstance(1L, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 3, 2),
			30, 0, lecture1, LectureStatus.OPEN);

		when(lectureInstanceRepository.getLectureInstance(lectureInstance.getId())).thenReturn(lectureInstance);
		// when
		LectureInstanceDto lectureInstanceDto = lectureInstanceService.getLectureInstance(lectureInstance.getId());

		// then
		assertThat(lectureInstanceDto).isNotNull();
		assertThat(lectureInstanceDto.getId()).isEqualTo(lectureInstance.getId());
	}

	@Test
	@DisplayName("없는 강의 인스턴스 불러오기")
	void 없는_강의_인스턴스_불러오기() {
		// given
		long nonExistentId = 1L; // 존재하지 않는 ID
		when(lectureInstanceRepository.getLectureInstance(nonExistentId)).thenReturn(null); // null을 반환하도록 설정

		// when & then
		assertThatThrownBy(() -> lectureInstanceService.getLectureInstance(nonExistentId))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("해당 강의 인스턴스는 존재하지 않습니다."); // 예외 메시지 검증
	}

	@Test
	@DisplayName("현재_인원수가_최대_인원수를_초과할때_예외발생")
	void 현재_인원수가_최대_인원수를_초과할때_예외발생() {
		// given
		Lecture lecture1 = new Lecture(1L, "Clean Architecture1", "향해99");
		LectureInstance lectureInstance = new LectureInstance(1L, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 3, 2),
			30, 31, lecture1, LectureStatus.OPEN);

		// when

		// then

	}


}