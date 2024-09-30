package com.example.cleancode.application.validation;

import com.example.cleancode.domain.Lecture;
import com.example.cleancode.domain.LectureApply;
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

		if(lecture.getMaxParticipants() < 1){
			throw new IllegalArgumentException("참가자 수는 1 이상이어야 합니다.");
		}

		if(lecture.getStatus() == null){
			throw new NullPointerException("강의 상태는 반ㄷ시 필요합니다.");
		}
	}

	public static void validationLectureApply(LectureApply lectureApply){
		if(lectureApply.getId() < 1){
			throw new IllegalArgumentException("lectureApplyId는 1이상의 값이어야 합니다");
		}

		if(lectureApply.getMember() == null){
			throw new NullPointerException("Member는 null일 수 없습니다.");
		}

		if(lectureApply.getLecture() == null){
			throw new NullPointerException("Lecture는 null일 수 없습니다.");
		}

	}
}
