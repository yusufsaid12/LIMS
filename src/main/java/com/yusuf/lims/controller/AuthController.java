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
import com.yusuf.lims.service.UserService;
import com.yusuf.lims.service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Controller
public class AuthController {

    private final RoleRepository roleRepository;
    private BookRepository bookRepository;
    private UserService userService;
    private BookService bookService;
    private UserServiceImpl userServiceImpl;
    private UserRepository userRepository;

    public AuthController(UserService userService, BookService bookService, BookRepository bookRepository, UserRepository userRepository, UserServiceImpl userServiceImpl, RoleRepository roleRepository) {
        this.userService = userService;
        this.bookService = bookService;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.userServiceImpl = userServiceImpl;
        this.roleRepository = roleRepository;
    }

    // handler method to handle home page request
    @GetMapping("/index")
    public ModelAndView index(Authentication authentication) {
        ModelAndView modelAndView = new ModelAndView("index");
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            UserDto user = userServiceImpl.mapToUserDto(userRepository.findByEmail(userDetails.getUsername()));
            String role = authentication.getAuthorities().toString();
            modelAndView.addObject("role", role);
            modelAndView.addObject("user", user);
        }
        return modelAndView;
    }

    // handler method to handle login request
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/store")
    public String showBooks(Model model) {
        List<BookDto> books = bookService.findAllBooks(); // Kitapları veritabanından al
        model.addAttribute("books", books); // Kitapları model'e ekle
        return "store"; // Thymeleaf şablonunun adı
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
                               Model model) {
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

    @PostMapping("/users/update")
    public String updateUser(@ModelAttribute("usr") UserDto userDto) {

        userService.updateUser(userDto.getId(), userDto.getPassword(), userDto.getRoles().get(0));
        return "redirect:/users"; // veya uygun bir gösterim sayfasına yönlendirme
    }

    @GetMapping("/users")
    public String users(Model model) {
        List<UserDto> users = userService.findAllUsers();
        UserDto userDto = new UserDto();
        Role user_role = roleRepository.findByName("ROLE_USER");
        user.setRoles(Arrays.asList(role));
        Role admin_role = roleRepository.findByName("ROLE_ADMIN");
        model.addAttribute("role_user", user_role);
        model.addAttribute("role_admin", admin_role);
        model.addAttribute("usr", userDto);
        model.addAttribute("users", users);
        return "users";
    }

}