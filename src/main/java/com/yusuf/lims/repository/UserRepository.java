package com.yusuf.lims.repository;

import com.yusuf.lims.dto.UserDto;
import com.yusuf.lims.entity.Role;
import com.yusuf.lims.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
