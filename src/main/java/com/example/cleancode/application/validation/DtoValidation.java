package com.example.cleancode.application.validation;

import java.util.Objects;

import com.example.cleancode.adapter.in.dto.LectureApplyDto;
import com.example.cleancode.adapter.in.dto.LectureDto;
import com.example.cleancode.adapter.in.dto.LectureInstanceDto;
import com.example.cleancode.adapter.in.dto.MemberDto;
public class DtoValidation {

	public static void validationMemberDto(MemberDto memberDto){
		if(memberDto == null){
			throw new NullPointerException("MemberDto는 null일 수 없습니다.");
		}

		if(memberDto.getId() < 1){
			throw new IllegalArgumentException("MemberDtoId는 1이상의 값이어야 합니다.");
		}

		if(memberDto.getName() == null || memberDto.getName().length() > 10 || memberDto.getName().isEmpty()){
			throw new IllegalArgumentException("이름은 10자를 초과할 수 없습니다.");
		}
	}

	public static void validationLectureDto(LectureDto lectureDto){
		if(lectureDto == null){
			throw new NullPointerException("LectureDto는 null일 수 없습니다.");
		}
		if(lectureDto.getId() < 1){
			throw new IllegalArgumentException("LectureDtoId는 1이상의 값이어야 합니다.");
		}

		if(lectureDto.getTitle() == null || lectureDto.getTitle().isEmpty()){
			throw new NullPointerException("강의 제목은 비어 있을 수 없습니다.");
		}

		if(lectureDto.getTitle().length() > 255){
			throw new IllegalArgumentException("강의 제목은 255자를 초과할 수 없습니다.");
		}

		if(lectureDto.getInstructor() == null || lectureDto.getInstructor().isEmpty()){
			throw new NullPointerException("강사의 정보는 반드시 필요합니다.");
		}
	}

	public static void validationLectureApplyDto(LectureApplyDto lectureApplyDto){
		if(lectureApplyDto == null){
			throw new NullPointerException("LectureApplyDto는 null일 수 없습니다.");
		}

		if(lectureApplyDto.getId() < 1){
			throw new IllegalArgumentException("LectureApplyDto ID는 1 이상의 값이어야 합니다.");
		}

		if(lectureApplyDto.getMemberDto() == null){
			throw new NullPointerException("MemberDto는 null일 수 없습니다.");
		}

		if(lectureApplyDto.getLectureInstanceDto() == null){
			throw new NullPointerException("LectureInstanceDto는 null일 수 없습니다.");
		}

		DtoValidation.validationMemberDto(lectureApplyDto.getMemberDto());
		DtoValidation.validateLectureInstanceDto(lectureApplyDto.getLectureInstanceDto());
	}

	public static void validateLectureInstanceDto(LectureInstanceDto lectureInstanceDto) {
		if(lectureInstanceDto == null){
			throw new NullPointerException("LectureInstance는 null일 수 없습니다.");
		}

		// ID 검증
		if (lectureInstanceDto.getId() == null || lectureInstanceDto.getId() < 1) {
			throw new IllegalArgumentException("LectureInstanceDto ID는 1 이상의 값이어야 합니다.");
		}

		// 시작일 검증
		if (lectureInstanceDto.getStartDate() == null) {
			throw new NullPointerException("강의 시작일은 null일 수 없습니다.");
		}

		// 종료일 검증
		if (lectureInstanceDto.getEndDate() == null) {
			throw new NullPointerException("강의 종료일은 null일 수 없습니다.");
		}

		if (lectureInstanceDto.getEndDate().isBefore(lectureInstanceDto.getStartDate())) {
			throw new IllegalArgumentException("강의 종료일은 시작일보다 이후여야 합니다.");
		}

		// 최대 수강 인원 검증
		if (lectureInstanceDto.getMaxParticipants() < 1) {
			throw new IllegalArgumentException("최대 수강 인원은 1 이상이어야 합니다.");
		}

		// 현재 수강 인원 검증
		if (lectureInstanceDto.getCurrentParticipants() < 0) {
			throw new IllegalArgumentException("현재 수강 인원은 0 이상이어야 합니다.");
		}

		if (lectureInstanceDto.getCurrentParticipants() > lectureInstanceDto.getMaxParticipants()) {
			throw new IllegalArgumentException("현재 수강 인원은 최대 수강 인원을 초과할 수 없습니다.");
		}

		// 강의 상태 검증
		if (lectureInstanceDto.getStatus() == null) {
			throw new NullPointerException("강의 상태는 반드시 필요합니다.");
		}

		if(lectureInstanceDto.getLectureDto() == null ){
			throw new NullPointerException("LectureDto는 null일 수 없습니다.");
		}
		// Lecture ID 검증
		if (lectureInstanceDto.getLectureDto().getId() < 1) {
			throw new IllegalArgumentException("Lecture ID는 1 이상의 값이어야 합니다.");
		}

		DtoValidation.validationLectureDto(lectureInstanceDto.getLectureDto());
	}
}
