package com.yusuf.lims.service;

import com.yusuf.lims.dto.UserDto;
import com.yusuf.lims.entity.Role;
import com.yusuf.lims.entity.User;
import com.yusuf.lims.repository.RoleRepository;
import com.yusuf.lims.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserDto userDto) throws IOException {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = roleRepository.findByName("ROLE_USER");
        if (role == null) {
            role = checkRoleExist();
        }
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> {
                    UserDto userDto = mapToUserDto(user);
                    userDto.setRoles(user.getRoles().stream()
                            .collect(Collectors.toList()));
                    return userDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteUser(Long id) {
        User user = userRepository.getReferenceById(id);
        user.getRoles().clear();
        userRepository.save(user);
        userRepository.delete(user);
        return true;
    }

    @Override
    public boolean updateUser(Long id, String password, String firstName, String lastName) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (password != null && !password.trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        user.setFirstName(firstName);
        user.setLastName(lastName);

        userRepository.save(user);
        return true;
    }

    @Override
    public boolean updateRole(Long id, List<Role> roles) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (roles != null && !roles.isEmpty()) {
            // Create a new modifiable list
            List<Role> newRoles = new ArrayList<>(roles);
            user.setRoles(newRoles);
        }

        userRepository.save(user);
        return true;
    }

    public UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUserBooks(user.getUserBooks());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setId(user.getId());
        return userDto;
    }

    private Role checkRoleExist() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }
}