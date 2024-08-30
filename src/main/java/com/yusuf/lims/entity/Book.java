package com.yusuf.lims.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private String writer_name;

    @Column(nullable=false)
    private String category_name;

    @Column(nullable=false)
    private int page_number;

    @Column(nullable=false)
    private boolean rent_status = false;

    @Lob
    @Column(name = "book_pictures")
    private byte[] book_pictures;
}
