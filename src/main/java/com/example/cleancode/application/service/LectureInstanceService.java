package com.example.cleancode.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cleancode.application.useCase.repository.LectureInstanceRepository;

@Service
public class LectureInstanceService {

	private final LectureInstanceRepository lectureInstanceRepository;

	@Autowired
	public LectureInstanceService(LectureInstanceRepository lectureInstanceRepository){
		this.lectureInstanceRepository = lectureInstanceRepository;
	}



}
