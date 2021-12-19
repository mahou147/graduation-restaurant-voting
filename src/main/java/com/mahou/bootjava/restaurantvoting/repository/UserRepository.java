package com.mahou.bootjava.restaurantvoting.repository;

import com.mahou.bootjava.restaurantvoting.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}