package com.module.validation.domain.member;

import com.module.validation.domain.exception.ErrorResponse;
import com.module.validation.domain.exception.ValidCustomException;
import com.module.validation.domain.member.dto.MemberRequestDto;
import com.module.validation.domain.member.dto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Validated
public class MemberService {

    private final MemberRepository memberRepository;

    public Long save(@Valid MemberRequestDto memberRequestDto) {
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
    public Optional<Object> findByPhone(@NotEmpty String phone) throws Exception {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        String phoneNum1 = "";
        String phoneNum2 = "";
        String phoneNum3 = "";

        if (phone.length() >= 9) {
            int mid = phone.length() == 10 ? 7 : 8;
            phoneNum1 = phone.substring(0, 3);
            phoneNum2 = phone.substring(4, mid);
            phoneNum3 = phone.substring(mid, phone.length()-1);
        }

        Member member = memberRepository.findByPhone1AndPhone2AndPhone3(phoneNum1, phoneNum2, phoneNum3).orElseThrow(() -> new IllegalArgumentException("wrong PhoneNumber"));
        MemberResponseDto memberResponseDto = new MemberResponseDto(member);

        Set<ConstraintViolation<MemberResponseDto>> violations = validator.validate(memberResponseDto);
        return (violations.size() <= 0) ? Optional.of(memberResponseDto) : Optional.of(buildResponseError(violations)) ;
    }

    @Transactional(readOnly = true)
    public Optional<Object> findByEmail(@NotEmpty String email) throws Exception {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("wrong Email"));
        MemberResponseDto memberResponseDto = new MemberResponseDto(member);
        Set<ConstraintViolation<MemberResponseDto>> violations = validator.validate(memberResponseDto);
        return (violations.size() <= 0) ? Optional.of(memberResponseDto) : Optional.of(buildResponseError(violations));
    }

    public ErrorResponse buildResponseError(Set<ConstraintViolation<MemberResponseDto>> violations) {
        List<String> errorMessage = new ArrayList<>();
        violations.forEach(violation -> errorMessage.add(violation.getMessage()));
        return ErrorResponse.of(errorMessage.toString(), 400);
    }
}
