package com.smartnews.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "sentiment")
public class Sentiment {
    @Id
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "type")
    private String type;

    @OneToOne(mappedBy = "sentiment")
    private News news;



}