package com.example.cleancode.adapter.in.controller;

import org.springframework.web.bind.annotation.*;

import com.example.cleancode.adapter.in.dto.LectureApplyDto;
import com.example.cleancode.adapter.in.dto.LectureInstanceDto;
import com.example.cleancode.adapter.in.dto.MemberDto;
import com.example.cleancode.application.useCase.LectureApplyUseCaseService;
import com.example.cleancode.domain.LectureStatus;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/LectureApply")
public class LectureApplyController {

	private final LectureApplyUseCaseService lectureApplyUseCaseService;

	// Constructor Injection
	public LectureApplyController(LectureApplyUseCaseService lectureApplyUseCaseService) {
		this.lectureApplyUseCaseService = lectureApplyUseCaseService;
	}

	// 강의 신청 - POST 요청
	@RequestMapping(value = "/applyForLecture", method = RequestMethod.POST)
	public void applyForLecture(@RequestBody LectureApplyRequest request) {
		lectureApplyUseCaseService.applyForLecture(request.getMemberDto(), request.getLectureInstanceDto());
	}

	// 강의 신청 상태 확인 - GET 요청
	@RequestMapping(value = "/getLectureApplicationStatus", method = RequestMethod.GET)
	public LectureApplyDto getLectureApplicationStatus(@RequestParam Long memberId,
		@RequestParam Long lectureInstanceId) {
		MemberDto memberDto = new MemberDto(memberId);
		LectureInstanceDto lectureInstanceDto = new LectureInstanceDto(lectureInstanceId);
		return lectureApplyUseCaseService.getLectureApplicationStatus(memberDto, lectureInstanceDto);
	}

	// 강의 신청 수정 - PUT 요청
	@RequestMapping(value = "/updateLectureApply", method = RequestMethod.PUT)
	public void updateLectureApply(@RequestBody LectureApplyDto lectureApplyDto) {
		lectureApplyUseCaseService.updateLectureApply(lectureApplyDto);
	}

	// 강의 신청 취소 - DELETE 요청
	@RequestMapping(value = "/cancelLectureApplication", method = RequestMethod.DELETE)
	public void cancelLectureApplication(@RequestBody LectureApplyRequest request) {
		lectureApplyUseCaseService.cancelLectureApplication(request.getMemberDto(), request.getLectureInstanceDto());
	}

	// 강의 신청 마감 처리 - POST 요청 (마감일이 지나지 않은 경우에만 신청 가능)
	@RequestMapping(value = "/applyForLectureWithDeadline", method = RequestMethod.POST)
	public void applyForLectureWithDeadline(@RequestBody LectureApplyRequest request) {
		lectureApplyUseCaseService.applyForLecture(request.getMemberDto(), request.getLectureInstanceDto());
	}

	// 신청 내역 필터링 조회 - GET 요청
	@RequestMapping(value = "/getLectureApplicationsByFilters", method = RequestMethod.GET)
	public List<LectureApplyDto> getLectureApplicationsByFilters(
		@RequestParam Long memberId,
		@RequestParam(required = false) LectureStatus status,
		@RequestParam(required = false) LocalDate fromDate,
		@RequestParam(required = false) LocalDate toDate) {

		MemberDto memberDto = new MemberDto(memberId);
		return lectureApplyUseCaseService.getLectureApplicationsByFilters(memberDto, status, fromDate, toDate);
	}

	// DTO 요청을 위한 내부 클래스 정의
	public static class LectureApplyRequest {
		private MemberDto memberDto;
		private LectureInstanceDto lectureInstanceDto;

		// Getter와 Setter 추가
		public MemberDto getMemberDto() {
			return memberDto;
		}

		public void setMemberDto(MemberDto memberDto) {
			this.memberDto = memberDto;
		}

		public LectureInstanceDto getLectureInstanceDto() {
			return lectureInstanceDto;
		}

		public void setLectureInstanceDto(LectureInstanceDto lectureInstanceDto) {
			this.lectureInstanceDto = lectureInstanceDto;
		}
	}
}