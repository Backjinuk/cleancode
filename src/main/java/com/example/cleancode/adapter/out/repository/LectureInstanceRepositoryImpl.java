package com.example.cleancode.adapter.out.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.cleancode.adapter.in.dto.LectureInstanceDto;
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
		lectureInstance.setCurrentParticipants(lectureInstance.getCurrentParticipants() + 1);
		entityManager.merge(lectureInstance);
		return lectureInstance;
	}

	@Override
	public LectureInstance getLectureInstance(Long id) {
		QLectureInstance qLectureInstance = QLectureInstance.lectureInstance;

		return queryFactory
			.selectFrom(qLectureInstance)
			.where(qLectureInstance.id.eq(id))
			.fetchOne();
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
