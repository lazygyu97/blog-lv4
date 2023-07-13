package com.example.bloglv4.repository;

import com.example.bloglv4.entity.Post;
import com.example.bloglv4.entity.PostLike;
import com.example.bloglv4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    //선택한 글에 로그인한 유저가 좋아요를 이미 했는지 확인
    Optional<PostLike> findByUserAndPost(User user, Post post);

}