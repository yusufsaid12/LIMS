package com.yusuf.lims.controller;

import com.yusuf.lims.dto.BookDto;
import com.yusuf.lims.dto.UserDto;
import com.yusuf.lims.entity.Book;
import com.yusuf.lims.entity.Role;
import com.yusuf.lims.entity.User;
import com.yusuf.lims.repository.BookRepository;
import com.yusuf.lims.repository.RoleRepository;
import com.yusuf.lims.repository.UserRepository;
import com.yusuf.lims.security.CustomUserDetailsService;
import com.yusuf.lims.service.BookService;
import com.yusuf.lims.service.BookServiceImpl;
import com.yusuf.lims.service.UserService;
import com.yusuf.lims.service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.*;

@Controller
public class AuthController {

    private final BookServiceImpl bookServiceImpl;
    private final RoleRepository roleRepository;
    private BookRepository bookRepository;
    private UserService userService;
    private BookService bookService;
    private UserServiceImpl userServiceImpl;
    private UserRepository userRepository;

    public AuthController(UserService userService, BookService bookService, BookRepository bookRepository, UserRepository userRepository, UserServiceImpl userServiceImpl, RoleRepository roleRepository, BookServiceImpl bookServiceImpl) {
        this.userService = userService;
        this.bookService = bookService;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.userServiceImpl = userServiceImpl;
        this.roleRepository = roleRepository;
        this.bookServiceImpl = bookServiceImpl;
    }

    // handler method to handle home page request
    @GetMapping("/index")
    public ModelAndView index(Authentication authentication) {
        ModelAndView modelAndView = new ModelAndView("index");
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            UserDto user = userServiceImpl.mapToUserDto(userRepository.findByEmail(userDetails.getUsername()));
            String role = authentication.getAuthorities().toString();
            List<BookDto> books = bookService.findAllBooks();
            modelAndView.addObject("books", books);
            modelAndView.addObject("role", role);
            modelAndView.addObject("user", user);
        }
        return modelAndView;
    }

    @PostMapping("/index/rent/{id}")
    public String rentBook(@PathVariable("id") Long bookId, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            UserDto user = userServiceImpl.mapToUserDto(userRepository.findByEmail(userDetails.getUsername()));
            Book book = bookRepository.findById(bookId).get();
            book.setRentStatus(true);
            user.getUserBooks().add(book);
            bookServiceImpl.rentBook(user);
        }
        return "redirect:/index";
    }

    @GetMapping("/profile")
    public String profilePage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        UserDto user = userServiceImpl.mapToUserDto(userRepository.findByEmail(userDetails.getUsername()));
        List<BookDto> book = bookService.findAllBooks();
        // Kullanıcı bilgilerini alıp Model'e ekleyin
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateUser(@ModelAttribute("user") UserDto userDto) {

        userService.updateUser(userDto.getId(), userDto.getPassword(), userDto.getFirstName(), userDto.getLastName());
        return "redirect:/profile"; // veya uygun bir gösterim sayfasına yönlendirme
    }

    @GetMapping("/profile/returnBook/{name}")
    public String returnBook(@PathVariable("name") String name, @AuthenticationPrincipal UserDetails userDetails) {
        UserDto userDto = userServiceImpl.mapToUserDto(userRepository.findByEmail(userDetails.getUsername()));
        bookService.returnBook(name, userDto);
        return "redirect:/users";
    }


    // handler method to handle login request
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // handler method to handle user registration form request
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // create model object to store form data
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    // handler method to handle user registration form submit request
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result,
                               Model model) throws IOException {
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", null,
                    "Aynı e-postayla kayıtlı bir hesap zaten var");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "/register";
        }

        userService.saveUser(userDto);
        return "redirect:/register?success";
    }

    // handler method to handle list of users
    @GetMapping("/users/delete/{id}")
    public String userDelete(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }

    @PostMapping("/users/update-role")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> updateRole(@RequestBody Map<String, String> payload) {
        Long id = Long.parseLong(payload.get("id"));
        String roleName = payload.get("role");

        Role role = roleRepository.findByName(roleName);
        List<Role> roles = new ArrayList<>(Collections.singletonList(role));

        boolean success = userService.updateRole(id, roles);

        Map<String, Boolean> response = new HashMap<>();
        response.put("success", success);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    public String users(Model model) {
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    // handler method to handle deletion of books
    @GetMapping("/bookRegister/delete/{id}")
    public String bookDelete(@PathVariable("id") Long id) {
        bookService.deleteBookById(id);
        return "redirect:/bookRegister";
    }

    @GetMapping("/bookRegister/list")
    public String book(Model model) {
        List<BookDto> bookRegister = bookService.findAllBooks();
        model.addAttribute("bookRegister", bookRegister);
        return "bookRegister";
    }

    @GetMapping("/bookRegister")
    public String bookRegister(Model model) {
        List<BookDto> books = bookService.findAllBooks();
        model.addAttribute("books", books);
        model.addAttribute("bookDto", new BookDto());  // Ensure you have a blank form object
        return "bookRegister";
    }

    @PostMapping("/bookRegister/save")
    public String registrationBook(@Valid @ModelAttribute("bookDto") BookDto bookDto,
                                   BindingResult result,
                                   Model model) throws IOException {

        Book existingBook = bookService.findBookByName(bookDto.getName());

        if (existingBook != null && existingBook.getName() != null && !existingBook.getName().isEmpty()) {
            result.rejectValue("name", null,
                    "Aynı isim ile kayıtlı bir kitap zaten var");
        }

        if (result.hasErrors()) {
            List<BookDto> books = bookService.findAllBooks();
            model.addAttribute("books", books);  // Add this line to repopulate the book list
            model.addAttribute("bookDto", bookDto);  // Make sure to pass back the book DTO
            return "bookRegister";
        }

        bookService.saveBook(bookDto);
        return "redirect:/bookRegister?success";
    }


}