package com.smartnews.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "news")
public class News {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "headline")
    private String headline;

    @Column(name = "article_url")
    private String articleUrl;

    @Column(name = "source")
    private String source;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "published_at",insertable = false,updatable = false)
    private Instant publishedAt;

    @Column(name = "thumbnail")
    private String thumbnail;

    @ManyToOne(fetch = FetchType.LAZY,optional = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY,optional = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "sentiment_id")
    private Sentiment sentiment;

}