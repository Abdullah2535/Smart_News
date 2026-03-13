package com.smartnews.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sentiment")
public class Sentiment {
    @Id
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "type")
    private String type;

    @OneToMany(mappedBy = "sentiment",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<News> newsSet = new HashSet<>();
}