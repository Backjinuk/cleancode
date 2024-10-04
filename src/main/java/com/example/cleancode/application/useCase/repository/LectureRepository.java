package com.example.cleancode.application.useCase.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.cleancode.domain.Lecture;

@Repository
public interface LectureRepository {

	List<Lecture> getAllLectureList();

	Lecture getLectureById(long l);

	Lecture addLecture(Lecture lecture);

	Lecture updateLecture(Lecture existingLecture);

	boolean deleteLecture(Long lectureId);
}
