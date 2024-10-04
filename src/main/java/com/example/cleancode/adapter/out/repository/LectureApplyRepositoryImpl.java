package com.example.cleancode.adapter.out.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.cleancode.application.useCase.repository.LectureApplyRepository;
import com.example.cleancode.domain.LectureApply;
import com.example.cleancode.domain.LectureInstance;
import com.example.cleancode.domain.LectureStatus;
import com.example.cleancode.domain.Member;
import com.example.cleancode.domain.QLectureApply;
import com.example.cleancode.domain.QLectureInstance;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class LectureApplyRepositoryImpl implements LectureApplyRepository {

	@PersistenceContext
	private EntityManager entityManager;

	private final JPAQueryFactory queryFactory;

	private static final QLectureApply qLectureApply = QLectureApply.lectureApply;
	private static final QLectureInstance qLectureInstance = QLectureInstance.lectureInstance;

	public LectureApplyRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	@Transactional
	public LectureApply addLectureApplyInMember(LectureApply lectureApply) {
		lectureApply.setId(null);
		entityManager.persist(lectureApply);
		return lectureApply;
	}

	@Override
	@Transactional(readOnly = true)
	public List<LectureApply> getLectureApplyInfoByMember(Long memberId) {
		return queryFactory
			.selectFrom(qLectureApply)
			.where(qLectureApply.member.id.eq(memberId))
			.fetch();
	}

	@Override
	@Transactional(readOnly = true)
	public boolean validateLectureApply(Member member, LectureInstance lectureInstance) {
		// fetchFirst()는 null을 반환할 수 있으므로 null 체크
		LectureApply existingApply = queryFactory
			.selectFrom(qLectureApply)
			.where(qLectureApply.member.eq(member)
				.and(qLectureApply.lectureInstance.eq(lectureInstance)))
			.fetchFirst();  // 첫 번째 결과를 가져오거나 없으면 null 반환

		return existingApply == null;  // 중복된 신청이 없으면 true 반환
	}


	@Override
	@Transactional(readOnly = true)
	public List<LectureApply> getLectureApplyInfoByMemberAndLectureInstance(Long memberId, Long lectureInstanceId) {
		return queryFactory
			.selectFrom(qLectureApply)
			.where(qLectureApply.member.id.eq(memberId)
				.and(qLectureApply.lectureInstance.id.eq(lectureInstanceId)))
			.fetch();
	}

	@Override
	@Transactional
	public void cancelLectureApply(long lectureApplyId) {
		LectureApply lectureApply = queryFactory
			.selectFrom(qLectureApply)
			.where(qLectureApply.id.eq(lectureApplyId))
			.fetchOne();

		if (lectureApply != null) {
			entityManager.remove(lectureApply);
		}
	}

	@Override
	@Transactional
	public void updateLectureApply(LectureApply lectureApply) {
		entityManager.merge(lectureApply);
	}

	@Override
	@Transactional(readOnly = true)
	public List<LectureApply> getLectureApplyInfoByFilters(long memberId, LectureStatus status, LocalDate fromDate, LocalDate toDate) {
		return queryFactory
			.selectFrom(qLectureApply)
			.join(qLectureApply.lectureInstance, qLectureInstance)
			.where(qLectureApply.member.id.eq(memberId)
				.and(status != null ? qLectureInstance.status.eq(status) : null)
				.and(fromDate != null ? qLectureInstance.startDate.goe(fromDate) : null)
				.and(toDate != null ? qLectureInstance.endDate.loe(toDate) : null))
			.fetch();
	}
}
