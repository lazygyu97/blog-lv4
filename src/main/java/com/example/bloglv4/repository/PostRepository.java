package com.example.bloglv4.repository;

import com.example.bloglv4.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {

    //제목에 키워드가 포함되어 있으면 조회
    List<Post> findAllByTitleContains(String keyword);

    //글 전체 조회를 작성시간 기준 내림차순
    List<Post> findAllByOrderByCreatedAtDesc();
}
