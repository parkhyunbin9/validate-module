package com.module.validation.domain.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.module.validation.domain.member.dto.MemberRequestDto;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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

    @DisplayName("post 테스트 - empty name")
    @Test
    public void save() throws Exception {
        // Given
        String postUrl = "/api/members";
        String nullUserEmail = "blank@google.com";
        String blankUserEmail = "null@yahoo.com";
        String blankNameUser = objectMapper.writeValueAsString(new MemberRequestDto(3L, "", "01012341234", "blank@google.com"));
        String nullNameUser = objectMapper.writeValueAsString(new MemberRequestDto(4L, null, "01012341234", "null@yahoo.com"));

        // When
        testPostRequest(postUrl, blankNameUser);
        testPostRequest(postUrl, nullNameUser);

        // Then
        Assertions.assertFalse(memberService.findAll().stream().anyMatch(e -> e.getEmail().equals(blankUserEmail)));
        Assertions.assertFalse(memberService.findAll().stream().anyMatch(e -> e.getEmail().equals(nullUserEmail)));
    }

    public void testPostRequest(String url, String bodyStr) throws Exception {
        mock.perform(MockMvcRequestBuilders.post(url)
                .content(bodyStr)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}