package com.module.validation.domain.member;

import com.module.validation.domain.exception.ErrorResponse;
import com.module.validation.domain.exception.ValidCustomException;
import com.module.validation.domain.member.dto.MemberRequestDto;
import com.module.validation.domain.member.dto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("잘못된 ID입니다."));
    }

    @Transactional(readOnly = true)
    public List<MemberResponseDto> findAll() {
        return memberRepository.findAll()
                .stream()
                .map(MemberResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<Object> findByEmail(String email) throws Exception {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        MemberRequestDto requestDto = new MemberRequestDto(0L, null, "01011112222", "test1naver.com");
        Member member = requestDto.toEntity();
        MemberResponseDto memberResponseDto = new MemberResponseDto(member);
        Set<ConstraintViolation<MemberResponseDto>> violations = validator.validate(memberResponseDto);
        return (violations.size() <= 0) ? Optional.of(memberResponseDto) : Optional.of(buildResponseError(violations)) ;
    }

    public ErrorResponse buildResponseError(Set<ConstraintViolation<MemberResponseDto>> violations) {
        List<String> errorMessage = new ArrayList<>();
        violations.forEach(violation -> errorMessage.add(violation.getMessage()));
        return ErrorResponse.of(errorMessage.toString(), 400);
    }
}
