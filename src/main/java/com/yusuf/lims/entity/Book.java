package com.yusuf.lims.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

@Getter
@Setter
@Builder
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

    @Column(nullable=false, name = "writer_name")
    private String writer;

    @Column(nullable=false, name = "category_name")
    private String category;

    @Column(nullable=false, name = "page_number")
    private int pageNumber;


    @Column(nullable=false, columnDefinition = "false", name = "rent_status")
    private boolean rentStatus;
}
