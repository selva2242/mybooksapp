package com.example.mybooksapp.repo;

import com.example.mybooksapp.model.UserRequest;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRequestRepo extends JpaRepository<UserRequest, Long> {

  List<UserRequest> findByCreatedBy(long userId);

}
