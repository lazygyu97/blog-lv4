package com.example.bloglv4.controller;

import com.example.bloglv4.dto.ApiResponseDto;
import com.example.bloglv4.dto.PostListResponseDto;
import com.example.bloglv4.dto.PostRequestDto;
import com.example.bloglv4.dto.PostResponseDto;
import com.example.bloglv4.security.UserDetailsImpl;
import com.example.bloglv4.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PostController {

    private PostService postService;

    //RequiredArgsConstructor 어노테이션이 제대로 작동하지 않아서 생성자 주입
    public PostController(PostService postService) {
        this.postService = postService;
    }

    //1. 글 작성 기능
    @PostMapping("/post")
    public ResponseEntity<ApiResponseDto> createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        try {
            PostResponseDto responseDto = postService.createPost(requestDto, userDetails.getUser());
            return ResponseEntity.status(201).body(new ApiResponseDto("글쓰기 성공!! 작성자 :" + responseDto.getUsername() + " 글 제목 : " + responseDto.getTitle() + " 글 내용 : " + responseDto.getContent(), HttpStatus.CREATED.value()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("글 쓰기 실패", HttpStatus.BAD_REQUEST.value()));
        }

    }

    //2. 글 전체 조회 기능
    @GetMapping("/posts")
    public ResponseEntity<PostListResponseDto> getPosts() {

        PostListResponseDto result = postService.getPosts();

        return ResponseEntity.ok().body(result);
    }

    //3. 글 번호를 통한 조회 기능
    @GetMapping("/posts/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {

        PostResponseDto responseDto = postService.getPostById(id);

        return ResponseEntity.ok().body(responseDto);
    }

    //4. 키워드를 통한 제목 기반 게시글 조회 기능
    @GetMapping("/posts/search/{keyword}")
    public ResponseEntity<PostListResponseDto> getPostByKeyword(@PathVariable String keyword) {

        PostListResponseDto responseDto = postService.getPostByKeyword(keyword);

        return ResponseEntity.ok().body(responseDto);
    }

    //5. 글 번호를 통한 삭제 기능
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<ApiResponseDto> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        try {
            postService.deletePost(id, userDetails.getUser());
            return ResponseEntity.status(201).body(new ApiResponseDto("글 삭제 성공!!", HttpStatus.CREATED.value()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("글 삭제 실패 (작성자만 삭제 할 수 있습니다.)", HttpStatus.BAD_REQUEST.value()));
        }
    }

    //6. 글 번호를 통한 수정 기능
    @PutMapping("/posts/{id}")
    public PostResponseDto updatePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails ,@RequestBody PostRequestDto requestDto) {

        try {
            return postService.updatePost(requestDto,id, userDetails.getUser());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(" 글 수정 실패 !!");
        }
    }


}
