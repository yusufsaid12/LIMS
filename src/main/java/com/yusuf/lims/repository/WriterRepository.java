package com.yusuf.lims.repository;

import com.yusuf.lims.entity.Writer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WriterRepository extends JpaRepository<Writer, String> {

    Writer findByFirsthName(String firsth_name);
    Writer findByLastName(String last_name);

}
