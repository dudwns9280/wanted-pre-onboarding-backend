package com.example.dashboard.post.entity;

import com.example.dashboard.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public void updatePost(String title, String content){
        if(title != null){
            this.title = title;
        }
        if(content != null){
            this.content = content;
        }
    }
}