package com.example.cleancode.adapter.in.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class LectureApplyDto {

	@NotNull(message = "lectureApplyId는 비어있을 수 없습니다.")
	@Min(value = 1, message = "lectureApplyId는 1이상이여야 합니다.")
	private long id;

	@NotNull(message = "memberDtos는 null일 수 없습니다.")
	private MemberDto memberDto;

	@NotNull(message = "lectureDto는 null일 수 없습니다.")
	private LectureDto lectureDto;
}
