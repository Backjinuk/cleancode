package com.example.cleancode.adapter.out.repository;

import com.example.cleancode.application.useCase.repository.LectureRepository;
import com.example.cleancode.domain.Lecture;
import com.example.cleancode.domain.QLecture;
import com.example.cleancode.domain.QLectureInstance;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;


import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class LectureRepositoryImpl implements LectureRepository {

	// QueryDSL을 사용하기 위한 QLecture 전역 변수 정의
	private final QLecture qLecture = QLecture.lecture;

	private final JPAQueryFactory queryFactory;

	@PersistenceContext
	private EntityManager entityManager;

	// Constructor Injection으로 JPAQueryFactory 주입
	public LectureRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	// 모든 강의 리스트 가져오기
	@Override
	public List<Lecture> getAllLectureList() {
		return queryFactory
			.selectFrom(qLecture)
			.fetch();
	}

	// ID로 강의 가져오기
	@Override
	public Lecture getLectureById(long id) {
		return queryFactory
			.selectFrom(qLecture)
			.where(qLecture.id.eq(id))
			.fetchOne();
	}

	// 강의 추가
	@Override
	@Transactional
	public Lecture addLecture(Lecture lecture) {
		// ID를 null로 설정하여 새로운 엔티티로 인식되도록 함
		lecture.setId(null);

		// EntityManager를 사용하여 강의 추가
		entityManager.persist(lecture);
		return lecture;
	}

	// 강의 수정
	@Override
	@Transactional
	public Lecture updateLecture(Lecture existingLecture) {
		// 강의가 DB에 존재하는 경우 수정
		Lecture lecture = entityManager.merge(existingLecture);
		return lecture;
	}

	// 강의 삭제
	@Override
	@Transactional
	public boolean deleteLecture(Long lectureId) {
		// 1. LECTURE_INSTANCE 테이블에서 해당 lectureId를 참조하는 레코드 삭제
		long deletedInstances = queryFactory
			.delete(QLectureInstance.lectureInstance)
			.where(QLectureInstance.lectureInstance.lecture.id.eq(lectureId))
			.execute();

		// 2. LECTURE 테이블에서 해당 강의를 삭제
		Lecture lecture = queryFactory
			.selectFrom(qLecture)
			.where(qLecture.id.eq(lectureId))
			.fetchOne();

		if (lecture != null) {
			entityManager.remove(lecture);  // 부모 테이블에서 레코드 삭제
			return true;
		}

		return false;
	}
}
