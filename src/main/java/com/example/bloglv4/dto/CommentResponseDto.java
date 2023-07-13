package com.example.bloglv4.dto;

import com.example.bloglv4.entity.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponseDto extends ApiResponseDto {
    private String body;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Integer likeCount;//댓글에 좋아요 갯수


    public CommentResponseDto(Comment comment) {
        super();//왜 사용하는지 생각해보기
        this.body = comment.getBody();
        this.username = comment.getUser().getUsername();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.likeCount = comment.getCommentLikes().size();//배열로 넘어오는 좋아요 목록의 사이즈를 담아준다.

    }
}