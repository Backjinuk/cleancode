package com.example.cleancode.adapter.in.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users/{userId}/lectures")
public class LectureController {

	/*
	 * TODO - 특정 유저의 특강을 조회할 수 있는 메서드 구현
	 * 설명: 주어진 유저 ID로 해당 유저가 신청한 특강 목록을 반환하는 메서드
	 */
	@GetMapping
	public ResponseEntity<?> getUserLectures(@PathVariable Long userId) {
		return ResponseEntity.ok("특강 목록을 반환합니다.");
	}

	/*
	 * TODO - 특정 유저의 특강을 신청할 수 있는 메서드 구현
	 * 설명: 주어진 유저 ID와 특강 ID로 유저가 특강을 신청하는 메서드
	 */
	@GetMapping("/{lectureId}/apply")
	public ResponseEntity<?> applyToLecture(@PathVariable Long userId, @PathVariable Long lectureId) {
		return ResponseEntity.ok("특강 신청 완료");
	}

	/*
	 * TODO - 특정 유저의 특강을 취소할 수 있는 메서드 구현
	 * 설명: 주어진 유저 ID와 특강 ID로 유저가 신청한 특강을 취소하는 메서드
	 */
	@GetMapping("/{lectureId}/cancel")
	public ResponseEntity<?> cancelLecture(@PathVariable Long userId, @PathVariable Long lectureId) {
		return ResponseEntity.ok("특강 신청 취소 완료");
	}

	/*
	 * TODO - 신청 가능한 특강을 조회할 수 있는 메서드 구현
	 * 설명: 현재 신청 가능한 특강 목록을 조회하여 반환하는 메서드
	 */
	@GetMapping("/available")
	public ResponseEntity<?> getAvailableLectures(@PathVariable Long userId) {
		return ResponseEntity.ok("신청 가능한 특강 목록을 반환합니다.");
	}

	/*
	 * TODO - 특정 유저의 모든 특강을 취소할 수 있는 메서드 구현
	 * 설명: 주어진 유저 ID로 해당 유저가 신청한 모든 특강을 취소하는 메서드
	 */
	@GetMapping("/cancel-all")
	public ResponseEntity<?> cancelAllLectures(@PathVariable Long userId) {
		return ResponseEntity.ok("모든 특강 신청 취소 완료");
	}

}
