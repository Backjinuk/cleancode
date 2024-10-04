package com.example.cleancode.application.useCase;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.cleancode.adapter.in.dto.LectureDto;
import com.example.cleancode.adapter.in.dto.LectureInstanceDto;
import com.example.cleancode.adapter.in.dto.MemberDto;
import com.example.cleancode.application.service.LectureInstanceService;
import com.example.cleancode.application.useCase.repository.LectureInstanceRepository;
import com.example.cleancode.domain.LectureInstance;
import com.example.cleancode.domain.LectureStatus;

@SpringBootTest
public class LectureApplyConcurrencyTest {

	@Autowired
	private LectureApplyUseCaseService lectureApplyUseCaseService;

	@Autowired
	private LectureInstanceRepository lectureInstanceRepository;

	@Autowired
	private LectureInstanceService lectureInstanceService;


	@DisplayName("40명이 신청했을 때, 30명만 성공")
	@Transactional
	@Test
	 void 회원_40명이_신청했을_때_30명만_성공() throws InterruptedException {
		// given: 강의 인스턴스 생성 및 최대 참가자 수 설정 (30명)
		LectureDto lecture1 = new LectureDto(1L, "Clean Architecture1", "허재");
		LectureInstanceDto lectureInstanceDto = new LectureInstanceDto(1L, LocalDate.of(2024, 11, 1),
			LocalDate.of(2024, 12, 2), 30, 0, lecture1, LectureStatus.OPEN);

		// given: List<MemberDto>로 40명의 회원 미리 설정
		List<MemberDto> members = setupDummyMembers();

		// when: 40명이 동시에 신청
		ExecutorService executorService = Executors.newFixedThreadPool(40);
		CountDownLatch latch = new CountDownLatch(40);  // 동시성 제어


		for (MemberDto member : members) {
			executorService.submit(() -> {
				try {
					// 각 회원이 동시에 강의에 신청을 시도
					lectureApplyUseCaseService.applyForLecture(member, lectureInstanceDto);
				} catch (Exception e) {
					// 신청 실패 시 (인원이 초과된 경우 예외 발생)
					System.out.println("신청 실패: " + e.getMessage());
				} finally {
					latch.countDown();  // 각 스레드가 작업을 마칠 때마다 latch 감소
				}
			});
		}

		// 모든 스레드가 종료될 때까지 대기
		latch.await(1000, TimeUnit.SECONDS);

		// then: 신청 성공자가 30명인지 확인 (AssertJ 사용)
		LectureInstanceDto updatedLectureInstance = lectureInstanceService.getLectureInstance(1L);
		assertThat(updatedLectureInstance.getCurrentParticipants()).isEqualTo(30);
	}

	// 40명의 더미 회원 데이터를 미리 설정하는 메서드
	private List<MemberDto> setupDummyMembers() {
		List<MemberDto> members = new ArrayList<>();
		for (int i = 1; i <= 40; i++) {
			// Member01 ~ Member40까지 더미 데이터 생성
			MemberDto member = new MemberDto((long) i, "Member" + i);
			members.add(member);
		}
		return members;
	}

	@DisplayName("동시 신청 중 일부 트랜잭션 실패 시 롤백 확인")
	@Transactional
	@Test
	void 동시_신청_중_트랜잭션_실패_롤백() throws InterruptedException {
		// given: 강의 인스턴스 설정
		LectureDto lecture1 = new LectureDto(1L, "Clean Architecture1", "허재");
		LectureInstanceDto lectureInstanceDto = new LectureInstanceDto(1L, LocalDate.of(2024, 11, 1),
			LocalDate.of(2024, 12, 2), 30, 0, lecture1, LectureStatus.OPEN);

		// 회원 설정
		List<MemberDto> members = setupDummyMembers();  // 40명의 회원 생성

		// 동시성 제어 설정
		ExecutorService executorService = Executors.newFixedThreadPool(40);
		CountDownLatch latch = new CountDownLatch(40);

		for (MemberDto member : members) {
			executorService.submit(() -> {
				try {
					lectureApplyUseCaseService.applyForLecture(member, lectureInstanceDto);
				} catch (Exception e) {
					System.out.println("신청 실패: " + e.getMessage());
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await(1000, TimeUnit.SECONDS);

		// then: 신청자 수가 여전히 30명인지 확인
		LectureInstanceDto updatedLectureInstance = lectureInstanceService.getLectureInstance(1L);
		assertThat(updatedLectureInstance.getCurrentParticipants()).isEqualTo(30);
	}


	@DisplayName("강의 상태가 CLOSED일 때 신청 불가")
	@Transactional
	@Test
	void 강의_상태가_CLOSED_일때_신청_불가() throws InterruptedException {
		// given: 강의 상태를 CLOSED로 설정
		LectureDto lecture1 = new LectureDto(1L, "Clean Architecture1", "허재");
		LectureInstanceDto lectureInstanceDto = new LectureInstanceDto(1L, LocalDate.of(2024, 11, 1),
			LocalDate.of(2024, 12, 2), 30, 0, lecture1, LectureStatus.CLOSE);

		MemberDto member = new MemberDto(1L, "Member01");

		// when: 강의 상태가 CLOSED일 때 회원이 신청 시도
		Exception exception = null;
		try {
			lectureApplyUseCaseService.applyForLecture(member, lectureInstanceDto);
		} catch (Exception e) {
			exception = e;
		}

		// then: 예외 발생 확인
		assertThat(exception).isNotNull();
		assertThat(exception.getMessage()).contains("종료된 강의에는 신청할 수 없습니다.");
	}


}
