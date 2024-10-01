package com.example.cleancode.application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cleancode.application.useCase.repository.LectureRepository;
import com.example.cleancode.domain.Lecture;

@Service
public class LectureService {

	private final LectureRepository lectureRepository;

	@Autowired
	public LectureService(LectureRepository lectureRepository) {
		this.lectureRepository = lectureRepository;
	}

	public List<Lecture> getAllLectureList() {
		return lectureRepository.getAllLectureList();
	}

	public Lecture getLectureById(long id) {
			Lecture lectureById = lectureRepository.getLectureById(id);

			if(lectureById == null) {
				throw new NullPointerException("강의를 조회할 수 없습니다.");
			}

			return lectureById;

	}
}
