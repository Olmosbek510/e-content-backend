package com.inha.os.econtentbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "books",
        uniqueConstraints = @UniqueConstraint(columnNames = {"author", "title"}))
public class Book extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String author;
    private Integer page;
    @OneToOne(cascade = CascadeType.ALL)
    private Photo photo;
    @OneToOne(cascade = CascadeType.ALL)
    private Content content;
}
