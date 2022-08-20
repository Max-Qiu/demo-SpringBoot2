package com.maxqiu.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.maxqiu.demo.entity.User;

/**
 * @author Max_Qiu
 */
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    @Query(value = "SELECT * FROM user WHERE name LIKE CONCAT('%',?1,'%') ", nativeQuery = true)
    List<User> listByName(String name);
}
