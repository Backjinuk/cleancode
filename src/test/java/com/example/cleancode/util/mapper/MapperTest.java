package com.example.cleancode.util.mapper;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.cleancode.adapter.in.dto.LectureApplyDto;
import com.example.cleancode.adapter.in.dto.LectureDto;
import com.example.cleancode.adapter.in.dto.MemberDto;
import com.example.cleancode.application.mapper.LectureApplyMapper;
import com.example.cleancode.application.mapper.LectureMapper;
import com.example.cleancode.application.mapper.MemberMapper;
import com.example.cleancode.domain.Lecture;
import com.example.cleancode.domain.LectureApply;
import com.example.cleancode.domain.LectureStatus;
import com.example.cleancode.domain.Member;

@SpringBootTest
class MapperTest {

	@Test
	@DisplayName("정상적인memberDto가 Entity로 변환")
	void 정상적인_memberDto가_Entity로_변환() {
		// given
		MemberDto memberDto = new MemberDto(1, "Member01");

		// when
		Member member = MemberMapper.memberDtoToEntity(memberDto);

		// then
		assertThat(member.getId()).isEqualTo(memberDto.getId());
		assertThat(member.getName()).isEqualTo(memberDto.getName());
	}

	@Test
	@DisplayName("Member에서 MemberDto로 변환 테스트")
	void memberEntityToDtoTest() {
		// given
		Member member = new Member(1L, "John Doe");

		// when
		MemberDto memberDto = MemberMapper.memberEntityToDto(member);

		// then
		assertThat(memberDto.getId()).isEqualTo(member.getId());
		assertThat(memberDto.getName()).isEqualTo(member.getName());
	}

	@Test
	@DisplayName("LectureDto에서 Lecture로 변환 테스트")
	void LectureDto에서_Lecture로_변환_테스트() {
		// given
		LectureDto lectureDto = new LectureDto(1, "lecture101", 30, LectureStatus.OPEN);

		// when
		Lecture lecture = LectureMapper.lectureDtoToEntity(lectureDto);

		// then
		assertThat(lecture.getId()).isEqualTo(lectureDto.getId());
		assertThat(lecture.getTitle()).isEqualTo(lectureDto.getTitle());
		assertThat(lecture.getMaxParticipants()).isEqualTo(lectureDto.getMaxParticipants());
		assertThat(lecture.getStatus()).isEqualTo(lectureDto.getStatus());
	}

	@Test
	@DisplayName("Lecture에서 LectureDto로 변환")
	void Lecture에서_LecutreDto로_변환() {
		// given
		Lecture lecture = new Lecture(1L, "lecture101", 30, LectureStatus.OPEN);

		// when
		LectureDto lectureDto = LectureMapper.lectureEntityToDto(lecture);

		// then
		assertThat(lectureDto.getId()).isEqualTo(lecture.getId());
		assertThat(lectureDto.getTitle()).isEqualTo(lecture.getTitle());
		assertThat(lectureDto.getMaxParticipants()).isEqualTo(lecture.getMaxParticipants());
		assertThat(lectureDto.getStatus()).isEqualTo(lecture.getStatus());

	}

	@Test
	@DisplayName("LectureApplyDto에서 LectureApply로 변환")
	void LectureApplyDto에서_LectureApply로_변화() {
		// given
		MemberDto memberDto = new MemberDto(1L, "Member01");
		LectureDto lectureDto = new LectureDto(1L, "lecture101", 30, LectureStatus.OPEN);
		LectureApplyDto lectureApplyDto = new LectureApplyDto(1L, memberDto, lectureDto);

		// when
		LectureApply lectureApply = LectureApplyMapper.lectureApplyDtoToEntity(lectureApplyDto);

		// then
		// ID 검증
		assertThat(lectureApply.getId()).isEqualTo(lectureApplyDto.getId());

		// Lecture 검증
		assertThat(lectureApply.getLecture().getId()).isEqualTo(lectureDto.getId());
		assertThat(lectureApply.getLecture().getTitle()).isEqualTo(lectureDto.getTitle());
		assertThat(lectureApply.getLecture().getMaxParticipants()).isEqualTo(lectureDto.getMaxParticipants());
		assertThat(lectureApply.getLecture().getStatus()).isEqualTo(lectureDto.getStatus());

		// Member 검증
		assertThat(lectureApply.getMember().getId()).isEqualTo(memberDto.getId());
		assertThat(lectureApply.getMember().getName()).isEqualTo(memberDto.getName());
	}


		@Test
		@DisplayName("LectureApplyDto에서 LectureApply로 변환")
		void LectureApplyDto에서_LectureApply로_변환() {
			// given
			Member member = new Member(1L, "Member01");
			Lecture lecture = new Lecture(1L, "lecture101", 30, LectureStatus.OPEN);
			LectureApply lectureApply = new LectureApply(1L, member, lecture);

			// when
			// Mapper를 사용하지 않고, 실제 Entity 값을 이용해서 LectureApply 생성
			LectureApplyDto lectureApplyDto = new LectureApplyDto(
				lectureApply.getId(),
				new MemberDto(member.getId(), member.getName()),
				new LectureDto(lecture.getId(), lecture.getTitle(), lecture.getMaxParticipants(), lecture.getStatus())
			);

			// then
			// ID 검증
			assertThat(lectureApply.getId()).isEqualTo(lectureApplyDto.getId());

			// Lecture 검증
			assertThat(lectureApply.getLecture().getId()).isEqualTo(lectureApplyDto.getLectureDto().getId());
			assertThat(lectureApply.getLecture().getTitle()).isEqualTo(lectureApplyDto.getLectureDto().getTitle());
			assertThat(lectureApply.getLecture().getMaxParticipants()).isEqualTo(lectureApplyDto.getLectureDto().getMaxParticipants());
			assertThat(lectureApply.getLecture().getStatus()).isEqualTo(lectureApplyDto.getLectureDto().getStatus());

			// Member 검증
			assertThat(lectureApply.getMember().getId()).isEqualTo(lectureApplyDto.getMemberDto().getId());
			assertThat(lectureApply.getMember().getName()).isEqualTo(lectureApplyDto.getMemberDto().getName());
		}
}