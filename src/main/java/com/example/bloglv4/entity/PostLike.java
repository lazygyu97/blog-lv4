package com.example.bloglv4.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "post_like")
public class PostLike extends TimeStamped {

    //게시글의 좋아요 기능

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //fetch 옵션 :연관된 엔티티를 어떻게 가져올지 설정하는 데 사용된다. FetchType.LAZY는 지연 로딩을 의미.
    //지연 로딩은 연관된 엔티티를 실제로 사용할 때까지 데이터베이스에서 가져오지 않고, 해당 엔티티에 대한 프록시 객체를 생성하여 대신 사용한다(?)
    // 따라서 실제로 연관된 엔티티를 사용할 때까지 데이터베이스에서 가져오지 않아도 되기 때문에 성능 개선에 도움이 된다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public PostLike(User user, Post post) {
        this.user = user;
        this.post = post;
    }
}