package com.inha.os.econtentbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Content extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private ContentType contentType;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false) // Enforces a foreign key constraint
    @OnDelete(action = OnDeleteAction.CASCADE) //
    private Subject subject;
    @Lob
    @Column(name = "file_data")
    private byte[] data;
    private String format;
}
