package com.example.bloglv4.dto;

import com.example.bloglv4.entity.UserRoleEnum;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequestDto {
    @Pattern(regexp = "^[a-z0-9]{4,10}$",
            message = "최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9) 로 구성되어야 합니다.")
    private String username;

    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+{}:\"<>?,.\\\\/]{8,15}$",
            message = "최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자 로 구성되어야 합니다.")
    private String password;

//    //Pattern 어노테이션은 String 값에 대해서만 유효성 검사를 할 수 있다.
//    @Pattern(regexp = "^(ADMIN|USER)$", message = "올바른 권한 값을 입력해주세요.")
//    private String role; // 회원 권한 (ADMIN, USER)
    private UserRoleEnum role; // 회원 권한 (ADMIN, USER)

}