package com.example.cleancode.util.mapper;

import com.example.cleancode.domain.LectureApply;
import com.example.cleancode.adapter.in.dto.LectureApplyDto;

public class LectureApplyMapper {

	public static LectureApply lectureApplyDtoToEntity(LectureApplyDto lectureApplyDto){
		if(lectureApplyDto == null){
			throw new NullPointerException("lectureApplyDto의 값은 null일 수 없습니다.");
		}

		return new LectureApply(lectureApplyDto.getId(),
			MemberMapper.memberDtoToEntity(lectureApplyDto.getMemberDto()),
			LectureMapper.lectureDtoToEntity(lectureApplyDto.getLectureDto())
		);
	}


	public static LectureApplyDto lectureApplyEntityToDto(LectureApply lectureApply){
		if(lectureApply == null){
			throw new NullPointerException("lectureApply의 값은 null일 수 없습니다.");
		}

		return new LectureApplyDto(lectureApply.getId(),
			MemberMapper.memberEntityToDto(lectureApply.getMember()),
			LectureMapper.lectureEntityToDto(lectureApply.getLecture())
		);
	}
}
