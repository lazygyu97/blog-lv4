package com.example.bloglv4.repository;

import com.example.bloglv4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<com.example.bloglv4.entity.User,Long> {
    //Optional 클래스의 이해
    // "null 일수도 있는 객체" 를 감싸는 Wrapper 클래스.
    // null 일수도 있는 객체를 다루기 용이하다.

    // 많이 쓰이는 Optional 메서드
    // isPresent(): null인 경우 false를, 값이 담긴 경우 true를 반환한다.
    // get() : 내부에 담긴 객체를 반환한다. 만약 null이면 NoSuchElementException 이 발생하기 때문에 .isPresent()fh 체크한다.
    // orElseThrow() : null이 아니라면 객체를 반환하고 null이라면 인자로 넘겨준 함수형 인자를 통해 생성된 예외를 발생시킨다.
    // orElse() : 내부에 담긴 객체가 null이 아니라면 객체를 반환하고 , null 이라면 인자로 넘겨준 객체를 반환
    Optional<User> findByUsername(String username);
}
