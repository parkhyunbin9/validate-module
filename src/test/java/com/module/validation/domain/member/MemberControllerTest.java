package com.module.validation.domain.member;

import com.module.validation.domain.member.dto.MemberRequestDto;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

}