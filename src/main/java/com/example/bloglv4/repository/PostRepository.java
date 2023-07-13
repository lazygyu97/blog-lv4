package com.example.bloglv4.repository;

import com.example.bloglv4.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findAllByTitleContains(String keyword);
}
