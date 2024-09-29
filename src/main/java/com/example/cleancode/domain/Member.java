package com.example.cleancode.domain;

import org.hibernate.validator.constraints.pl.NIP;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull(message = "memberId는 반드시 필요합니다.")
	@Min(value = 1, message = "memberId는 1 이상의 값이어야 합니다.")
	private Long id;

	@NotBlank(message = "이름은 비어 있을 수 없습니다.")
	@Size(max = 10, message = "이름은 10자를 초과할 수 없습니다.")
	private String name;
}
