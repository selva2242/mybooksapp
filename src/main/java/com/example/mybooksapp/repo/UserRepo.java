package com.example.mybooksapp.repo;


import com.example.mybooksapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
  
}
