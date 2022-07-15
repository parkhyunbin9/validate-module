package com.module.validation.domain.member.dto;

import com.module.validation.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class MemberResponseDto {

    private Long id;

    @NotEmpty(message = "이름은 비어있을 수 없습니다.")
    private String name;

    @NotEmpty(message = "전화번호는 비어있을 수 없습니다.")
    private String phoneNum;

    @Email(message =  "올바르지 않은 이메일형식입니다.: ${validatedValue}", regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})$") // .com에서 . 이 없어도 VALID PASS하므로 regexp 추가
    private String email;

    public MemberResponseDto(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.phoneNum = buildPhoneNumStr(member.getPhone1(), member.getPhone2(), member.getPhone3());
        this.email = member.getEmail();
    }

    String buildPhoneNumStr(String phone1, String phone2, String phone3) {
        return phone1 + phone2 + phone3;
    }
}
