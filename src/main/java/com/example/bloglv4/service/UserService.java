package com.example.bloglv4.service;

import com.example.bloglv4.dto.AuthRequestDto;
import com.example.bloglv4.entity.User;
import com.example.bloglv4.entity.UserRoleEnum;
import com.example.bloglv4.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    // 1.회원가입 기능
    public void signUp(AuthRequestDto authRequestDto) {

        //사용자에게 입력받은 값을 변수 선언
        String username = authRequestDto.getUsername();
        // 비밀번호는 passwordEncoder 통해 암호화
        String password = passwordEncoder.encode(authRequestDto.getPassword());
        // role을 입력 받을때 admin에 대한 추가인증은 없기때문에 dto 값을 그대로 가져온다.
        UserRoleEnum role = authRequestDto.getRole();

        //아이디 중복검사 (User Entity에서 아이디 값은 unique 옵션이 설정됨)
        //isPresent() --> null인 경우 false를, 값이 담긴 경우 true를 반환한다.
        if (userRepository.findByUsername(username).isPresent()) {
            //이미 같은 아이디를 사용한 유저가 있는 상황 --> 중복 오류
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        //위의 중복검사에서 통과되어 레파지토리를 통해 회원을 DB에 저장
        User user = new User(username, password, role);
        userRepository.save(user);

    }

    // 2. 로그인 기능
    public void login(AuthRequestDto authRequestDto) {

        //사용자에게 입력받은 값을 변수 선언
        String username = authRequestDto.getUsername();
        String password = authRequestDto.getPassword();

        //DB에 입력받은 정보의 회원이 있는지 확인하고 있다면 user에 해당 회원의 정보를 담아준다.
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("등록된 사용자가 없습니다."));

        //비밀번호 확인 : passwordEncoder에서 제공하는 matches 함수를 이용해 비교
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

    }


}
