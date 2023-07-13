package com.example.bloglv4.service;


import com.example.bloglv4.dto.CommentRequestDto;
import com.example.bloglv4.dto.CommentResponseDto;
import com.example.bloglv4.entity.*;
import com.example.bloglv4.repository.CommentLikeRepository;
import com.example.bloglv4.repository.CommentRepository;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.RejectedExecutionException;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final CommentLikeRepository commentLikeRepository;

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

        if (!user.getRole().equals(UserRoleEnum.ADMIN) && !comment.getUser().equals(user)) {
            throw new RejectedExecutionException();
        }

        comment.setBody(requestDto.getBody());

        return new CommentResponseDto(comment);
    }

    // 3. 댓글 삭제 기능
    public void deleteComment(Long id, User user) {
        Comment comment = findComment(id);

        if (!user.getRole().equals(UserRoleEnum.ADMIN) && !comment.getUser().equals(user)) {
            throw new RejectedExecutionException();
        }

        commentRepository.delete(comment);
    }

    //4. 댓글 좋아요 기능
    @Transactional
    public void likeComment(Long id, User user) {
        Comment comment = findComment(id);
        Optional<CommentLike> commentLikeOptional = checkLike(user, comment);

        if (commentLikeOptional.isPresent()) {
            throw new DuplicateRequestException("이미 좋아요 한 댓글 입니다.");
        } else {
            CommentLike commentLike = new CommentLike(user, comment);
            commentLikeRepository.save(commentLike);
        }
    }

    //5. 댓글 좋아요 취소 기능
    @Transactional
    public void deleteLikeComment(Long id, User user) {
        Comment comment = findComment(id);
        Optional<CommentLike> commentLikeOptional = checkLike(user, comment);
        if (commentLikeOptional.isPresent()) {
            commentLikeRepository.delete(commentLikeOptional.get());
        } else {
            throw new IllegalArgumentException("아직 좋아요를 누르지 않은 댓글입니다.");
        }
    }

    //댓글이 있는지 확인하는 메서드
    public Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 댓글은 존재하지 않습니다.")
        );
    }

    //댓글에 좋아요 확인 메서드
    public Optional<CommentLike> checkLike(User user, Comment comment) {
        return commentLikeRepository.findByUserAndComment(user, comment);
    }
}
