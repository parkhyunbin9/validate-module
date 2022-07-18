package com.module.validation.domain.member;

import com.module.validation.domain.exception.ValidCustomException;
import com.module.validation.domain.member.dto.MemberRequestDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @BeforeAll
    public void before() {
        memberService.save(new MemberRequestDto(0L, "김확인", "01011112223", "test1@naver.com"));
        memberService.save(new MemberRequestDto(1L, "김테스트", "01011112222", "test2@naver.com"));
    }

    @DisplayName("중복된 메일이 입력되면 에러를 발생시켜야한다.")
    @Test
    public void test_duplicateEmail() throws Exception {
        // Given
        MemberRequestDto duplicateEmailUser = new MemberRequestDto(2L, "이테스트", "01011112225", "test2@naver.com");

        // Then
        Assertions.assertThrows(ValidCustomException.class, () -> memberService.save(duplicateEmailUser));
    }

    @DisplayName("serviceLevel 에서  MemberResponseDto valid test - 잘못된 이메일 형식/ 비어있는 이메일 //save or find")
    @Test
    public void test_memberResponseDto_email() throws Exception {
        String testEmail = "test125@naver.com";
        String wrongEmail = testEmail.replace("@", "");
        memberService.save(new MemberRequestDto(1L, "이테스트", "01011112222", "hello@firstEmail.com"));

        Assertions.assertThrows(ConstraintViolationException.class, () -> memberService.save(new MemberRequestDto(1L, "이테스트", "01011112222", null)));
        Assertions.assertThrows(ConstraintViolationException.class, () -> memberService.save(new MemberRequestDto(0L, "이테스트", "01011112222", wrongEmail)));
        Assertions.assertThrows(ValidCustomException.class, () -> memberService.save(new MemberRequestDto(1L, "테스트리", "01330205032", "hello@firstEmail.com")));
        Assertions.assertThrows(IllegalArgumentException.class, () -> memberService.findByEmail(wrongEmail).get());
        Assertions.assertThrows(ConstraintViolationException.class, ()-> memberService.findByEmail(null).get());
    }

    @DisplayName("serviceLevel 에서  MemberResponseDto valid test - 잘못된 전화번호/ 비어있는 전화번호")
    @Test
    public void test_memberResponseDto_phone() throws Exception {

        String testPhoneNum = "01011112222";
        String shortLengthPhoneNum = (String) testPhoneNum.subSequence(0, 9);
        String longLengthPhoneNum = testPhoneNum + testPhoneNum;

        Assertions.assertThrows(ConstraintViolationException.class, () -> memberService.save(new MemberRequestDto(1L, "이테스트", null, "test125@naver.com")));
        Assertions.assertThrows(ConstraintViolationException.class, () -> memberService.save(new MemberRequestDto(0L, "이테스트", longLengthPhoneNum, "test125@naver.com")));
        Assertions.assertThrows(ConstraintViolationException.class, () -> memberService.save(new MemberRequestDto(0L, "이테스트", shortLengthPhoneNum, "test125@naver.com")));

        Assertions.assertThrows(IllegalArgumentException.class, () -> memberService.findByPhone(longLengthPhoneNum).get());
        Assertions.assertThrows(IllegalArgumentException.class, () -> memberService.findByPhone(shortLengthPhoneNum).get());
        Assertions.assertThrows(ConstraintViolationException.class, ()-> memberService.findByPhone(null).get());
    }

    @DisplayName("serviceLevel 에서  MemberResponseDto valid test - 잘못된 이름/ 비어있는 이름")
    @Test
    public void test_memberResponseDto_name() throws Exception {

        memberService.save(new MemberRequestDto(3L, "test이테스트test", "01011112222", "test125@naver.com"));

        Assertions.assertThrows(ConstraintViolationException.class, () -> memberService.save(new MemberRequestDto(1L, "", "01011112222", "test125@naver.com")));
        Assertions.assertThrows(ConstraintViolationException.class, () -> memberService.save(new MemberRequestDto(2L, null, "01011112222", "test125@naver.com")));

        Assertions.assertThrows(IllegalArgumentException.class, () -> memberService.findByPhone("test이테스트test").get());
        Assertions.assertThrows(ConstraintViolationException.class, () -> memberService.findByPhone("").get());
        Assertions.assertThrows(IllegalArgumentException.class, () -> memberService.findByPhone(" ").get());
        Assertions.assertThrows(ConstraintViolationException.class, ()-> memberService.findByPhone(null).get());
    }

}