package com.module.validation.domain.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.module.validation.domain.exception.CustomExceptionHandler;
import com.module.validation.domain.member.dto.MemberRequestDto;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.constraints.AssertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // test 클래스당 인스턴스 생성 -> beforeall 로 생성한 인스턴스 재활용 가능
class MemberControllerTest {

    @Autowired
    MockMvc mock;
    @Autowired
    MemberService memberService;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    public void before() {
        memberService.save(new MemberRequestDto(0L, "김확인", "01011112223", "test1@naver.com"));
        memberService.save(new MemberRequestDto(1L, "김테스트", "01011112222", "test2@naver.com"));
        memberService.save(new MemberRequestDto(2L, "이테스트", "01012341234", "test2@google.com"));
    }


    @DisplayName("get 테스트")
    @Test
    public void findAll() throws Exception {
        // Given
        mock.perform(MockMvcRequestBuilders.get("/api/members"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // When

        // Then

    }

    @DisplayName("post 테스트 - empty or null -> name : 유효하지 않는 이름")
    @Test
    public void test_name_save() throws Exception {
        // Given
        String postUrl = "/api/members";
        String blankNameUser = objectMapper.writeValueAsString(new MemberRequestDto(3L, "", "01012341234", "blank@google.com"));
        String nullNameUser = objectMapper.writeValueAsString(new MemberRequestDto(4L, null, "01012341234", "null@yahoo.com"));

        // When
        Exception blankNameException = mock.perform(MockMvcRequestBuilders.post(postUrl)
                .content(blankNameUser)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResolvedException();

        Exception nullNameException = mock.perform(MockMvcRequestBuilders.post(postUrl)
                .content(nullNameUser)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResolvedException();

        // Then
        Assertions.assertEquals(MethodArgumentNotValidException.class, blankNameException.getClass());
        Assertions.assertEquals(MethodArgumentNotValidException.class, nullNameException.getClass());
    }

    @DisplayName("post 테스트 - empty or null or format error -> email : 유효하지 않는 메일")
    @Test
    public void test_email_save() throws Exception {
        // Given
        String postUrl = "/api/members";

        String withoutAtEmail = objectMapper.writeValueAsString(new MemberRequestDto( "test user 1", "01012341234", "blankgoogle.com"));
        String withoutDotUser = objectMapper.writeValueAsString(new MemberRequestDto( "test user 2", "01012341234", "null@yahoocom"));
        String multipleAt = objectMapper.writeValueAsString(    new MemberRequestDto( "test user 3", "01012341234", "n@.com@"));
        String multipleDot = objectMapper.writeValueAsString(   new MemberRequestDto( "test user 4", "01012341234", "n@.com.com"));
        String numberOnly = objectMapper.writeValueAsString(    new MemberRequestDto( "test user 5", "01012341234", "1231231231"));
        String withBlack = objectMapper.writeValueAsString(     new MemberRequestDto( "test user 6", "01012341234", "abc @ google.com"));
        String withNull = objectMapper.writeValueAsString(      new MemberRequestDto( "test user 7", "01012341234", null));
        String withEmpty = objectMapper.writeValueAsString(     new MemberRequestDto( "test user 8", "01012341234", ""));

        // When
        Exception withoutAtEmailException = getExceptionFromPostRequest(postUrl, withoutAtEmail);
        Exception multipleAtEmailException = getExceptionFromPostRequest(postUrl, multipleAt);
        Exception multipleDotEmailException = getExceptionFromPostRequest(postUrl, multipleDot);
        Exception numberOnlyEmailException = getExceptionFromPostRequest(postUrl, numberOnly);
        Exception withBlackEmailException = getExceptionFromPostRequest(postUrl, withBlack);
        Exception withNullEmailException = getExceptionFromPostRequest(postUrl, withNull);
        Exception withEmptyEmailException = getExceptionFromPostRequest(postUrl, withEmpty);
        Exception withoutDotUserEmailException = getExceptionFromPostRequest(postUrl, withoutDotUser);
        // Then
        Assertions.assertEquals(MethodArgumentNotValidException.class, withoutAtEmailException.getClass());
        Assertions.assertEquals(MethodArgumentNotValidException.class, withoutDotUserEmailException.getClass());
        Assertions.assertEquals(MethodArgumentNotValidException.class, multipleAtEmailException.getClass());
        Assertions.assertEquals(MethodArgumentNotValidException.class, multipleDotEmailException.getClass());
        Assertions.assertEquals(MethodArgumentNotValidException.class, numberOnlyEmailException.getClass());
        Assertions.assertEquals(MethodArgumentNotValidException.class, withBlackEmailException.getClass());
        Assertions.assertEquals(MethodArgumentNotValidException.class, withNullEmailException.getClass());
        Assertions.assertEquals(MethodArgumentNotValidException.class, withEmptyEmailException.getClass());
    }

    public Exception getExceptionFromPostRequest(String url, String body) throws Exception {
        return mock.perform(MockMvcRequestBuilders.post(url)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResolvedException();
    }
}