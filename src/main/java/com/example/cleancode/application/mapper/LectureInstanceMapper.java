package com.example.cleancode.application.mapper;

import com.example.cleancode.adapter.in.dto.LectureInstanceDto;
import com.example.cleancode.domain.Lecture;
import com.example.cleancode.domain.LectureInstance;

public class LectureInstanceMapper {

	public static LectureInstanceDto lectureInstanceEntityToDto(LectureInstance lectureInstance) {
		if (lectureInstance == null) {
			throw new NullPointerException("LectureInstance는 null일 수 없습니다.");
		}

		return new LectureInstanceDto(
			lectureInstance.getId(),
			lectureInstance.getStartDate(),
			lectureInstance.getEndDate(),
			lectureInstance.getMaxParticipants(),
			lectureInstance.getCurrentParticipants(),
			LectureMapper.lectureEntityToDto(lectureInstance.getLecture()),
			lectureInstance.getStatus()
		);
	}


	public static LectureInstance lectureInstanceDtoToEntity(LectureInstanceDto dto) {
		if (dto == null) {
			throw new NullPointerException("LectureInstanceDto는 null일 수 없습니다.");
		}

		LectureInstance lectureInstance = new LectureInstance();
		lectureInstance.setId(dto.getId());
		lectureInstance.setStartDate(dto.getStartDate());
		lectureInstance.setEndDate(dto.getEndDate());
		lectureInstance.setMaxParticipants(dto.getMaxParticipants());
		lectureInstance.setCurrentParticipants(dto.getCurrentParticipants());
		lectureInstance.setStatus(dto.getStatus());
		lectureInstance.setLecture(LectureMapper.lectureDtoToEntity(dto.getLectureDto()));


		return lectureInstance;
	}

}
