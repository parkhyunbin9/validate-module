package com.module.validation.domain.member.dto;

import com.module.validation.domain.member.Member;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class MemberRequestDto {

    private Long id;

    @NotBlank(message = "이름을 입력해 주세요.")
    private String name;

    @NotBlank(message = "전화번호를 입력해 주세요")
    @Pattern(regexp = "[0-9]{10,11}", message = "전화번호는 10~11자리 숫자만 가능합니다.")
    private String phoneNum;

    @NotBlank(message = "메일을 입력해 주세요.")
    @Email(message = "메일의 포맷을 확인해 주세요.")
    private String email;

    public Member toEntity() {
        String[] phone = buildPhoneNumber();
        return Member.builder()
                .name(name)
                .phone1(phone[0])
                .phone2(phone[1])
                .phone3(phone[2])
                .email(email)
                .build();
    }

    public String[] buildPhoneNumber() {
        String[] phones = new String[3];
        int mid = phoneNum.length() == 10 ? 7 : 8;
        phones[0] = phoneNum.substring(0, 3);
        phones[1] = phoneNum.substring(4, mid);
        phones[2] = phoneNum.substring(mid, phoneNum.length()-1);
        return phones;
    }


}
