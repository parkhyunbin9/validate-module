package com.module.validation.domain.member.dto;

import com.module.validation.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberResponseDto {
    private Long id;
    private String name;
    private String phoneNum;
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
