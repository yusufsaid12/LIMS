package com.yusuf.lims.service;

import com.yusuf.lims.dto.BookDto;

import java.util.List;

public interface BookService {
    void saveBook(BookDto bookDto);
    List<BookDto> findAllBooks();
    BookDto findBookByName(String name);
    void deleteBookById(Long id);
}
