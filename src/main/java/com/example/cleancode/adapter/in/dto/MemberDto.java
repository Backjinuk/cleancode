package com.example.cleancode.adapter.in.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberDto {

	@NotNull(message =  "memberId는 반드시 필요합니다.")
	@Min(value = 1, message = "memberId는 1이상 이여야 합니다.")
	public long id;

	@NotBlank(message = "이름은 비어 있을 수 없습니다.")
	@Size(max = 5, message = "이름은 5자를 초과할 수 없습니다.")
	public String name;

}
