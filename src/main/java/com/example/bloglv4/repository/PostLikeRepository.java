package com.example.bloglv4.repository;

import com.example.bloglv4.entity.Post;
import com.example.bloglv4.entity.PostLike;
import com.example.bloglv4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByUserAndPost(User user, Post post);

}