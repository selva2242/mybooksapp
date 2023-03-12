package com.example.mybooksapp.controller;

import com.example.mybooksapp.model.User;
import com.example.mybooksapp.service.UserService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  @Autowired
  private UserService userService;

  private static Logger log = Logger.getLogger(UserController.class.getSimpleName());

  @GetMapping("/")
  public String getUser() {
    try {
      return userService.getUserDetails();
    } catch (Exception e) {
      log.log(Level.INFO, "Exception occurred in get all user :: {0}", e.getMessage());
    }
    return "Exception";
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<User> getUser(@PathVariable long userId) {
    try {

      return userService.getUser(userId);

    } catch (Exception e) {
      log.log(Level.INFO, "Exception occurred in get user :: {0}", e.getMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/user")
  public ResponseEntity<User> addUser(@RequestBody User user) {
    try {

      return userService.addUser(user);

    } catch (Exception e) {
      log.log(Level.INFO, "Exception occurred in add user :: {0}", e.getMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

  @PutMapping("/user/{userId}")
  public ResponseEntity<User> updateUser(@PathVariable long userId,
      @RequestBody User user) {
    try {

      return userService.updateUser(userId, user);

    } catch (Exception e) {
      log.log(Level.INFO, "Exception occurred in update user :: {0}", e.getMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/user/{userId}")
  public ResponseEntity<HttpStatus> deleteUser(@PathVariable long userId) {
    try {

      return userService.deleteUser(userId);

    } catch (Exception e) {
      log.log(Level.INFO, "Exception occurred in delete user :: {0}", e.getMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


}
