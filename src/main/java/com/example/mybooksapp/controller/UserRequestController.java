package com.example.mybooksapp.controller;

import com.example.mybooksapp.model.UserRequest;
import com.example.mybooksapp.service.UserRequestService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRequestController {

  @Autowired
  private UserRequestService userRequestService;

  private static Logger log = Logger.getLogger(UserRequestController.class.getSimpleName());

  @PostMapping("/request")
  public ResponseEntity<UserRequest> createRequest(@RequestBody UserRequest userRequest) {
    try {
      return userRequestService.createRequest(userRequest);
    } catch (Exception e) {
      log.log(Level.SEVERE, "Exception occurred in create Request :: {0}", e.getMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/requests")
  public ResponseEntity<List<UserRequest>> getAllRequests(
      @RequestParam(name = "userId", defaultValue = "") long userId
  ) {
    try {
      return userRequestService.getAllRequests(userId);

    } catch (Exception e) {
      log.log(Level.INFO, "Exception occurred in get all requests :: {0}", e.getMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
