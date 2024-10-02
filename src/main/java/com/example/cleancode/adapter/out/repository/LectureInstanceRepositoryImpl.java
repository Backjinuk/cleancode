package com.example.cleancode.adapter.out.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.cleancode.application.useCase.repository.LectureInstanceRepository;
import com.example.cleancode.domain.LectureInstance;

@Repository
public class LectureInstanceRepositoryImpl implements LectureInstanceRepository {

	@Override
	public boolean addLectureInLectureInstance(LectureInstance lectureInstance) {
		return false;
	}

	@Override
	public List<LectureInstance> getAllLectureInstance() {
		return List.of();
	}

	@Override
	public LectureInstance increnentCurrentParticpants(LectureInstance lectureInstance1) {
		return null;
	}

	@Override
	public LectureInstance getLectureInstance(Long id) {
		return null;
	}
}
