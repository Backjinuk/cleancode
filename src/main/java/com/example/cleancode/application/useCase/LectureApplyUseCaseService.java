package com.example.cleancode.application.useCase;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.example.cleancode.adapter.in.dto.LectureApplyDto;
import com.example.cleancode.adapter.in.dto.LectureInstanceDto;
import com.example.cleancode.adapter.in.dto.MemberDto;

import com.example.cleancode.application.service.LectureApplyService;
import com.example.cleancode.application.service.LectureInstanceService;
import com.example.cleancode.application.service.LectureService;
import com.example.cleancode.application.validation.DtoValidation;

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


	@Transactional
	public void applyForLecture(MemberDto memberDto, LectureInstanceDto lectureInstanceDto) {
		// 비관적 락을 걸고, 트랜잭션 내에서 동시성 문제를 해결
		LectureInstanceDto lectureInstance = lectureInstanceService.getLectureInstance(lectureInstanceDto.getId());

		List<LectureApplyDto> lectureApplyDtoList = lectureApplyService.getLectureApplyInfoByMemberAndLectureInstance(
			memberDto.id, lectureInstanceDto.getId());

		if(!lectureApplyDtoList.isEmpty()){
			throw new IllegalArgumentException("이미 신청된 강의 목록입니다.");
		}

		if (lectureInstance.getCurrentParticipants() >= lectureInstance.getMaxParticipants()) {
			throw new IllegalArgumentException("현재 인원수가 최대 수강 인원을 초과할 수 없습니다.");
		}

		// 참가자 수 증가
		lectureInstanceService.incrementCurrentParticipants(lectureInstanceDto);

		// 강의 신청 처리
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
