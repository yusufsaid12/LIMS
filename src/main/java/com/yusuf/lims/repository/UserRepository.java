package com.yusuf.lims.repository;


import com.yusuf.lims.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface UserRepository extends JpaRepository<User, String> {

    User findByEmail(String email);
}
