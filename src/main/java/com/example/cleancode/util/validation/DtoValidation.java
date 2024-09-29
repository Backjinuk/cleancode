package com.example.cleancode.util.validation;

import com.example.cleancode.adapter.in.dto.LectureApplyDto;
import com.example.cleancode.adapter.in.dto.LectureDto;
import com.example.cleancode.adapter.in.dto.MemberDto;
public class DtoValidation {

	public static void validationMemberDto(MemberDto memberDto){
		if(memberDto.getId() < 1){
			throw new IllegalArgumentException("MemberDtoId는 1이상의 값이어야 합니다.");
		}

		if(memberDto.getName() == null || memberDto.getName().length() > 10 || memberDto.getName().isEmpty()){
			throw new IllegalArgumentException("이름은 10자를 초과할 수 없습니다.");
		}
	}

	public static void validationLectureDto(LectureDto lectureDto){
		if(lectureDto.getId() < 1){
			throw new IllegalArgumentException("LectureDtoId는 1이상의 값이어야 합니다");
		}

		if(lectureDto.getTitle() == null || lectureDto.getTitle().isEmpty()){
			throw new NullPointerException("강의 제목은 비어 있을 수 없습니다.");
		}

		if(lectureDto.getTitle().length() > 255){
			throw new IllegalArgumentException("강의 제목은 255자를 초과할 수 없습니다.");
		}

		if(lectureDto.getMaxParticipants() < 1){
			throw new IllegalArgumentException("참가자 수는 1 이상이어야 합니다.");
		}

		if(lectureDto.getStatus() == null){
			throw new NullPointerException("강의 상태는 반드시 필요합니다.");
		}
	}

	public static void validationLectureApplyDto(LectureApplyDto lectureApplyDto){
		if(lectureApplyDto.getId() < 1){
			throw new IllegalArgumentException("LectureApplyDtoId는 1이상의 값이어야 합니다");
		}

		if(lectureApplyDto.getMemberDto() == null){
			throw new NullPointerException("MemberDto는 null일 수 없습니다.");
		}

		if(lectureApplyDto.getLectureDto() == null){
			throw new NullPointerException("LectureDto는 null일 수 없습니다.");
		}

	}
}
