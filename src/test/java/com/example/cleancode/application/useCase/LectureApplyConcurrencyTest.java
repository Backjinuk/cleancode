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


	@Test
	@Transactional
	public void testDuplicateLectureApply() throws InterruptedException {
		// Given: 강의 인스턴스 생성 및 사용자 설정
		LectureDto lecture1 = new LectureDto(1L, "Clean Architecture1", "허재");
		LectureInstanceDto lectureInstanceDto = new LectureInstanceDto(1L, LocalDate.of(2024, 11, 1),
			LocalDate.of(2024, 12, 2), 30, 0, lecture1, LectureStatus.OPEN);

		// Given: 테스트용 사용자 생성
		MemberDto member = new MemberDto(1L, "Member1");

		// 동시성 제어를 위해 CountDownLatch 사용
		ExecutorService executorService = Executors.newFixedThreadPool(5); // 5번 시도
		CountDownLatch latch = new CountDownLatch(5);  // 5번의 스레드 동시 실행 대기


		// 성공과 실패 카운트를 위한 배열
		int[] successCount = {0};  // 성공 카운트 (람다 내에서는 final이 필요하므로 배열 사용)
		int[] failureCount = {0};  // 실패 카운트

		// 동일한 사용자가 5번 신청 시도
		for (int i = 0; i < 5; i++) {
			executorService.submit(() -> {
				try {
					// 각 스레드에서 동일한 사용자가 신청 시도
					lectureApplyUseCaseService.applyForLecture(member, lectureInstanceDto);
					successCount[0]++;  // 성공 시 카운트 증가
				} catch (Exception e) {
					// 실패한 경우 카운트 증가
					failureCount[0]++;
				} finally {
					latch.countDown();  // 스레드 실행 완료 시 latch 감소
				}
			});
		}

		// 모든 스레드가 완료될 때까지 대기
		latch.await();

		// Then: 하나의 신청만 성공했는지 검증
		assertThat(successCount[0]).isEqualTo(1);
		assertThat(failureCount[0]).isEqualTo(4);  // 나머지 4번은 중복 신청으로 실패해야 함
	}


	@DisplayName("동시 신청 시 중복 신청자 처리 및 최대 인원 제한 확인")
	@Transactional
	@Test
	void 동시_신청_시_중복_신청자_처리_및_최대_인원_제한_확인() throws InterruptedException {
		// given: 강의 인스턴스 생성 및 최대 참가자 수 설정 (30명)
		LectureDto lecture1 = new LectureDto(1L, "Clean Architecture1", "허재");
		LectureInstanceDto lectureInstanceDto = new LectureInstanceDto(1L, LocalDate.of(2024, 11, 1),
			LocalDate.of(2024, 12, 2), 30, 0, lecture1, LectureStatus.OPEN);

		// given: List<MemberDto>로 35명의 회원 미리 설정
		List<MemberDto> members = setupDummyMembers();  // 35명의 회원 생성 (중복 포함)

		// 동일한 회원이 중복 신청할 수 있도록 추가
		members.add(new MemberDto(1L, "Member1"));  // 중복 신청할 회원 추가
		members.add(new MemberDto(1L, "Member1"));  // 동일한 회원 다시 추가

		// 동시성 제어를 위한 ExecutorService와 CountDownLatch 설정
		ExecutorService executorService = Executors.newFixedThreadPool(40);
		CountDownLatch latch = new CountDownLatch(37);  // 37명의 스레드 동시 실행 대기 (35명 + 중복 2명)

		// 성공과 실패 카운트를 위한 배열
		int[] successCount = {0};  // 성공 카운트 (람다 내에서는 final이 필요하므로 배열 사용)
		int[] failureCount = {0};  // 실패 카운트

		// 37명이 동시에 신청 (중복된 사용자 포함)
		for (MemberDto member : members) {
			executorService.submit(() -> {
				try {
					// 각 회원이 동시에 강의에 신청을 시도
					lectureApplyUseCaseService.applyForLecture(member, lectureInstanceDto);
					successCount[0]++;  // 성공 시 카운트 증가
				} catch (Exception e) {
					// 실패한 경우 카운트 증가
					failureCount[0]++;
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

		// then: 동일한 회원이 중복 신청을 하지 못하고 실패했는지 확인
		assertThat(successCount[0]).isEqualTo(30);  // 최대 30명만 성공해야 함
		assertThat(failureCount[0]).isGreaterThan(7);  // 중복 신청으로 인해 실패한 케이스 포함
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

}
