package com.example.cleancode.adapter.in.controller;

import org.springframework.web.bind.annotation.*;

import com.example.cleancode.adapter.in.dto.LectureInstanceDto;
import com.example.cleancode.application.useCase.LectureInstanceUseCaseService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/lectureInstances")
public class LectureInstanceController {

	private final LectureInstanceUseCaseService lectureInstanceUseCaseService;

	// Constructor Injection
	public LectureInstanceController(LectureInstanceUseCaseService lectureInstanceUseCaseService) {
		this.lectureInstanceUseCaseService = lectureInstanceUseCaseService;
	}

	// 모든 강의 목록 조회 - GET 요청
	@RequestMapping(value = "/getAllLectureInstances", method = RequestMethod.GET)
	public List<LectureInstanceDto> getAllLectureInstances() {
		return lectureInstanceUseCaseService.getAllLectureInstances();
	}

	// ID 기반 강의 찾기 - GET 요청
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public LectureInstanceDto getLectureInstanceById(@PathVariable Long id) {
		return lectureInstanceUseCaseService.getLectureInstanceById(id);
	}

	// ID 및 날짜 기반 강의 찾기 - GET 요청
	@RequestMapping(value = "/{id}/date", method = RequestMethod.GET)
	public List<LectureInstanceDto> getLectureInstancesByIdAndDate(@PathVariable Long id, @RequestParam("date") String date) {
		LocalDate localDate = LocalDate.parse(date); // String을 LocalDate로 변환
		return lectureInstanceUseCaseService.getLectureInstancesByIdAndDate(id, localDate);
	}

	// 강의 등록 - POST 요청
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public LectureInstanceDto addLectureInstance(@RequestBody LectureInstanceDto lectureInstanceDto) {
		return lectureInstanceUseCaseService.addLectureInstance(lectureInstanceDto);
	}

	// 강의 수정 - PUT 요청
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public LectureInstanceDto updateLectureInstance(@RequestBody LectureInstanceDto lectureInstanceDto) {
		return lectureInstanceUseCaseService.updateLectureInstance(lectureInstanceDto);
	}

	// 강의 삭제 - DELETE 요청 (강의 ID로 삭제)
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public boolean deleteLectureInstance(@PathVariable Long id) {
		return lectureInstanceUseCaseService.deleteLectureInstance(id);
	}

}
