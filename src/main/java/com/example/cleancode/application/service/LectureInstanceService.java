package com.example.cleancode.application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cleancode.adapter.in.dto.LectureDto;
import com.example.cleancode.adapter.in.dto.LectureInstanceDto;
import com.example.cleancode.application.mapper.LectureInstanceMapper;
import com.example.cleancode.application.useCase.repository.LectureInstanceRepository;
import com.example.cleancode.domain.LectureInstance;

@Service
public class LectureInstanceService {

	private final LectureInstanceRepository lectureInstanceRepository;

	@Autowired
	public LectureInstanceService(LectureInstanceRepository lectureInstanceRepository){
		this.lectureInstanceRepository = lectureInstanceRepository;
	}

	public boolean getLectureInLectureInstance(LectureInstance lectureInstance) {
		return lectureInstanceRepository.addLectureInLectureInstance(lectureInstance);
	}

	public List<LectureInstanceDto> getAllLectureInstance() {
		return lectureInstanceRepository.getAllLectureInstance().stream()
			.map(LectureInstanceMapper::lectureInstanceEntityToDto).toList();
	}

	public LectureInstanceDto incenentCurrentParticpants(LectureInstance lectureInstance) {
		return LectureInstanceMapper.lectureInstanceEntityToDto(
			lectureInstanceRepository.increnentCurrentParticpants(lectureInstance)
		);
	}

	public LectureInstanceDto getLectureInstance(Long id) {
		try {
			LectureInstance lectureInstance = lectureInstanceRepository.getLectureInstance(id);

			return LectureInstanceMapper.lectureInstanceEntityToDto(lectureInstance);
		} catch (Exception e) {
			throw new NullPointerException("해당 강의 인스턴스는 존재하지 않습니다.");
		}
	}
}
