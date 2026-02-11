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
@Table(name = "category")
public class Category {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "name")
    private String name;

    @OneToOne(mappedBy = "category")
    private News news;

    @OneToMany(mappedBy = "category")
    private Set<PreferenceContent> preferenceContents = new HashSet<>();

}