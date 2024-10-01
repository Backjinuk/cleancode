package com.example.cleancode.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cleancode.application.useCase.repository.LectureApplyRepository;

@Service
public class LectureApplyService {

	private final LectureApplyRepository lectureApplyRepository;

	@Autowired
	public  LectureApplyService(LectureApplyRepository lectureApplyRepository){
		this.lectureApplyRepository = lectureApplyRepository;
	}
}
