package com.yusuf.lims.service;

import com.yusuf.lims.dto.CategoryDto;
import com.yusuf.lims.entity.Category;

public interface CategoryService {

    void saveCategory(CategoryDto categoryDto);

    Category findByCategoryName(String name);
}
