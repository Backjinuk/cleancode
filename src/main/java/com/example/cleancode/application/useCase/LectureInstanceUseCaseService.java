package com.example.cleancode.application.useCase;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.cleancode.adapter.in.dto.LectureInstanceDto;
import com.example.cleancode.application.service.LectureInstanceService;

@Service
public class LectureInstanceUseCaseService {

	private LectureInstanceService lectureInstanceService;

	public LectureInstanceUseCaseService(LectureInstanceService lectureInstanceService){
		this.lectureInstanceService = lectureInstanceService;
	}

	// 모든 강의 목록 조회
	public List<LectureInstanceDto> getAllLectureInstances() {
		return lectureInstanceService.getAllLectureInstance();
	}

	// 강의 등록
	public LectureInstanceDto addLectureInstance(LectureInstanceDto lectureInstanceDto) {
		// 강의 중복 검사
		// if (lectureInstanceService.getLectureInstance(lectureInstanceDto.getId()) != null) {
		// 	throw new IllegalArgumentException("이미 존재하는 강의입니다.");
		// }

		boolean savedCheck = lectureInstanceService.addLectureInLectureInstance(lectureInstanceDto);

		if(!savedCheck){
			throw new IllegalArgumentException("강의 등록에 실패하였습니다.");
		}

		return lectureInstanceDto;
	}

	// 강의 수정
	public LectureInstanceDto updateLectureInstance(LectureInstanceDto lectureDto) {
		lectureInstanceService.updateLectureInLectureInstance(lectureDto);
		return lectureDto;
	}

	// 강의 삭제
	public boolean deleteLectureInstance(Long id) {
		return lectureInstanceService.deleteLectureInLectureInstance(id);
	}

	// ID 기반 강의 찾기
	public LectureInstanceDto getLectureInstanceById(Long id) {
		LectureInstanceDto lectureInstance = lectureInstanceService.getLectureInstance(id);
		if (lectureInstance == null) {
			throw new IllegalArgumentException("해당 ID로 강의를 찾을 수 없습니다.");
		}
		return lectureInstance;
	}

	// ID 및 날짜 기반 강의 찾기
	public List<LectureInstanceDto> getLectureInstancesByIdAndDate(Long id, LocalDate date) {
		List<LectureInstanceDto> lectureInstances = lectureInstanceService.getLectureInstancesByIdAndDate(id, date);
		if (lectureInstances.isEmpty()) {
			throw new IllegalArgumentException("해당 ID 및 날짜로 강의를 찾을 수 없습니다.");
		}
		return lectureInstances;
	}


}
