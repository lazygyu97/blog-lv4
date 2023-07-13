package com.example.bloglv4.service;

import com.example.bloglv4.dto.PostListResponseDto;
import com.example.bloglv4.dto.PostRequestDto;
import com.example.bloglv4.dto.PostResponseDto;
import com.example.bloglv4.entity.Post;
import com.example.bloglv4.entity.PostLike;
import com.example.bloglv4.entity.User;
import com.example.bloglv4.entity.UserRoleEnum;
import com.example.bloglv4.repository.PostLikeRepository;
import com.example.bloglv4.repository.PostRepository;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    //1.글 작성 기능
    public PostResponseDto createPost(PostRequestDto requestDto, User user) {
        //입력받은 값의 null 확인
        if (requestDto.getTitle().isEmpty()) {
            throw new IllegalArgumentException("글 제목이 입력되지 않았습니다.");
        }
        if (requestDto.getContent().isEmpty()) {
            throw new IllegalArgumentException("글 내용이 입력되지 않았습니다.");
        }

        Post post = new Post(requestDto);
        post.setUser(user);

        postRepository.save(post);

        return new PostResponseDto(post);
    }

    //2. 글 전체 조회 기능
    public PostListResponseDto getPosts() {

        //받아온 글을 List 형식으로 정리
        List<PostResponseDto> postList = postRepository.findAll().stream().map(PostResponseDto::new).collect(Collectors.toList());

        return new PostListResponseDto(postList);

    }

    //3. 글 번호를 통한 조회 기능
    public PostResponseDto getPostById(Long id) {
        Post post = findPost(id);
        return new PostResponseDto(post);
    }

    //4. 키워드를 통한 제목 기반 게시글 조회 기능
    public PostListResponseDto getPostByKeyword(String keyword) {

        if (postRepository.findAllByTitleContains(keyword).isEmpty()) {
            throw new IllegalArgumentException("키워드가 포함된 글이 없습니다.");
        }

        List<PostResponseDto> postList = postRepository.findAllByTitleContains(keyword).stream().map(PostResponseDto::new).collect(Collectors.toList());

        return new PostListResponseDto(postList);
    }

    //5. 글 번호를 통한 삭제 기능
    public PostResponseDto deletePost(Long id, User user) {
        Post post = findPost(id);
        if (!(user.getRole().equals(UserRoleEnum.ADMIN) || post.getUser().equals(user))) {
            throw new IllegalArgumentException("작성자가 일치하지 않습니다.");
        }
        postRepository.delete(post);
        return new PostResponseDto(post);
    }


    //6. 글 번호를 통한 수정 기능
    @Transactional
    public PostResponseDto updatePost(PostRequestDto requestDto, Long id, User user) {
        Post post = findPost(id);

        if (!(user.getRole().equals(UserRoleEnum.ADMIN) || post.getUser().equals(user))) {
            throw new IllegalArgumentException("작성자가 일치하지 않습니다.");
        }

        post.setTitle(requestDto.getTitle());
        post.setContent(requestDto.getContent());

        return new PostResponseDto(post);
    }

    //7.글 좋아요 기능
    @Transactional
    public void likePost(Long id, User user) {
        Post post = findPost(id);
        Optional<PostLike> postLikeOptional=checkLike(user, post);

        if (postLikeOptional.isPresent()) {
            throw new DuplicateRequestException("이미 좋아요 한 게시글 입니다.");
        } else {
            PostLike postLike = new PostLike(user, post);
            postLikeRepository.save(postLike);
        }
    }

    //글 좋아요 취소 기능
    @Transactional
    public void deleteLikePost(Long id, User user) {
        Post post = findPost(id);
        Optional<PostLike> postLikeOptional=checkLike(user, post);
        if (postLikeOptional.isPresent()) {
            postLikeRepository.delete(postLikeOptional.get());
        } else {
            throw new IllegalArgumentException("해당 게시글에 취소할 좋아요가 없습니다.");
        }
    }

    public Post findPost(long id) {
        return postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("선택한 게시글은 존재하지 않습니다."));
    }

    public Optional<PostLike> checkLike(User user, Post post){
        return postLikeRepository.findByUserAndPost(user, post);
    }

}
