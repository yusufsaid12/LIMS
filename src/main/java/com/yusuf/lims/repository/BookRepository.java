package com.yusuf.lims.repository;

import com.yusuf.lims.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, String> {

    Book findByName(String name);

}


