package com.example.cleancode.application.useCase.repository;

import java.time.LocalDate;
import java.util.List;

import com.example.cleancode.adapter.in.dto.LectureApplyDto;
import com.example.cleancode.domain.Lecture;
import com.example.cleancode.domain.LectureApply;
import com.example.cleancode.domain.LectureInstance;
import com.example.cleancode.domain.LectureStatus;
import com.example.cleancode.domain.Member;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public interface LectureApplyRepository {

	LectureApply addLectureApplyInMember(LectureApply lectureApply);

	List<LectureApply> getLectureApplyInfoByMember(Long id);

	boolean validateLectureApply(Member member, LectureInstance lectureInstance);

	List<LectureApply> getLectureApplyInfoByMemberAndLectureInstance(
		@NotNull(message = "memberId는 반드시 필요합니다.") @Min(value = 1, message = "memberId는 1 이상의 값이어야 합니다.") Long id, Long id1);

	void cancelLectureApply(@NotNull(message = "lectureApplyId는 비어있을 수 없습니다.") @Min(value = 1, message = "lectureApplyId는 1이상이여야 합니다.") long id);

	void updateLectureApply(LectureApply lectureApply);

	List<LectureApply> getLectureApplyInfoByFilters(@NotNull(message =  "memberId는 반드시 필요합니다.") @Min(value = 1, message = "memberId는 1이상 이여야 합니다.") long id, LectureStatus status, LocalDate fromDate, LocalDate toDate);
}
