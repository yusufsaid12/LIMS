package com.yusuf.lims.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String writer;
    @NotEmpty
    private String category_name;

    private int page_number;

    private byte[] book_pictures;

    public BookDto(String name, String writer, int pageNumber, String category_name, byte[] book_pictures ) {
        this.name = name;
        this.writer = writer;
        this.page_number = pageNumber;
        this.category_name = category_name;
        this.book_pictures = book_pictures;
    }
}
