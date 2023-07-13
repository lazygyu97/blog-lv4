package com.example.bloglv4.repository;

import com.example.bloglv4.entity.Comment;
import com.example.bloglv4.entity.CommentLike;
import com.example.bloglv4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    //선택한 댓글에 로그인한 유저가 좋아요를 이미 했는지 확인
    Optional<CommentLike> findByUserAndComment(User user, Comment comment);
}
