package com.example.cleancode.adapter.out.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.cleancode.application.useCase.repository.LectureRepository;
import com.example.cleancode.domain.Lecture;

@Repository
public class LectureRepositoryImpl implements LectureRepository {

	@Override
	public List<Lecture> getAllLectureList() {
		return List.of();
	}

	@Override
	public Lecture getLectureById(long l) {
		return null;
	}
}
