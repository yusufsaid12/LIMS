package com.yusuf.lims.repository;

import com.yusuf.lims.entity.Book;
import com.yusuf.lims.entity.BookRent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentRepository extends JpaRepository<BookRent, Long> {
    BookRent findByBook(Book book);
}
