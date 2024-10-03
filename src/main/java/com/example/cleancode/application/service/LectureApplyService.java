package com.example.cleancode.application.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cleancode.adapter.in.dto.LectureApplyDto;
import com.example.cleancode.application.mapper.LectureApplyMapper;
import com.example.cleancode.application.mapper.LectureMapper;
import com.example.cleancode.application.useCase.repository.LectureApplyRepository;
import com.example.cleancode.application.validation.EntityValidation;
import com.example.cleancode.domain.LectureApply;
import com.example.cleancode.domain.LectureStatus;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Service
public class LectureApplyService {

	private final LectureApplyRepository lectureApplyRepository;

	@Autowired
	public  LectureApplyService(LectureApplyRepository lectureApplyRepository){
		this.lectureApplyRepository = lectureApplyRepository;
	}

	public LectureApplyDto addLectureApplyInMember(LectureApplyDto lectureApplyDto) {
		LectureApply lectureApply = LectureApplyMapper.lectureApplyDtoToEntity(lectureApplyDto);
		EntityValidation.validationLectureApply(lectureApply);

		if(lectureApply.getLectureInstance().getStatus().equals(LectureStatus.CLOSE)){
			throw new IllegalArgumentException("종료된 강의에는 신청할 수 없습니다.");
		}

		return LectureApplyMapper.lectureApplyEntityToDto(
			lectureApplyRepository.addLectureApplyInMember(lectureApply)
		);
	}

	public List<LectureApplyDto> getLectureApplyInfoByMember(
		@NotNull(message = "memberId는 반드시 필요합니다.") @Min(value = 1, message = "memberId는 1 이상의 값이어야 합니다.") Long id) {

		return lectureApplyRepository.getLectureApplyInfoByMember(id).stream()
			.map(LectureApplyMapper::lectureApplyEntityToDto)
			.toList();
	}

	public void validateLectureApply(LectureApplyDto lectureApplyDto) {
		LectureApply lectureApply = LectureApplyMapper.lectureApplyDtoToEntity(lectureApplyDto);
		EntityValidation.validationLectureApply(lectureApply); // 유효성 검증

		boolean existsCheck = lectureApplyRepository.validateLectureApply(
			lectureApply.getMember(),
			lectureApply.getLectureInstance()
		);

		if (existsCheck) {
			throw new IllegalArgumentException("이미 신청한 강의입니다."); // 예외 발생
		}
	}

	public List<LectureApplyDto> getLectureApplyInfoByMemberAndLectureInstance(
		@NotNull(message = "memberId는 반드시 필요합니다.") @Min(value = 1, message = "memberId는 1 이상의 값이어야 합니다.") Long memberId, Long lectureInstanceId) {

		return lectureApplyRepository.getLectureApplyInfoByMemberAndLectureInstance(memberId, lectureInstanceId).stream().map(LectureApplyMapper::lectureApplyEntityToDto).toList();
	}

	public void cancelLectureApply(@NotNull(message = "lectureApplyId는 비어있을 수 없습니다.") @Min(value = 1, message = "lectureApplyId는 1이상이여야 합니다.") long id) {
		lectureApplyRepository.cancelLectureApply(id);
	}

	public void updateLectureApply(LectureApplyDto lectureApplyDto) {
		lectureApplyRepository.updateLectureApply(LectureApplyMapper.lectureApplyDtoToEntity(lectureApplyDto));
	}

	public List<LectureApplyDto> getLectureApplyInfoByFilters(@NotNull(message =  "memberId는 반드시 필요합니다.") @Min(value = 1, message = "memberId는 1이상 이여야 합니다.") long id, LectureStatus status, LocalDate fromDate, LocalDate toDate) {
		return lectureApplyRepository.getLectureApplyInfoByFilters(id,status, fromDate, toDate)
			.stream().map(LectureApplyMapper::lectureApplyEntityToDto)
			.toList();
	}
}
