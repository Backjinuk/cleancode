package com.example.cleancode.application.useCase;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.cleancode.adapter.in.dto.LectureApplyDto;
import com.example.cleancode.adapter.in.dto.LectureInstanceDto;
import com.example.cleancode.adapter.in.dto.MemberDto;
import com.example.cleancode.application.mapper.LectureApplyMapper;
import com.example.cleancode.application.service.LectureApplyService;
import com.example.cleancode.application.service.LectureInstanceService;
import com.example.cleancode.application.service.LectureService;
import com.example.cleancode.application.validation.DtoValidation;
import com.example.cleancode.domain.LectureApply;
import com.example.cleancode.domain.LectureStatus;

@Service
public class LectureApplyUseCaseService {

	private final LectureApplyService lectureApplyService;
	private final LectureInstanceService lectureInstanceService;
	private final LectureService lectureService;

	// Constructor Injection
	public LectureApplyUseCaseService(LectureApplyService lectureApplyService, LectureInstanceService lectureInstanceService, LectureService lectureService) {
		this.lectureApplyService = lectureApplyService;
		this.lectureInstanceService = lectureInstanceService;
		this.lectureService = lectureService;
	}

	public void applyForLecture(MemberDto memberDto, LectureInstanceDto lectureInstanceDto) {
		DtoValidation.validationMemberDto(memberDto);

		System.out.println("lectureInstanceDto.getStatus() : " + lectureInstanceDto.getStatus());
		DtoValidation.validateLectureInstanceDto(lectureInstanceDto);

		// 1. 회원이 해당 강의 신청한 내역이 있는지 확인
		List<LectureApplyDto> existingApplications = lectureApplyService.getLectureApplyInfoByMemberAndLectureInstance(memberDto.getId(), lectureInstanceDto.getId());

		if (!existingApplications.isEmpty()) {
			throw new IllegalArgumentException("이미 신청한 강의입니다.");
		}

		LectureInstanceDto lectureInstance = lectureInstanceService.getLectureInstance(lectureInstanceDto.getId());
		System.out.println("lectureInstance : " + lectureInstance.getStatus());

		DtoValidation.validateLectureInstanceDto(lectureInstance);

		// 2. 해당 강의의 현재 인원수 확인
		if (lectureInstance.getCurrentParticipants() >= lectureInstance.getMaxParticipants()) {
			throw new IllegalArgumentException("현재 인원수가 최대 수강 인원을 초과할 수 없습니다.");
		}

		// 3. 해당 강의의 상태 확인
		if (lectureInstance.getStatus() != LectureStatus.OPEN) {
			throw new IllegalArgumentException("현재 신청할 수 없는 강의입니다.");
		}

		// 4. 해당 강의의 날짜 확인
		if (lectureInstance.getStartDate().isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("강의 시작일은 오늘 이후여야 합니다.");
		}

		// 5. 해당 강의 신청
		LectureApplyDto lectureApplyDto = new LectureApplyDto(Long.MAX_VALUE, memberDto, lectureInstanceDto);
		lectureApplyService.addLectureApplyInMember(lectureApplyDto);
	}

	// Cancel a lecture application
	public void cancelLectureApplication(MemberDto memberDto, LectureInstanceDto lectureInstanceDto) {
		DtoValidation.validationMemberDto(memberDto);
		DtoValidation.validateLectureInstanceDto(lectureInstanceDto);

		// Check if the application exists
		List<LectureApplyDto> existingApplications = lectureApplyService.getLectureApplyInfoByMemberAndLectureInstance(memberDto.getId(), lectureInstanceDto.getId());
		if (existingApplications.isEmpty()) {
			throw new IllegalArgumentException("신청한 강의가 없습니다.");
		}

		// Cancel the application
		for (LectureApplyDto application : existingApplications) {
			lectureApplyService.cancelLectureApply(application.getId());
		}
	}

	// Get all applications for a member
	public List<LectureApplyDto> getLectureApplicationsByMember(MemberDto memberDto) {
		DtoValidation.validationMemberDto(memberDto);

		return lectureApplyService.getLectureApplyInfoByMember(memberDto.getId());
	}

	// 신청 상태 확인
	public LectureApplyDto getLectureApplicationStatus(MemberDto memberDto, LectureInstanceDto lectureInstanceDto) {

		// 해당 회원이 해당 강의에 신청했는지 확인
		List<LectureApplyDto> existingApplications = lectureApplyService.getLectureApplyInfoByMemberAndLectureInstance(memberDto.getId(), lectureInstanceDto.getId());
		if (existingApplications.isEmpty()) {
			throw new IllegalArgumentException("신청한 강의가 없습니다.");
		}

		// 신청 내역이 있을 경우 상태 반환 (일반적으로 첫 번째 항목)
		return existingApplications.get(0);
	}


	// 신청 정보 수정
	public void updateLectureApply(LectureApplyDto lectureApplyDto) {
		DtoValidation.validationMemberDto(lectureApplyDto.getMemberDto());
		DtoValidation.validateLectureInstanceDto(lectureApplyDto.getLectureInstanceDto());

		// 기존 신청 내역이 있는지 확인
		List<LectureApplyDto> existingApplications = lectureApplyService.getLectureApplyInfoByMemberAndLectureInstance(lectureApplyDto.getMemberDto().getId(), lectureApplyDto.getLectureInstanceDto().getId());
		if (existingApplications.isEmpty()) {
			throw new IllegalArgumentException("신청한 강의가 없습니다.");
		}

		// 신청 내역 수정
		lectureApplyService.updateLectureApply(lectureApplyDto);
	}

	public List<LectureApplyDto> getLectureApplicationsByFilters(MemberDto memberDto, LectureStatus status, LocalDate fromDate, LocalDate toDate) {
		//DtoValidation.validationMemberDto(memberDto);

		return lectureApplyService.getLectureApplyInfoByFilters(memberDto.getId(), status, fromDate, toDate);
	}



}
