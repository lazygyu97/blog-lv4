package com.example.bloglv4.controller;

import com.example.bloglv4.dto.ApiResponseDto;
import com.example.bloglv4.dto.CommentRequestDto;
import com.example.bloglv4.dto.CommentResponseDto;
import com.example.bloglv4.security.UserDetailsImpl;
import com.example.bloglv4.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.RejectedExecutionException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;

    //1.댓글 작성 기능
    @PostMapping("/comments")
    public ResponseEntity<CommentResponseDto> createComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CommentRequestDto requestDto) {
        log.info(userDetails.getUsername());
        CommentResponseDto result = commentService.createComment(requestDto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    //2.댓글 수정 기능
    @PutMapping("/comments/{id}")
    public ResponseEntity<ApiResponseDto> updateComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id, @RequestBody CommentRequestDto requestDto) {
        try {
            CommentResponseDto result = commentService.updateComment(id, requestDto, userDetails.getUser());
            return ResponseEntity.ok().body(result);
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("작성자만 수정 할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }

    //3.댓글 삭제 기능
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<ApiResponseDto> deleteComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        try {
            commentService.deleteComment(id, userDetails.getUser());
            return ResponseEntity.ok().body(new ApiResponseDto("댓글 삭제 성공", HttpStatus.OK.value()));
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("작성자만 삭제 할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }

}
