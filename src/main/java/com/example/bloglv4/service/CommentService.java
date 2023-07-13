package com.example.bloglv4.service;


import com.example.bloglv4.dto.CommentRequestDto;
import com.example.bloglv4.dto.CommentResponseDto;
import com.example.bloglv4.entity.Comment;
import com.example.bloglv4.entity.Post;
import com.example.bloglv4.entity.User;
import com.example.bloglv4.entity.UserRoleEnum;
import com.example.bloglv4.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.RejectedExecutionException;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;

    //1.댓글 작성 기능
    public CommentResponseDto createComment(CommentRequestDto requestDto, User user) {
        Post post = postService.findPost(requestDto.getPostId());

        Comment comment = new Comment(requestDto.getBody());
        comment.setUser(user);
        comment.setPost(post);

        var savedComment = commentRepository.save(comment);

        return new CommentResponseDto(savedComment);
    }

    // 2. 댓글 수정 기능
    @Transactional
    public CommentResponseDto updateComment(Long id, CommentRequestDto requestDto, User user) {
        Comment comment = findComment(id);

        // 요청자가 운영자 이거나 댓글 작성자(post.user) 와 요청자(user) 가 같은지 체크
        if (!user.getRole().equals(UserRoleEnum.ADMIN) && !comment.getUser().equals(user)) {
            throw new RejectedExecutionException();
        }

        comment.setBody(requestDto.getBody());

        return new CommentResponseDto(comment);
    }

    // 3. 댓글 삭제 기능
    public void deleteComment(Long id, User user) {
        Comment comment = findComment(id);

        // 요청자가 운영자 이거나 댓글 작성자(post.user) 와 요청자(user) 가 같은지 체크
        if (!user.getRole().equals(UserRoleEnum.ADMIN) && !comment.getUser().equals(user)) {
            throw new RejectedExecutionException();
        }

        commentRepository.delete(comment);
    }

    public Comment findComment(Long id){
        return commentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.")
        );
    }

}
