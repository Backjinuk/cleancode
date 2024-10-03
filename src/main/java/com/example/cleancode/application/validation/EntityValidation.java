package com.example.cleancode.application.validation;

import java.util.Objects;

import javax.swing.text.html.parser.Entity;

import com.example.cleancode.domain.Lecture;
import com.example.cleancode.domain.LectureApply;
import com.example.cleancode.domain.LectureInstance;
import com.example.cleancode.domain.Member;

public class EntityValidation {

	public static void validationMember(Member member){
		if(member.getId() == null || member.getId() < 1){
			throw new IllegalArgumentException("memberId는 1이상의 값이어야 합니다.");
		}

		if(member.getName() == null || member.getName().length() > 10 || member.getName().isEmpty()){
			throw new IllegalArgumentException("이름은 10자를 초과할 수 없습니다.");
		}
	}

	public static void validationLecture(Lecture lecture){
		if(lecture.getId() == null || lecture.getId() < 1){
			throw new IllegalArgumentException("lectureId는 1이상의 값이어야 합니다");
		}

		if(lecture.getTitle() == null || lecture.getTitle().isEmpty()){
			throw new NullPointerException("강의 제목은 비어 있을 수 없습니다.");
		}

		if(lecture.getTitle().length() > 255){
			throw new IllegalArgumentException("강의 제목은 255자를 초과할 수 없습니다.");
		}

		if(lecture.getInstructor() == null || lecture.getInstructor().isEmpty()){
			throw new NullPointerException("강사의 정보는 반드시 필요합니다.");
		}
	}

	public static void validationLectureApply(LectureApply lectureApply){
		if(lectureApply.getId() < 1){
			throw new IllegalArgumentException("lectureApplyId는 1이상의 값이어야 합니다");
		}

		if(lectureApply.getMember() == null){
			throw new NullPointerException("Member는 null일 수 없습니다.");
		}

		if(lectureApply.getLectureInstance() == null){
			throw new NullPointerException("LectureInstance는 null일 수 없습니다.");
		}

		EntityValidation.validationMember(lectureApply.getMember());
		EntityValidation.validateLectureInstance(lectureApply.getLectureInstance());
	}

	public static void validateLectureInstance(LectureInstance lectureInstance) {
		// ID 검증
		if (Objects.isNull(lectureInstance.getId()) || lectureInstance.getId() < 1) {
			throw new IllegalArgumentException("LectureInstanceRepository ID는 1 이상의 값이어야 합니다.");
		}

		// 시작일 검증
		if (Objects.isNull(lectureInstance.getStartDate())) {
			throw new NullPointerException("강의 시작일은 null일 수 없습니다.");
		}

		// 종료일 검증
		if (Objects.isNull(lectureInstance.getEndDate())) {
			throw new NullPointerException("강의 종료일은 null일 수 없습니다.");
		}

		if (lectureInstance.getEndDate().isBefore(lectureInstance.getStartDate())) {
			throw new IllegalArgumentException("강의 종료일은 시작일보다 이후여야 합니다.");
		}

		// 최대 수강 인원 검증
		if (lectureInstance.getMaxParticipants() < 1) {
			throw new IllegalArgumentException("최대 수강 인원은 1 이상이어야 합니다.");
		}

		// 현재 수강 인원 검증
		if (lectureInstance.getCurrentParticipants() < 0) {
			throw new IllegalArgumentException("현재 수강 인원은 0 이상이어야 합니다.");
		}

		if (lectureInstance.getCurrentParticipants() > lectureInstance.getMaxParticipants()) {
			throw new IllegalArgumentException("현재 수강 인원은 최대 수강 인원을 초과할 수 없습니다.");
		}

		// 강의 상태 검증
		if (lectureInstance.getStatus() == null) {
			throw new NullPointerException("강의 상태는 반드시 필요합니다.");
		}

		// 강의 검증
		if (lectureInstance.getLecture() == null) {
			throw new NullPointerException("Lecture는 null일 수 없습니다.");
		}

		EntityValidation.validationLecture(lectureInstance.getLecture());
	}
}
