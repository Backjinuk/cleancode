package com.example.cleancode.adapter.in.controller;

import org.springframework.web.bind.annotation.*;

import com.example.cleancode.adapter.in.dto.LectureDto;
import com.example.cleancode.application.useCase.LectureUseCaseService;

import java.util.List;

@RestController
@RequestMapping("/lectures")
class LectureController {

	private final LectureUseCaseService lectureUseCaseService;

	// Constructor Injection
	public LectureController(LectureUseCaseService lectureUseCaseService) {
		this.lectureUseCaseService = lectureUseCaseService;
	}

	// 강의 추가 - POST 요청
	@RequestMapping(value = "/addLecture", method = RequestMethod.POST)
	public LectureDto addLecture(@RequestBody LectureDto lectureDto) {
		return lectureUseCaseService.addLecture(lectureDto);
	}

	// 모든 강의 리스트 조회 - GET 요청
	@RequestMapping(value = "/getAllLectures", method = RequestMethod.GET)
	public List<LectureDto> getAllLectures() {
		return lectureUseCaseService.getAllLectureList();
	}

	// 특정 강의 조회 - GET 요청 (강의 ID로 조회)
	@RequestMapping(value = "/getLectureById/{id}", method = RequestMethod.GET)
	public LectureDto getLectureById(@PathVariable long id) {
		return lectureUseCaseService.getLectureById(id);
	}

	// 강의 삭제 - DELETE 요청 (강의 ID로 삭제)
	@RequestMapping(value = "/deleteLecture/{id}", method = RequestMethod.DELETE)
	public boolean deleteLecture(@PathVariable long id) {
		return lectureUseCaseService.deleteLecture(id);
	}

	// 강의 수정 - PUT 요청
	@RequestMapping(value = "/updateLecture", method = RequestMethod.PUT)
	public void updateLecture(@RequestBody LectureDto lectureDto) {
		lectureUseCaseService.updateLecture(lectureDto);
	}
}
