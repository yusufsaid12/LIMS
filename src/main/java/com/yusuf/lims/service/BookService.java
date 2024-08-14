package com.yusuf.lims.service;

import com.yusuf.lims.dto.BookDto;
import com.yusuf.lims.entity.Book;

public interface BookService {

    void saveBook(BookDto bookDto);

    Book findByBookName(String name);
}
