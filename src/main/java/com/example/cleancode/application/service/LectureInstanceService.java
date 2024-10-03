package com.example.cleancode.application.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cleancode.adapter.in.dto.LectureInstanceDto;
import com.example.cleancode.application.mapper.LectureInstanceMapper;
import com.example.cleancode.application.useCase.repository.LectureInstanceRepository;
import com.example.cleancode.application.validation.EntityValidation;
import com.example.cleancode.domain.LectureInstance;

@Service
public class LectureInstanceService {

	private final LectureInstanceRepository lectureInstanceRepository;

	@Autowired
	public LectureInstanceService(LectureInstanceRepository lectureInstanceRepository) {
		this.lectureInstanceRepository = lectureInstanceRepository;
	}

	public boolean addLectureInLectureInstance(LectureInstanceDto lectureInstanceDto) {
		LectureInstance lectureInstance = LectureInstanceMapper.lectureInstanceDtoToEntity(lectureInstanceDto);
		EntityValidation.validateLectureInstance(lectureInstance);

		if (lectureInstance.getStartDate().isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("강의 시작일은 오늘 이후여야 합니다.");
		}

		return lectureInstanceRepository.addLectureInstance(lectureInstance);
	}

	public List<LectureInstanceDto> getAllLectureInstance() {
		return lectureInstanceRepository.getAllLectureInstance().stream()
			.map(LectureInstanceMapper::lectureInstanceEntityToDto)
			.toList();
	}

	public LectureInstanceDto incrementCurrentParticipants(LectureInstanceDto lectureInstanceDto) {
		LectureInstance lectureInstance = LectureInstanceMapper.lectureInstanceDtoToEntity(lectureInstanceDto);
		lectureInstance.setCurrentParticipants(lectureInstance.getCurrentParticipants() + 1);
		EntityValidation.validateLectureInstance(lectureInstance);

		return LectureInstanceMapper.lectureInstanceEntityToDto(
			lectureInstanceRepository.increnentCurrentParticpants(lectureInstance)
		);
	}

	public LectureInstanceDto getLectureInstance(Long id) {
		LectureInstance lectureInstance = lectureInstanceRepository.getLectureInstance(id);
		if (lectureInstance == null) {
			throw new NullPointerException("해당 강의 인스턴스는 존재하지 않습니다.");
		}
		return LectureInstanceMapper.lectureInstanceEntityToDto(lectureInstance);
	}

	public LectureInstanceDto decrementCurrentParticipants(LectureInstanceDto lectureInstanceDto) {
		LectureInstance lectureInstance = LectureInstanceMapper.lectureInstanceDtoToEntity(lectureInstanceDto);
		if (lectureInstance.getCurrentParticipants() <= 0) {
			throw new IllegalArgumentException("현재 수강 인원은 0 이상이어야 합니다.");
		}
		lectureInstance.setCurrentParticipants(lectureInstance.getCurrentParticipants() - 1);
		EntityValidation.validateLectureInstance(lectureInstance);

		return LectureInstanceMapper.lectureInstanceEntityToDto(
			lectureInstanceRepository.decrementCurrentParticipants(lectureInstance)
		);
	}

	public LectureInstanceDto updateLectureInLectureInstance(LectureInstanceDto lectureInstanceDto) {
		System.out.println("lectureInstanceDto.getStatus() : " + lectureInstanceDto.getStatus());

		LectureInstance lectureInstance = LectureInstanceMapper.lectureInstanceDtoToEntity(lectureInstanceDto);

		System.out.println("lectureIstance.getSatus : " + lectureInstance.getStatus());
		//EntityValidation.validateLectureInstance(lectureInstance);

		LectureInstance updatedInstance = lectureInstanceRepository.updateLectureInLectureInstance(lectureInstance);

		if (updatedInstance == null) {
			throw new NullPointerException("해당 강의 인스턴스는 존재하지 않습니다.");
		}

		return LectureInstanceMapper.lectureInstanceEntityToDto(updatedInstance);
	}

	public boolean deleteLectureInLectureInstance(Long lectureInstanceId) {
		boolean isDeleted = lectureInstanceRepository.deleteLectureInstance(lectureInstanceId);

		if (!isDeleted) {
			throw new NullPointerException("해당 강의 인스턴스는 존재하지 않습니다.");
		}

		return true;
	}

	public List<LectureInstanceDto> getLectureInstancesByIdAndDate(Long id, LocalDate date) {
		return lectureInstanceRepository.getLectureInstancesByIdAndDate(id, date).stream().map(
			LectureInstanceMapper::lectureInstanceEntityToDto).toList();
	}
}
