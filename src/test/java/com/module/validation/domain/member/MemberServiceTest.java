package com.module.validation.domain.member;

import com.module.validation.domain.exception.ValidCustomException;
import com.module.validation.domain.member.dto.MemberRequestDto;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @BeforeEach
    public void before() {
        memberService.save(new MemberRequestDto(0L, "김확인", "01011112223", "test1@naver.com"));
        memberService.save(new MemberRequestDto(1L, "김테스트", "01011112222", "test2@naver.com"));
    }

    @DisplayName("중복된 메일이 입력되면 에러를 발생시켜야한다.")
    @Test
    public void test_duplicateEmail() throws Exception {
        // Given
        MemberRequestDto duplicateEmailUser = new MemberRequestDto(2L, "이테스트", "01011112225", "test2@naver.com");
        // When

        // Then
        Assertions.assertThrows(ValidCustomException.class, () -> memberService.save(duplicateEmailUser));
    }
}