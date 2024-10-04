package com.example.cleancode.application.useCase.repository;

import java.time.LocalDate;
import java.util.List;

import com.example.cleancode.adapter.in.dto.LectureInstanceDto;
import com.example.cleancode.domain.LectureInstance;

public interface LectureInstanceRepository {

	boolean addLectureInstance(LectureInstance lectureInstance);

	List<LectureInstance> getAllLectureInstance();

	LectureInstance increnentCurrentParticpants(LectureInstance lectureInstance);

	LectureInstance getLectureInstance(Long id);

	LectureInstance decrementCurrentParticipants(LectureInstance lectureInstance);

	LectureInstance updateLectureInLectureInstance(LectureInstance lectureInstance);

	boolean deleteLectureInstance(Long lectureInstanceId);

	List<LectureInstance> getLectureInstancesByIdAndDate(Long id, LocalDate date);
}
