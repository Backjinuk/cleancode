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

import com.example.cleancode.adapter.in.dto.LectureDto;
import com.example.cleancode.adapter.in.dto.LectureInstanceDto;
import com.example.cleancode.application.mapper.LectureInstanceMapper;
import com.example.cleancode.application.useCase.repository.LectureInstanceRepository;
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
	 * 1. 강의 목록 불러오기
	 * 2. 강의를 강의목록에 등록하기
	 * 3. 특정_강의목록에_현재_인원수_증가
	 * 4. 특정 강의 인스턴스의 현재 인원수 감소
	 * 5. 특정 강의 인스턴스 불러오기
	 * 6. 강의 인스턴스 등록 실패 - 필수 값이 누락된 경우
	 * 7. 강의 종료일이 시작일보다 이전일 때
	 * 8. 현재 인원수 증가 후 최대 인원수 도달 테스트
	 * 9. 강의 인스턴스의 현재 인원수 감소 후 확인
	 * 10. 강의 목록 불러오기에서 반환되는 DTO의 검증
	 *
	 * 실패 케이스:
	 * 1. 현재 인원수가 음수가 될 때 예외 발생
	 * 2. 없는 강의 인스턴스 불러오기
	 * 3. 현재_인원수가_최대_인원수를_초과할때_예외발생
	 * 4. 강의 인스턴스 등록 실패 - 최대 수용 인원이 0일 때
	 * 5. 강의 시작일이 오늘보다 이전일 때 등록 실패
	 * 6. 유효하지 않은 강의 신청 시 예외 발생
	 */

	// 성공 케이스
	@Test
	@DisplayName("강의 목록 불러오기")
	void 강의_목록_불러오기() {
		// given
		LectureDto lecture1 = new LectureDto(1L, "Clean Architecture1", "향해99");
		LectureDto lecture2 = new LectureDto(2L, "Clean Architecture2", "향해99");

		LectureInstanceDto lectureInstance1 = new LectureInstanceDto(1L, LocalDate.of(2024, 11, 1),
			LocalDate.of(2024, 12, 2), 30, 0, lecture1, LectureStatus.OPEN);

		LectureInstanceDto lectureInstance2 = new LectureInstanceDto(2L, LocalDate.of(2024, 11, 1),
			LocalDate.of(2024, 12, 2), 30, 0, lecture1, LectureStatus.OPEN);

		LectureInstanceDto lectureInstance3 = new LectureInstanceDto(3L, LocalDate.of(2024, 11, 1),
			LocalDate.of(2024, 12, 2), 30, 0, lecture2, LectureStatus.OPEN);

		LectureInstanceDto lectureInstance4 = new LectureInstanceDto(4L, LocalDate.of(2024, 11, 1),
			LocalDate.of(2024, 12, 2), 30, 0, lecture2, LectureStatus.OPEN);

		when(lectureInstanceRepository.getAllLectureInstance()).thenReturn(
			Arrays.asList(LectureInstanceMapper.lectureInstanceDtoToEntity(lectureInstance1),
				LectureInstanceMapper.lectureInstanceDtoToEntity(lectureInstance2),
				LectureInstanceMapper.lectureInstanceDtoToEntity(lectureInstance3),
				LectureInstanceMapper.lectureInstanceDtoToEntity(lectureInstance4)));

		// when
		List<LectureInstanceDto> allLectureInstance = lectureInstanceService.getAllLectureInstance();

		// then
		assertThat(allLectureInstance).hasSize(4);

		// Verify each LectureInstanceDto
		assertThat(allLectureInstance).extracting("id").containsExactlyInAnyOrder(1L, 2L, 3L, 4L);
		assertThat(allLectureInstance).extracting("startDate")
			.containsExactlyInAnyOrder(LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 1),
				LocalDate.of(2024, 11, 1));
		assertThat(allLectureInstance).extracting("endDate")
			.containsExactlyInAnyOrder(LocalDate.of(2024, 12, 2), LocalDate.of(2024, 12, 2), LocalDate.of(2024, 12, 2),
				LocalDate.of(2024, 12, 2));
		assertThat(allLectureInstance).extracting("maxParticipants").containsExactlyInAnyOrder(30, 30, 30, 30);
		assertThat(allLectureInstance).extracting("currentParticipants").containsExactlyInAnyOrder(0, 0, 0, 0);
		assertThat(allLectureInstance).extracting("lectureDto.id").containsExactlyInAnyOrder(1L, 1L, 2L, 2L);
	}

	@Test
	@DisplayName("강의를 강의목록에 등록하기")
	void 강의를_강의목록에_등록하기() {
		// given
		LectureDto lecture1 = new LectureDto(1L, "Clean Architecture", "향해99");
		LectureInstanceDto lectureInstance = new LectureInstanceDto(1L, LocalDate.of(2024, 11, 1),
			LocalDate.of(2024, 12, 2), 30, 0, lecture1, LectureStatus.OPEN);

		// when
		when(lectureInstanceRepository.addLectureInstance(any())).thenReturn(true);
		boolean saveCheck = lectureInstanceService.addLectureInLectureInstance(lectureInstance);

		// then
		assertThat(saveCheck).isEqualTo(true);
	}

	@Test
	@DisplayName("특정_강의목록에_현재_인원수_증가")
	void 특정_강의목록에_현재_인원수_증가() {
		// given
		LectureDto lecture1 = new LectureDto(1L, "Clean Architecture1", "향해99");
		LectureInstanceDto lectureInstance1 = new LectureInstanceDto(1L, LocalDate.of(2024, 11, 1),
			LocalDate.of(2024, 12, 2), 30, 0, lecture1, LectureStatus.OPEN);

		LectureInstanceDto updatedLectureInstance = new LectureInstanceDto(1L, LocalDate.of(2024, 11, 1),
			LocalDate.of(2024, 12, 2), 30, 1, lecture1, LectureStatus.OPEN);

		when(lectureInstanceRepository.increnentCurrentParticpants(any())).thenReturn(
			LectureInstanceMapper.lectureInstanceDtoToEntity(updatedLectureInstance));

		// when
		LectureInstanceDto lectureInstanceDto = lectureInstanceService.incrementCurrentParticipants(lectureInstance1);

		// then
		assertThat(lectureInstanceDto).isNotNull();
		assertThat(lectureInstanceDto.getId()).isEqualTo(updatedLectureInstance.getId());
		assertThat(lectureInstanceDto.getCurrentParticipants()).isEqualTo(
			updatedLectureInstance.getCurrentParticipants());
	}

	@Test
	@DisplayName("특정 강의 인스턴스의 현재 인원수 감소")
	void 특정_강의_인스턴스의_현재_인원수_감소() {
		// given
		LectureDto lecture1 = new LectureDto(1L, "Clean Architecture1", "향해99");
		LectureInstanceDto lectureInstance = new LectureInstanceDto(1L, LocalDate.of(2024, 11, 1),
			LocalDate.of(2024, 12, 2), 30, 1, lecture1, LectureStatus.OPEN); // 현재 인원수 1

		LectureInstanceDto lectureInstance2 = new LectureInstanceDto(1L, LocalDate.of(2024, 11, 1),
			LocalDate.of(2024, 12, 2), 30, 0, lecture1, LectureStatus.OPEN);

		// when
		when(lectureInstanceRepository.decrementCurrentParticipants(any())).thenReturn(
			LectureInstanceMapper.lectureInstanceDtoToEntity(lectureInstance2));
		LectureInstanceDto lectureInstanceDto = lectureInstanceService.decrementCurrentParticipants(lectureInstance);

		// then
		assertThat(lectureInstanceDto).isNotNull();
		assertThat(lectureInstanceDto.getCurrentParticipants()).isEqualTo(0); // 현재 인원수 감소 확인
	}

	// 실패 케이스
	@Test
	@DisplayName("현재 인원수가 음수가 될 때 예외 발생")
	void 현재_인원수가_음수가_될때_예외발생() {
		// given
		LectureDto lecture1 = new LectureDto(1L, "Clean Architecture1", "향해99");
		LectureInstanceDto lectureInstance = new LectureInstanceDto(1L, LocalDate.of(2024, 11, 1),
			LocalDate.of(2024, 12, 2), 30, 0, lecture1, LectureStatus.OPEN); // 현재 인원수 0

		// when & then
		assertThatThrownBy(() -> lectureInstanceService.decrementCurrentParticipants(lectureInstance)).isInstanceOf(
			IllegalArgumentException.class).hasMessage("현재 수강 인원은 0 이상이어야 합니다."); // 예외 메시지 검증
	}

	@Test
	@DisplayName("특정 강의 인스턴스 불러오기")
	void 특정_강의_인스턴스_불러오기() {
		// given
		LectureDto lecture1 = new LectureDto(1L, "Clean Architecture1", "향해99");
		LectureInstanceDto lectureInstance = new LectureInstanceDto(1L, LocalDate.of(2024, 11, 1),
			LocalDate.of(2024, 12, 2), 30, 0, lecture1, LectureStatus.OPEN);

		when(lectureInstanceRepository.getLectureInstance(lectureInstance.getId())).thenReturn(
			LectureInstanceMapper.lectureInstanceDtoToEntity(lectureInstance));
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
		assertThatThrownBy(() -> lectureInstanceService.getLectureInstance(nonExistentId)).isInstanceOf(
			NullPointerException.class).hasMessage("해당 강의 인스턴스는 존재하지 않습니다."); // 예외 메시지 검증
	}

	@Test
	@DisplayName("현재_인원수가_최대_인원수를_초과할때_예외발생")
	void 현재_인원수가_최대_인원수를_초과할때_예외발생() {
		// given
		LectureDto lecture1 = new LectureDto(1L, "Clean Architecture1", "향해99");
		LectureInstanceDto lectureInstance = new LectureInstanceDto(1L, LocalDate.of(2024, 11, 1),
			LocalDate.of(2024, 12, 2), 30, 31, lecture1, LectureStatus.OPEN);

		// when & then
		assertThatThrownBy(() -> lectureInstanceService.incrementCurrentParticipants(lectureInstance)).isInstanceOf(
			IllegalArgumentException.class).hasMessage("현재 수강 인원은 최대 수강 인원을 초과할 수 없습니다.");
	}

	@Test
	@DisplayName("강의 인스턴스 등록 실패 - 필수 값이 누락된 경우")
	void 강의_인스턴스_등록_실패_필수값_누락() {
		// given
		LectureDto lecture1 = new LectureDto(1L, "Clean Architecture1", "향해99");
		// 시작일과 종료일이 null인 경우
		LectureInstanceDto lectureInstance = new LectureInstanceDto(1L, null, null, 30, 0, lecture1,
			LectureStatus.OPEN);

		// when & then
		assertThatThrownBy(() -> lectureInstanceService.addLectureInLectureInstance(lectureInstance)).isInstanceOf(
			NullPointerException.class).hasMessage("강의 시작일은 null일 수 없습니다."); // 예외 메시지 검증
	}

	@Test
	@DisplayName("강의 종료일이 시작일보다 이전일 때")
	void 강의_종료일이_시작일보다_이전일때() {
		// given
		LectureDto lecture1 = new LectureDto(1L, "Clean Architecture1", "향해99");
		LectureInstanceDto lectureInstance = new LectureInstanceDto(1L, LocalDate.of(2024, 3, 1),
			LocalDate.of(2024, 1, 1), 30, 0, lecture1, LectureStatus.OPEN);

		// when & then
		assertThatThrownBy(() -> lectureInstanceService.addLectureInLectureInstance(lectureInstance)).isInstanceOf(
			IllegalArgumentException.class).hasMessage("강의 종료일은 시작일보다 이후여야 합니다."); // 예외 메시지 검증
	}

	@Test
	@DisplayName("강의 인스턴스 등록 실패 - 최대 수용 인원이 0일 때")
	void 강의_인스턴스_등록_실패_최대수용인원이_0() {
		// given
		LectureDto lecture1 = new LectureDto(1L, "Clean Architecture1", "향해99");
		LectureInstanceDto lectureInstance = new LectureInstanceDto(1L, LocalDate.of(2024, 11, 1),
			LocalDate.of(2024, 12, 2), 0, 0, lecture1, LectureStatus.OPEN); // 최대 수용 인원 0

		// when & then
		assertThatThrownBy(() -> lectureInstanceService.addLectureInLectureInstance(lectureInstance)).isInstanceOf(
			IllegalArgumentException.class).hasMessage("최대 수강 인원은 1 이상이어야 합니다."); // 예외 메시지 검증
	}

	@Test
	@DisplayName("강의 인스턴스의 현재 인원수 증가 후 최대 인원수 도달 테스트")
	void 현재_인원수_증가_후_최대_인원수_도달_테스트() {
		// given
		LectureDto lecture1 = new LectureDto(1L, "Clean Architecture1", "향해99");
		LectureInstanceDto lectureInstance = new LectureInstanceDto(1L, LocalDate.of(2024, 11, 1),
			LocalDate.of(2024, 12, 2), 30, 30, lecture1, LectureStatus.OPEN); // 현재 인원수가 최대 인원수와 같음

		// when & then
		assertThatThrownBy(() -> lectureInstanceService.incrementCurrentParticipants(lectureInstance)).isInstanceOf(
			IllegalArgumentException.class).hasMessage("현재 수강 인원은 최대 수강 인원을 초과할 수 없습니다."); // 예외 메시지 검증
	}

	@Test
	@DisplayName("강의 인스턴스의 현재 인원수 감소 후 확인")
	void 강의_인스턴스의_현재_인원수_감소_후_확인() {
		// given
		LectureDto lecture1 = new LectureDto(1L, "Clean Architecture1", "향해99");
		LectureInstanceDto lectureInstance = new LectureInstanceDto(1L, LocalDate.of(2024, 11, 1),
			LocalDate.of(2024, 12, 2), 30, 1, lecture1, LectureStatus.OPEN); // 현재 인원수 1

		LectureInstanceDto lectureInstance2 = new LectureInstanceDto(1L, LocalDate.of(2024, 11, 1),
			LocalDate.of(2024, 12, 2), 30, 0, lecture1, LectureStatus.OPEN);

		// when
		when(lectureInstanceRepository.decrementCurrentParticipants(any())).thenReturn(
			LectureInstanceMapper.lectureInstanceDtoToEntity(lectureInstance2));
		LectureInstanceDto lectureInstanceDto = lectureInstanceService.decrementCurrentParticipants(lectureInstance);

		// then
		assertThat(lectureInstanceDto).isNotNull();
		assertThat(lectureInstanceDto.getCurrentParticipants()).isEqualTo(0); // 현재 인원수 감소 확인
	}

	@Test
	@DisplayName("강의 인스턴스 등록 시 유효성 검증 - 필수 필드 누락")
	void 강의_인스턴스_등록_실패_필수필드_누락() {
		// given
		LectureDto lecture1 = new LectureDto(1L, "Clean Architecture1", "향해99");
		LectureInstanceDto lectureInstance = new LectureInstanceDto(1L, null, null, 30, 0, lecture1,
			LectureStatus.OPEN); // 시작일과 종료일이 null인 경우

		// when & then
		assertThatThrownBy(() -> lectureInstanceService.addLectureInLectureInstance(lectureInstance)).isInstanceOf(
			NullPointerException.class).hasMessage("강의 시작일은 null일 수 없습니다."); // 예외 메시지 검증
	}

	@Test
	@DisplayName("강의 시작일이 오늘보다 이전일 때 등록 실패")
	void 강의_시작일이_오늘보다_이전일때_등록실패() {
		// given
		LectureDto lecture1 = new LectureDto(1L, "Clean Architecture1", "향해99");
		LectureInstanceDto lectureInstance = new LectureInstanceDto(1L, LocalDate.of(2023, 1, 1),
			LocalDate.of(2024, 1, 1), 30, 0, lecture1, LectureStatus.OPEN); // 시작일이 오늘보다 이전

		// when & then
		assertThatThrownBy(() -> lectureInstanceService.addLectureInLectureInstance(lectureInstance)).isInstanceOf(
			IllegalArgumentException.class).hasMessage("강의 시작일은 오늘 이후여야 합니다."); // 예외 메시지 검증
	}

	@Test
	@DisplayName("강의 목록 불러오기에서 반환되는 DTO의 검증")
	void 강의_목록_불러오기에서_반환되는_DTO_검증() {
		// given
		LectureDto lecture1 = new LectureDto(1L, "Clean Architecture1", "향해99");
		LectureInstanceDto lectureInstance1 = new LectureInstanceDto(1L, LocalDate.of(2024, 11, 1),
			LocalDate.of(2024, 12, 2), 30, 0, lecture1, LectureStatus.OPEN);

		when(lectureInstanceRepository.getAllLectureInstance()).thenReturn(
			List.of(LectureInstanceMapper.lectureInstanceDtoToEntity(lectureInstance1)));

		// when
		List<LectureInstanceDto> allLectureInstance = lectureInstanceService.getAllLectureInstance();

		// then
		assertThat(allLectureInstance).hasSize(1);
		assertThat(allLectureInstance.get(0).getId()).isEqualTo(lectureInstance1.getId());
		assertThat(allLectureInstance.get(0).getStartDate()).isEqualTo(lectureInstance1.getStartDate());
		assertThat(allLectureInstance.get(0).getEndDate()).isEqualTo(lectureInstance1.getEndDate());
	}

	@Test
	@DisplayName("강의 인스턴스 수정 시, 존재하지 않는 강의에 대한 예외 발생")
	void 강의_인스턴스_수정_시_존재하지_않는_강의에_대한_예외발생() {
		// given
		LectureDto lecture = new LectureDto(1L, "Clean Architecture1", "향해99");
		LectureInstanceDto lectureInstance = new LectureInstanceDto(1L, LocalDate.of(2024, 11, 1),
			LocalDate.of(2024, 12, 2), 30, 0, lecture, LectureStatus.OPEN);

		// when
		when(lectureInstanceRepository.updateLectureInLectureInstance(any())).thenReturn(null); // 강의가 존재하지 않음

		// then
		assertThatThrownBy(() -> lectureInstanceService.updateLectureInLectureInstance(lectureInstance)).isInstanceOf(
			NullPointerException.class).hasMessage("해당 강의 인스턴스는 존재하지 않습니다."); // 예외 메시지 검증
	}

	@Test
	@DisplayName("강의 인스턴스 삭제 시, 존재하지 않는 강의에 대한 예외 발생")
	void 강의_인스턴스_삭제_시_존재하지_않는_강의에_대한_예외발생() {
		// given
		Long lectureInstanceId = 1L;
		when(lectureInstanceRepository.deleteLectureInstance(any())).thenReturn(false); // 삭제 실패

		// then
		assertThatThrownBy(() -> lectureInstanceService.deleteLectureInLectureInstance(lectureInstanceId)).isInstanceOf(
			NullPointerException.class).hasMessage("해당 강의 인스턴스는 존재하지 않습니다."); // 예외 메시지 검증
	}

}
