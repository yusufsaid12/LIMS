package com.yusuf.lims.service;

import com.yusuf.lims.dto.BookDto;
import com.yusuf.lims.entity.Book;
import com.yusuf.lims.repository.BookRepository;
import com.yusuf.lims.repository.WriterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final WriterRepository writerRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, WriterRepository writerRepository) {
        this.bookRepository = bookRepository;
        this.writerRepository = writerRepository;
    }

    @Override
    public void saveBook(BookDto bookDto) {
        Book book = new Book();
        book.setName(bookDto.getName());
        book.setPage_number(bookDto.getPage_number());
        book.setWriter_name(bookDto.getWriter());
        book.setCategory_name(bookDto.getCategory_name());

        bookRepository.save(book);
    }

    @Override
    public List<BookDto> findAllBooks() {
        return bookRepository.findAll().stream()
                .map(book -> new BookDto(book.getName(), book.getWriter_name(), book.getPage_number(), book.getCategory_name(), book.getBook_pictures()))
                .collect(Collectors.toList());
    }

    @Override
    public BookDto findBookByName(String name) {
        Book book = bookRepository.findByName(name);
        if (book != null) {
            return new BookDto(book.getName(), book.getWriter_name(), book.getPage_number(), book.getCategory_name(), book.getBook_pictures());
        }
        return null;
    }

    @Override
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }
}