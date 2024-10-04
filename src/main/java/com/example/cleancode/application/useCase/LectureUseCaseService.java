package com.example.cleancode.application.useCase;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cleancode.adapter.in.dto.LectureDto;
import com.example.cleancode.application.service.LectureService;
import com.example.cleancode.application.validation.DtoValidation;

@Service
@Transactional  // 클래스 레벨에서 모든 메서드에 트랜잭션 적용
public class LectureUseCaseService {

	private final LectureService lectureService;

	public LectureUseCaseService(LectureService lectureService){
		this.lectureService = lectureService;
	}

	public LectureDto addLecture(LectureDto lectureDto){
		DtoValidation.validationLectureDto(lectureDto);

		return lectureService.addLecture(lectureDto);
	}

	public List<LectureDto> getAllLectureList() {
		return lectureService.getAllLectureList();
	}

	public LectureDto getLectureById(long id) {
		return lectureService.getLectureById(id);
	}

	public boolean deleteLecture(long id) {
		return lectureService.deleteLecture(id);
	}

	public void updateLecture(LectureDto lectureDto) {
		lectureService.updateLecture(lectureDto);
	}
}
