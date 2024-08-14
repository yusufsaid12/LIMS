package com.yusuf.lims.dto;

import jakarta.validation.constraints.NotEmpty;

public class CategoryDto {

    private Long id;

    @NotEmpty
    private String name;
}
