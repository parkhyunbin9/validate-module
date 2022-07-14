package com.module.validation.domain.member;

import com.module.validation.domain.member.dto.MemberRequestDto;
import com.module.validation.domain.member.dto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    @GetMapping("/api/members")
    public List<MemberResponseDto> findAll() {
        return memberService.findAll();
    }

    @PostMapping("/api/members")
    public Long save(@RequestBody @Valid  MemberRequestDto requestDto) {
        return memberService.save(requestDto);
    }

}
