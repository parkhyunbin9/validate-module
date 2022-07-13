package com.module.validation.domain.member;

import com.module.validation.domain.exception.ValidCustomException;
import com.module.validation.domain.member.dto.MemberRequestDto;
import com.module.validation.domain.member.dto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Long save(MemberRequestDto memberRequestDto) {
        checkDuplicateEmail(memberRequestDto.getEmail());
        return memberRepository.save(memberRequestDto.toEntity()).getId();
    }

    void checkDuplicateEmail(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new ValidCustomException("이미 사용중인 이메일 입니다.", "email");
        }
    }

    @Transactional(readOnly = true)
    public List<MemberResponseDto> findAll() {
        return memberRepository.findAll()
                .stream()
                .map(MemberResponseDto::new)
                .collect(Collectors.toList());
    }

}
