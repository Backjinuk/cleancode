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

	private static final QLecture qLecture = QLecture.lecture;
	private static final QLectureInstance qLectureInstance = QLectureInstance.lectureInstance;

	private final JPAQueryFactory queryFactory;

	@PersistenceContext
	private EntityManager entityManager;

	// Constructor Injection으로 JPAQueryFactory 주입
	public LectureRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	// 모든 강의 리스트 가져오기
	@Override
	@Transactional(readOnly = true)
	public List<Lecture> getAllLectureList() {
		return queryFactory
			.selectFrom(qLecture)
			.fetch();  // 모든 Lecture 목록 반환
	}

	// ID로 강의 가져오기
	@Override
	@Transactional(readOnly = true)
	public Lecture getLectureById(long id) {
		return queryFactory
			.selectFrom(qLecture)
			.where(qLecture.id.eq(id))
			.fetchOne();  // ID에 해당하는 Lecture 반환
	}

	// 강의 추가
	@Override
	@Transactional
	public Lecture addLecture(Lecture lecture) {
		lecture.setId(null);  // 새로운 Lecture로 인식되도록 ID를 null로 설정
		entityManager.persist(lecture);  // 엔티티 저장
		return lecture;
	}

	// 강의 수정
	@Override
	@Transactional
	public Lecture updateLecture(Lecture existingLecture) {
		return entityManager.merge(existingLecture);  // 엔티티 병합
	}

	// 강의 삭제
	@Override
	@Transactional
	public boolean deleteLecture(Long lectureId) {
		// 1. LECTURE_INSTANCE 테이블에서 해당 lectureId를 참조하는 레코드 삭제
		long deletedInstances = queryFactory
			.delete(qLectureInstance)
			.where(qLectureInstance.lecture.id.eq(lectureId))
			.execute();

		// 2. LECTURE 테이블에서 해당 강의를 삭제
		Lecture lecture = queryFactory
			.selectFrom(qLecture)
			.where(qLecture.id.eq(lectureId))
			.fetchOne();

		if (lecture != null) {
			entityManager.remove(lecture);  // 강의 삭제
			return true;
		}

		return false;
	}
}
