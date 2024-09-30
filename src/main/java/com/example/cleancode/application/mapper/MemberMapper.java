package com.example.cleancode.application.mapper;

import com.example.cleancode.domain.Member;
import com.example.cleancode.adapter.in.dto.MemberDto;

public class MemberMapper {

	public static Member memberDtoToEntity(MemberDto memberDto){
		if(memberDto == null){
			throw new NullPointerException("memberDto의 값은 null일수 없습니다.");
		}

		return new Member(memberDto.getId(), memberDto.getName());
	}

	public static MemberDto memberEntityToDto(Member member){
		if(member == null){
			throw new NullPointerException("member의 값은 null일수 없습니다.");
		}

		return new MemberDto(member.getId(), member.getName());
	}
}
