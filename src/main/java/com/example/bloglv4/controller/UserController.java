package com.example.bloglv4.controller;

import com.example.bloglv4.dto.ApiResponseDto;
import com.example.bloglv4.dto.AuthRequestDto;
import com.example.bloglv4.jwt.JwtUtil;
import com.example.bloglv4.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    // 1. 회원가입 기능
    // 메서드의 반환 값인 ResponseEntity의 이해
    // Spring Framework에서 제공하는 클래스 중 HttpEntity라는 클래스가 존재한다.
    // 이것은 HTTP 요청(Request) 또는 응답(Response)에 해당하는 HttpHeader와 HttpBody를 포함하는 클래스이다
    // HttpEntity 클래스를 상속받아 구현한 클래스가 RequestEntity, ResponseEntity 클래스이다.
    // ResponseEntity는 사용자의 HttpRequest에 대한 응답 데이터를 포함하는 클래스이다.
    // 따라서 HttpStatus, HttpHeaders, HttpBody를 포함한다.
    // =====> DTO 의 응답값이 적절히 처리되어 넘어왔는지 확인하여, 상황에 맞게 오류 또는 성공의 상태의 결과값을 만들 수 있다.
    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto> signUp(@Valid @RequestBody AuthRequestDto requestDto, BindingResult bindingResult) {

        // Validation 예외처리 : 아이디와 패스워드가 pattern에 맞게 입력 되었는지 확인.
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        if(fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(new ApiResponseDto("입력하신 정보가 회원가입 요건에 맞지 않습니다.", HttpStatus.BAD_REQUEST.value()));
        }

        // Validation 예외 처리후 서비스로 전달.
        try {
            userService.signUp(requestDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("중복된 username 입니다.", HttpStatus.BAD_REQUEST.value()));
        }

        return ResponseEntity.status(201).body(new ApiResponseDto("회원가입 성공", HttpStatus.CREATED.value()));
    }

    // 2. 로그인 기능
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto> login(@RequestBody AuthRequestDto requestDto, HttpServletResponse response){

        try {
            userService.login(requestDto);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(new ApiResponseDto("로그인 실패 (아이디 또는 패스워드를 확인해주세요)", HttpStatus.BAD_REQUEST.value()));
        }
        //JWT 생성 및 쿠키에 저장 후 Response 객체에 추가
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(requestDto.getUsername(), requestDto.getRole()));

        return ResponseEntity.status(201).body(new ApiResponseDto("로그인 성공!! 토큰 정보 : "+response.getHeader("Authorization"), HttpStatus.CREATED.value()));

    }

    // 3. 로그아웃 기능 : 구현해보고 싶어서 하긴했는데 확인이 필요함.
    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDto> logout(HttpServletRequest request, HttpServletResponse response) {
        // 현재 사용자의 토큰을 가져오기
        String token = jwtUtil.resolveToken(request);
        log.info(token);
        if (token == null) {
            throw new IllegalArgumentException("로그인 상태가 아닙니다.");
        }
        // 토큰을 블랙리스트에 추가하여 만료시키기
        jwtUtil.addTokenToBlacklist(token);
        // 쿠키에서 토큰 제거
        jwtUtil.removeTokenFromCookie(response);

        return ResponseEntity.ok(new ApiResponseDto("로그아웃 되었습니다.", HttpStatus.OK.value()));
    }
}