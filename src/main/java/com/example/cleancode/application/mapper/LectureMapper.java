package com.example.cleancode.application.mapper;

import com.example.cleancode.domain.Lecture;
import com.example.cleancode.adapter.in.dto.LectureDto;

public class LectureMapper {

	public static Lecture lectureDtoToEntity(LectureDto lectureDto){
		if(lectureDto == null){
			throw new NullPointerException("lectureDto의 값은 null일 수 없습니다.");
		}

		return new Lecture(lectureDto.getId(), lectureDto.getTitle(), lectureDto.getMaxParticipants(), lectureDto.getStatus());
	}


	public static LectureDto lectureEntityToDto(Lecture lecture){
		if(lecture == null){
			throw new NullPointerException("lecture의 값은 null일수 없습니다.");
		}

		return new LectureDto(lecture.getId(), lecture.getTitle(), lecture.getMaxParticipants(), lecture.getStatus());
	}
}
