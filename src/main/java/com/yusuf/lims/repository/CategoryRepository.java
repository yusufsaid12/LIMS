package com.yusuf.lims.repository;

import com.yusuf.lims.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {

    Category findByName(String name);

}
