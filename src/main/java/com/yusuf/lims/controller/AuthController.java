package com.yusuf.lims.controller;

import com.yusuf.lims.dto.BookDto;
import com.yusuf.lims.dto.UserDto;
import com.yusuf.lims.entity.Book;
import com.yusuf.lims.entity.User;
import com.yusuf.lims.repository.BookRepository;
import com.yusuf.lims.service.BookService;
import com.yusuf.lims.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AuthController {

    private BookRepository bookRepository;
    private UserService userService;
    private BookService bookService;

    public AuthController(UserService userService, BookService bookService, BookRepository bookRepository) {
        this.userService = userService;
        this.bookService = bookService;
        this.bookRepository = bookRepository;
    }

    // handler method to handle home page request
    @GetMapping("/index")
    public String home(){
        return "index";
    }

    // handler method to handle login request
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/bookInformation")
    public String bookInformation(){
        return "bookInformation";
    }

    // handler method to handle user registration form request
    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        // create model object to store form data
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    // handler method to handle user registration form submit request
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result,
                               Model model){
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            result.rejectValue("email", null,
                    "Aynı e-postayla kayıtlı bir hesap zaten var");
        }

        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            return "/register";
        }

        userService.saveUser(userDto);
        return "redirect:/register?success";
    }

    @PostMapping("/books/save")
    public String saveBook(@Valid @ModelAttribute("book") BookDto bookDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("book", bookDto);
            return "books";
        }

        bookService.saveBook(bookDto);  // Kitap kaydetme işlemi
        return "redirect:/bookIformation?success";
    }

    @GetMapping("/bookForm")
    public String showBookForm(Model model) {
        model.addAttribute("book", new BookDto());
        return "bookForm";
    }

    @GetMapping("/books")
    public String books(Model model){
        List<BookDto> books = bookService.findAllBooks();
        model.addAttribute("books", books);
        model.addAttribute("book", new BookDto());  // Yeni kitap eklemek için form
        return "books";
    }

    // handler method to handle list of users
    @GetMapping("/users")
    public String users(Model model){
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    public BookDto findBookByName(String name) {
        Book book = bookRepository.findByName(name);
        if (book != null) {
            return new BookDto(book.getName(), book.getWriter_name(), book.getPage_number(), book.getCategory_name(), book.getBook_pictures());
        }
        return null;
    }

    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }
}