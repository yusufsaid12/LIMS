package com.yusuf.lims.service;

import com.yusuf.lims.dto.BookDto;
import com.yusuf.lims.dto.UserDto;
import com.yusuf.lims.entity.Book;
import com.yusuf.lims.entity.BookRent;
import com.yusuf.lims.entity.User;
import com.yusuf.lims.repository.BookRepository;
import com.yusuf.lims.repository.RentRepository;
import com.yusuf.lims.repository.UserRepository;
import com.yusuf.lims.repository.WriterRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final WriterRepository writerRepository;
    private final RentRepository rentRepository;

    public BookServiceImpl(BookRepository bookRepository, WriterRepository writerRepository, UserRepository userRepository, RentRepository rentRepository) {
        this.rentRepository = rentRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.writerRepository = writerRepository;
    }

    @Override
    public void saveBook(BookDto bookDto) throws IOException {
        Book book = new Book();
        book.setName(bookDto.getName());
        book.setWriter(bookDto.getWriter());
        book.setCategory(bookDto.getCategory());
        book.setPageNumber(bookDto.getPageNumber());
        book.setRentStatus(bookDto.isRentStatus());

        bookRepository.save(book);
    }

    @Override
    public List<BookDto> findAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(book -> {
                    BookDto bookDto = mapToBookDto(book);
                    return bookDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Book findBookByName(String book) {
        return bookRepository.findByName(book);
    }

    @Override
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public void rentBook(UserDto userDto) {
        BookRent bookRent = new BookRent();
        User user = new User();
        user.setId(userDto.getId());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.getUserBooks().add(userDto.getUserBooks().get(0));
        user.setPassword(userDto.getPassword());
        Date currentDate = new Date();
        // Calendar örneği oluştur ve mevcut tarihi ayarla
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        // Tarihe 1 ay ekle
        calendar.add(Calendar.MONTH, 1);

        // Güncellenmiş tarihi al
        Date nextMonthDate = calendar.getTime();
        bookRent.setUser(user);
        bookRent.setBook(user.getUserBooks().get(0));
        bookRent.setRentDate(currentDate);
        bookRent.setReturnDate(nextMonthDate);

    }

    public void returnBook(String name, UserDto userDto){
        Book book = findBookByName(name);
        book.setRentStatus(false);
        User user = userRepository.findByEmail(userDto.getEmail());
        user.getUserBooks().remove(book);
        BookRent bookRent = rentRepository.findByBook(book);
        Date currentDate = new Date();
        bookRepository.save(book);
        rentRepository.save(bookRent);
        userRepository.save(user);
    }

    public BookDto mapToBookDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setName(book.getName());
        bookDto.setWriter(book.getWriter());
        bookDto.setCategory(book.getCategory());
        bookDto.setPageNumber(book.getPageNumber());
        bookDto.setRentStatus(book.isRentStatus());
        return bookDto;
    }
}