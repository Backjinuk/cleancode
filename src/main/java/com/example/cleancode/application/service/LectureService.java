package com.example.cleancode.application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cleancode.adapter.in.dto.LectureDto;
import com.example.cleancode.application.mapper.LectureMapper;
import com.example.cleancode.application.useCase.repository.LectureRepository;
import com.example.cleancode.application.validation.EntityValidation;
import com.example.cleancode.domain.Lecture;

@Service
public class LectureService {

	private final LectureRepository lectureRepository;

	@Autowired
	public LectureService(LectureRepository lectureRepository) {
		this.lectureRepository = lectureRepository;
	}

	public List<LectureDto> getAllLectureList() {
		return lectureRepository.getAllLectureList().stream().map(LectureMapper::lectureEntityToDto).toList();
	}

	public LectureDto getLectureById(long id) {
		Lecture lectureById = lectureRepository.getLectureById(id);

		if (lectureById == null) {
			throw new NullPointerException("강의를 조회할 수 없습니다.");
		}

		return LectureMapper.lectureEntityToDto(lectureById);

	}

	@Transactional
	public LectureDto addLecture(LectureDto lectureDto) {
		Lecture lecture = LectureMapper.lectureDtoToEntity(lectureDto);
		EntityValidation.validationLecture(lecture);

		return LectureMapper.lectureEntityToDto(lectureRepository.addLecture(lecture));
	}

	@Transactional
	public LectureDto updateLecture(LectureDto lectureDto) {
		Lecture lecture = LectureMapper.lectureDtoToEntity(lectureDto);
		EntityValidation.validationLecture(lecture);

		return LectureMapper.lectureEntityToDto(lectureRepository.updateLecture(lecture));
	}

	@Transactional
	public boolean deleteLecture(Long lectureId) {
		boolean deleteLecture = lectureRepository.deleteLecture(lectureId);

		if(!deleteLecture){
			throw new IllegalArgumentException("삭제할 강의가 존재하지 않습니다.");
		}

		return deleteLecture;
	}
}
