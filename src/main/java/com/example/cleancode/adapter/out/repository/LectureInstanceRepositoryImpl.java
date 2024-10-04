package com.example.cleancode.adapter.out.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.cleancode.application.useCase.repository.LectureInstanceRepository;
import com.example.cleancode.domain.LectureInstance;
import com.example.cleancode.domain.QLectureInstance;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class LectureInstanceRepositoryImpl implements LectureInstanceRepository {

	@PersistenceContext
	private EntityManager entityManager;

	private final JPAQueryFactory queryFactory;

	private static final QLectureInstance qLectureInstance = QLectureInstance.lectureInstance;

	public LectureInstanceRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	@Transactional
	public boolean addLectureInstance(LectureInstance lectureInstance) {
		lectureInstance.setId(null);  // 새 인스턴스인 경우 ID는 null로 설정
		entityManager.persist(lectureInstance);
		return true;
	}

	@Override
	@Transactional(readOnly = true)
	public List<LectureInstance> getAllLectureInstance() {
		return queryFactory
			.selectFrom(qLectureInstance)
			.fetch();  // 전체 LectureInstance 리스트 조회
	}

	@Override
	@Transactional
	public LectureInstance increnentCurrentParticpants(LectureInstance lectureInstance) {
		lectureInstance.setCurrentParticipants(lectureInstance.getCurrentParticipants() + 1);  // 현재 참가자 수 증가
		return entityManager.merge(lectureInstance);  // 변경 사항 병합
	}

	@Override
	@Transactional(readOnly = true)
	public LectureInstance getLectureInstance(Long id) {
		return queryFactory
			.selectFrom(qLectureInstance)
			.where(qLectureInstance.id.eq(id))
			.fetchOne();  // ID에 해당하는 LectureInstance 조회
	}

	@Override
	@Transactional
	public LectureInstance decrementCurrentParticipants(LectureInstance lectureInstance) {
		lectureInstance.setCurrentParticipants(lectureInstance.getCurrentParticipants() - 1);  // 현재 참가자 수 감소
		return entityManager.merge(lectureInstance);
	}

	@Override
	@Transactional
	public LectureInstance updateLectureInLectureInstance(LectureInstance lectureInstance) {
		LectureInstance lectureInstanceCheck = queryFactory
			.selectFrom(qLectureInstance)
			.where(qLectureInstance.id.eq(lectureInstance.getId()))
			.fetchOne();  // 해당 ID의 LectureInstance 존재 여부 확인

		if (lectureInstanceCheck != null) {
			entityManager.merge(lectureInstance);  // 존재할 경우 업데이트
		}

		return lectureInstance;
	}

	@Override
	@Transactional
	public boolean deleteLectureInstance(Long lectureInstanceId) {
		LectureInstance lectureInstance = queryFactory
			.selectFrom(qLectureInstance)
			.where(qLectureInstance.id.eq(lectureInstanceId))
			.fetchOne();  // 삭제할 LectureInstance 조회

		if (lectureInstance != null) {
			entityManager.remove(lectureInstance);  // 존재할 경우 삭제
			return true;
		}
		return false;
	}

	@Override
	@Transactional(readOnly = true)
	public List<LectureInstance> getLectureInstancesByIdAndDate(Long id, LocalDate date) {
		return queryFactory
			.selectFrom(qLectureInstance)
			.where(qLectureInstance.lecture.id.eq(id)
				.and(qLectureInstance.startDate.eq(date)))
			.fetch();  // 특정 강의와 날짜에 해당하는 LectureInstance 리스트 조회
	}
}
