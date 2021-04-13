package com.maxqiu.demo.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.maxqiu.demo.entity.User;

/**
 * @author Max_Qiu
 */
@Repository
public interface UserRepository extends ElasticsearchRepository<User, Integer> {

    List<User> findUsersByNameAndAddress(String name, String address);
}
