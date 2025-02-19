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
    private String description;
    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Content content;
}
