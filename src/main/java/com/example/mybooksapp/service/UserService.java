package com.example.mybooksapp.service;

import com.example.mybooksapp.model.User;
import com.example.mybooksapp.model.UserAvailability;
import com.example.mybooksapp.repo.UserAvailabilityRepo;
import com.example.mybooksapp.repo.UserRepo;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  private UserRepo userRepo;

  @Autowired
  private UserAvailabilityRepo userAvailabilityRepo;

  private static final Logger log = Logger.getLogger(UserService.class.getName());

  public String getUserDetails() {
    return "Hey bro thanks for visiting the page";
  }

  public ResponseEntity<User> getUser(long userId) {
    try {
      Optional<User> user = userRepo.findById(userId);

      if (!user.isPresent()) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>(user.get(), HttpStatus.OK);

    } catch (Exception e) {
      log.log(Level.SEVERE, "Exception occurred in get user :: {0}", e.getMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public ResponseEntity<User> addUser(User user) {
    try {
      user.setCreatedAt(new Date());
      log.info("user Info");
      ;
      log.info(user.toString());
      User newUser = userRepo.save(user);

      //updating user Availability

      UserAvailability userAvailability = new UserAvailability();
      userAvailability.setUserId(newUser.getUserId());
      userAvailability.setOccupiedHours(0);
      DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
      userAvailability.setOccupiedUntil(formatter.parse(formatter.format(new Date())));

      if (user.getUserType().equals("expert")) {
        userAvailabilityRepo.save(userAvailability);
      }
      return new ResponseEntity<>(newUser, HttpStatus.OK);
    } catch (Exception e) {
      log.log(Level.SEVERE, "Exception occurred in add user :: {0}", e.getMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

  public ResponseEntity<User> updateUser(long userId, User user) {
    try {
      Optional<User> existingUser = userRepo.findById(userId);

      if (!existingUser.isPresent()) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      User updatedUser = existingUser.get();
      updatedUser.setUserName(user.getUserName());

      User updatedUserObj = userRepo.save(updatedUser);

      return new ResponseEntity<>(updatedUserObj, HttpStatus.OK);
    } catch (Exception e) {
      log.log(Level.SEVERE, "Exception occurred in update user :: {0}", e.getMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public ResponseEntity<HttpStatus> deleteUser(long userId) {
    try {

      Optional<User> existingUser = userRepo.findById(userId);

      if (!existingUser.isPresent()) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      userRepo.deleteById(userId);

      if (existingUser.get().getUserType().equals("expert")) {
        userAvailabilityRepo.deleteById(userId);
      }

      return new ResponseEntity<>(HttpStatus.OK);

    } catch (Exception e) {
      log.log(Level.SEVERE, "Exception occurred in delete user :: {0}", e.getMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public UserAvailability getUserAvailability(Date deadline) {
    try {
      List<UserAvailability> userAvailabilities =
          userAvailabilityRepo.findByOccupiedUntilLessThanEqualOrderByOccupiedUntilAscOccupiedHoursAsc(
              deadline);

//      Optional<User> user = userRepo.findById(userAvailabilities.get(0).getUserId());
//
//      if (user.isPresent()) {
//        return user.get();
//      }

      log.info("user Availabilities" + userAvailabilities.toString());

      if (userAvailabilities.size() > 0) {
        return userAvailabilities.get(0);
      }

    } catch (Exception e) {
      log.log(Level.SEVERE, "Exception occurred in getting user availbility user :: {0}",
          e.getMessage());
    }
    return new UserAvailability();
  }
}
