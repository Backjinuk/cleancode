package com.example.cleancode.adapter.out.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.cleancode.adapter.in.dto.LectureInstanceDto;
import com.example.cleancode.application.useCase.repository.LectureInstanceRepository;
import com.example.cleancode.domain.LectureInstance;
import com.example.cleancode.domain.QLecture;
import com.example.cleancode.domain.QLectureInstance;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;

@Repository
public class LectureInstanceRepositoryImpl implements LectureInstanceRepository {

	@PersistenceContext
	private EntityManager entityManager;

	private final JPAQueryFactory queryFactory;

	public LectureInstanceRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	@Transactional
	public boolean addLectureInstance(LectureInstance lectureInstance) {
		lectureInstance.setId(null);
		entityManager.persist(lectureInstance);
		return true;
	}

	@Override
	public List<LectureInstance> getAllLectureInstance() {
		QLectureInstance qLectureInstance = QLectureInstance.lectureInstance;

		return queryFactory
			.selectFrom(qLectureInstance)
			.fetch();
	}



	@Override
	@Transactional
	public LectureInstance increnentCurrentParticpants(LectureInstance lectureInstance) {
		entityManager.merge(lectureInstance);
		return lectureInstance;
	}

	@Transactional
	@Override
	public LectureInstance getLectureInstance(Long id) {
		QLectureInstance qLectureInstance = QLectureInstance.lectureInstance;
		QLecture qLecture = QLecture.lecture;  // QLecture 객체 추가

		// LectureInstance와 Lecture를 JOIN하여 조회
		LectureInstance lectureInstance = queryFactory
			.selectFrom(qLectureInstance)
			.join(qLectureInstance.lecture, qLecture).fetchJoin()  // Lecture와 JOIN 수행
			.where(qLectureInstance.id.eq(id))
			.setLockMode(LockModeType.PESSIMISTIC_WRITE)  // 비관적 락 설정
			.setHint("javax.persistence.lock.timeout", 5000)  // 5초 타임아웃 설정
			.fetchOne();

		if (lectureInstance == null) {
			throw new RuntimeException("LectureInstance를 찾을 수 없습니다.");
		}

		return lectureInstance;
	}

	@Override
	@Transactional
	public LectureInstance decrementCurrentParticipants(LectureInstance lectureInstance) {
		lectureInstance.setCurrentParticipants(lectureInstance.getCurrentParticipants() - 1);
		entityManager.merge(lectureInstance);
		return lectureInstance;
	}

	@Override
	@Transactional
	public LectureInstance updateLectureInLectureInstance(LectureInstance lectureInstance) {
		QLectureInstance qLectureInstance = QLectureInstance.lectureInstance;

		LectureInstance lectureInstanceCheck = queryFactory
			.selectFrom(qLectureInstance)
			.where(qLectureInstance.id.eq(lectureInstance.getId()))
			.fetchOne();

		if (lectureInstanceCheck != null) {
			entityManager.merge(lectureInstance);
		}

		return lectureInstance;
	}

	@Override
	@Transactional
	public boolean deleteLectureInstance(Long lectureInstanceId) {
		QLectureInstance qLectureInstance = QLectureInstance.lectureInstance;

		LectureInstance lectureInstance = queryFactory
			.selectFrom(qLectureInstance)
			.where(qLectureInstance.id.eq(lectureInstanceId))
			.fetchOne();

		if (lectureInstance != null) {
			entityManager.remove(lectureInstance);
			return true;
		}
		return false;
	}

	@Override
	public List<LectureInstance> getLectureInstancesByIdAndDate(Long id, LocalDate date) {
		QLectureInstance qLectureInstance = QLectureInstance.lectureInstance;

		return queryFactory
			.selectFrom(qLectureInstance)
			.where(qLectureInstance.lecture.id.eq(id)
				.and(qLectureInstance.startDate.eq(date)))
			.fetch();
	}

}
