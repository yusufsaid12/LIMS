package com.yusuf.lims.service;

import com.yusuf.lims.dto.BookDto;
import com.yusuf.lims.dto.UserDto;
import com.yusuf.lims.entity.Book;

import java.io.IOException;
import java.util.List;

public interface BookService {
    void saveBook(BookDto bookDto) throws IOException;
    List<BookDto> findAllBooks();
    Book findBookByName(String name);
    void deleteBookById(Long id);
    void rentBook(UserDto userDto);
    void returnBook(String name, UserDto userDto);
}
