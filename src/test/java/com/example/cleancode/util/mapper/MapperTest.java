package com.example.cleancode.util.mapper;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.cleancode.adapter.in.dto.LectureDto;
import com.example.cleancode.adapter.in.dto.LectureInstanceDto;
import com.example.cleancode.adapter.in.dto.MemberDto;
import com.example.cleancode.application.mapper.LectureInstanceMapper;
import com.example.cleancode.application.mapper.LectureMapper;
import com.example.cleancode.application.mapper.MemberMapper;
import com.example.cleancode.domain.Lecture;
import com.example.cleancode.domain.LectureInstance;
import com.example.cleancode.domain.LectureStatus;
import com.example.cleancode.domain.Member;

@SpringBootTest
class MapperTest {

	@Test
	@DisplayName("정상적인 memberDto가 Entity로 변환")
	void 정상적인_memberDto가_Entity로_변환() {
		// given
		MemberDto memberDto = new MemberDto(1L, "Member01");

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
		LectureDto lectureDto = new LectureDto(1L, "lecture101", "John Doe");

		// when
		Lecture lecture = LectureMapper.lectureDtoToEntity(lectureDto);

		// then
		assertThat(lecture.getId()).isEqualTo(lectureDto.getId());
		assertThat(lecture.getTitle()).isEqualTo(lectureDto.getTitle());
		assertThat(lecture.getInstructor()).isEqualTo(lectureDto.getInstructor());
	}

	@Test
	@DisplayName("Lecture에서 LectureDto로 변환")
	void Lecture에서_LecutreDto로_변환() {
		// given
		Lecture lecture = new Lecture(1L, "lecture101", "John Doe");

		// when
		LectureDto lectureDto = LectureMapper.lectureEntityToDto(lecture);

		// then
		assertThat(lectureDto.getId()).isEqualTo(lecture.getId());
		assertThat(lectureDto.getTitle()).isEqualTo(lecture.getTitle());
		assertThat(lectureDto.getInstructor()).isEqualTo(lecture.getInstructor());
	}

	@Test
	@DisplayName("LectureInstanceDto에서 LectureInstance로 변환 테스트")
	void LectureInstanceDto에서_LectureInstance로_변환_테스트() {
		// given
		LectureDto lectureDto = new LectureDto(1L, "lecture101", "John Doe");
		LectureInstanceDto lectureInstanceDto = new LectureInstanceDto(1L, LocalDate.of(2024, 1, 1),
			LocalDate.of(2024, 3, 1), 30, 10, lectureDto, LectureStatus.OPEN);

		// when
		LectureInstance lectureInstance = LectureInstanceMapper.lectureInstanceDtoToEntity(lectureInstanceDto);

		// then
		assertThat(lectureInstance.getId()).isEqualTo(lectureInstanceDto.getId());
		assertThat(lectureInstance.getStartDate()).isEqualTo(lectureInstanceDto.getStartDate());
		assertThat(lectureInstance.getEndDate()).isEqualTo(lectureInstanceDto.getEndDate());
		assertThat(lectureInstance.getMaxParticipants()).isEqualTo(lectureInstanceDto.getMaxParticipants());
		assertThat(lectureInstance.getCurrentParticipants()).isEqualTo(lectureInstanceDto.getCurrentParticipants());
		assertThat(lectureInstance.getLecture().getId()).isEqualTo(lectureInstanceDto.getLectureDto().getId());
		assertThat(lectureInstance.getStatus()).isEqualTo(lectureInstanceDto.getStatus());
	}

	@Test
	@DisplayName("LectureInstance에서 LectureInstanceDto로 변환")
	void LectureInstance에서_LecutreInstanceDto로_변환() {
		// given
		Lecture lecture = new Lecture(1L, "lecture101", "John Doe");
		LectureInstance lectureInstance = new LectureInstance(1L, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 3, 1),
			30, 10, lecture, LectureStatus.OPEN);

		// when
		LectureInstanceDto lectureInstanceDto = LectureInstanceMapper.lectureInstanceEntityToDto(lectureInstance);

		// then
		assertThat(lectureInstanceDto.getId()).isEqualTo(lectureInstance.getId());
		assertThat(lectureInstanceDto.getStartDate()).isEqualTo(lectureInstance.getStartDate());
		assertThat(lectureInstanceDto.getEndDate()).isEqualTo(lectureInstance.getEndDate());
		assertThat(lectureInstanceDto.getMaxParticipants()).isEqualTo(lectureInstance.getMaxParticipants());
		assertThat(lectureInstanceDto.getCurrentParticipants()).isEqualTo(lectureInstance.getCurrentParticipants());
		assertThat(lectureInstanceDto.getLectureDto().getId()).isEqualTo(lecture.getId());
		assertThat(lectureInstanceDto.getStatus()).isEqualTo(lectureInstance.getStatus());
	}
}
