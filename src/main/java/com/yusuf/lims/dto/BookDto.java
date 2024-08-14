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

    private String page_number;

    private byte[] book_pictures;
}
