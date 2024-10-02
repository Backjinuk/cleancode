package com.example.cleancode.application.useCase.repository;

import java.util.List;

import com.example.cleancode.domain.LectureInstance;

public interface LectureInstanceRepository {

	boolean addLectureInLectureInstance(LectureInstance lectureInstance);

	List<LectureInstance> getAllLectureInstance();

	LectureInstance increnentCurrentParticpants(LectureInstance lectureInstance1);

	LectureInstance getLectureInstance(Long id);
}
