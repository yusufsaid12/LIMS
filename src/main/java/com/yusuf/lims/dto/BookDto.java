package com.yusuf.lims.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

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
    private String category;

    private int pageNumber;

    private boolean rentStatus;
}
