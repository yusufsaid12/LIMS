package com.yusuf.lims.service;

import com.yusuf.lims.dto.UserDto;
import com.yusuf.lims.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();
}
