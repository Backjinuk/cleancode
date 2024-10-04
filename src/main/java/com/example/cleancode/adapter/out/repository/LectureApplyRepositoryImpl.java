package com.example.cleancode.adapter.out.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.cleancode.adapter.in.dto.LectureApplyDto;
import com.example.cleancode.application.useCase.repository.LectureApplyRepository;
import com.example.cleancode.domain.LectureApply;
import com.example.cleancode.domain.LectureInstance;
import com.example.cleancode.domain.LectureStatus;
import com.example.cleancode.domain.Member;
import com.example.cleancode.domain.QLectureApply;
import com.example.cleancode.domain.QLectureInstance;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;

@Repository
public class LectureApplyRepositoryImpl implements LectureApplyRepository {

	@PersistenceContext
	private EntityManager entityManager;

	private final JPAQueryFactory queryFactory;

	public LectureApplyRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	@Transactional  // 트랜잭션 처리: LectureApply 저장
	public LectureApply addLectureApplyInMember(LectureApply lectureApply) {
		lectureApply.setId(null);

		entityManager.persist(lectureApply);  // LectureApply 엔티티 저장
		return lectureApply;
	}

	@Override
	@Transactional(readOnly = true)  // 트랜잭션 처리: 조회 작업은 readOnly
	public List<LectureApply> getLectureApplyInfoByMember(Long memberId) {
		QLectureApply qLectureApply = QLectureApply.lectureApply;

		return queryFactory
			.selectFrom(qLectureApply)
			.where(qLectureApply.member.id.eq(memberId))
			.fetch();  // 특정 Member에 해당하는 LectureApply 리스트 조회
	}

	@Override
	@Transactional(readOnly = true)  // 트랜잭션 처리: 중복 검사 조회 작업도 readOnly
	public boolean validateLectureApply(Member member, LectureInstance lectureInstance) {
		QLectureApply qLectureApply = QLectureApply.lectureApply;

		long count = queryFactory
			.selectFrom(qLectureApply)
			.where(qLectureApply.member.eq(member)
				.and(qLectureApply.lectureInstance.eq(lectureInstance)))
			.fetchCount();  // 회원과 강의 인스턴스를 통해 중복 여부 확인

		return count == 0;  // 중복된 신청이 없으면 true 반환
	}

	@Override
	@Transactional(readOnly = true)  // 트랜잭션 처리: 조회 작업은 readOnly
	public List<LectureApply> getLectureApplyInfoByMemberAndLectureInstance(Long memberId, Long lectureInstanceId) {
		QLectureApply qLectureApply = QLectureApply.lectureApply;

		return queryFactory
			.selectFrom(qLectureApply)
			.where(qLectureApply.member.id.eq(memberId)
				.and(qLectureApply.lectureInstance.id.eq(lectureInstanceId)))
			.setLockMode(LockModeType.PESSIMISTIC_WRITE)  // 비관적 락 적용
			.setHint("javax.persistence.lock.timeout", 5000)  // 타임아웃 5초 설정
			.fetch();  // 특정 회원과 강의 인스턴스에 해당하는 신청 내역 조회
	}

	@Override
	@Transactional  // 트랜잭션 처리: 삭제 작업
	public void cancelLectureApply(long lectureApplyId) {
		QLectureApply qLectureApply = QLectureApply.lectureApply;

		LectureApply lectureApply = queryFactory
			.selectFrom(qLectureApply)
			.where(qLectureApply.id.eq(lectureApplyId))
			.fetchOne();  // 특정 ID의 LectureApply 조회

		if (lectureApply != null) {
			entityManager.remove(lectureApply);  // 해당 신청 내역 삭제
		}
	}

	@Override
	@Transactional  // 트랜잭션 처리: 수정 작업
	public void updateLectureApply(LectureApply lectureApply) {
		entityManager.merge(lectureApply);  // LectureApply 업데이트
	}

	@Override
	@Transactional(readOnly = true)
	public List<LectureApply> getLectureApplyInfoByFilters(long memberId, LectureStatus status, LocalDate fromDate, LocalDate toDate) {
		QLectureApply qLectureApply = QLectureApply.lectureApply;
		QLectureInstance qLectureInstance = QLectureInstance.lectureInstance;  // LectureInstance 추가

		return queryFactory
			.selectFrom(qLectureApply)
			.join(qLectureApply.lectureInstance, qLectureInstance)  // LectureInstance와 조인
			.where(qLectureApply.member.id.eq(memberId)
				.and(status != null ? qLectureInstance.status.eq(status) : null)  // LectureInstance의 status 필터링
				.and(fromDate != null ? qLectureInstance.startDate.goe(fromDate) : null)  // LectureInstance의 startDate 필터링
				.and(toDate != null ? qLectureInstance.endDate.loe(toDate) : null))  // LectureInstance의 endDate 필터링
			.fetch();  // 필터 조건에 따라 신청 내역 조회
	}

}
