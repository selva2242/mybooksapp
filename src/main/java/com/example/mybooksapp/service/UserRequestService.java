package com.example.mybooksapp.service;

import com.example.mybooksapp.Constants;
import com.example.mybooksapp.model.Task;
import com.example.mybooksapp.model.UserRequest;
import com.example.mybooksapp.repo.UserRequestRepo;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserRequestService {

  @Autowired
  private UserRequestRepo userRequestRepo;

  @Autowired
  private TaskService taskService;

  private static Logger log = Logger.getLogger(UserRequestService.class.getSimpleName());

  public ResponseEntity<UserRequest> createRequest(UserRequest userRequest) {

    try {

      JSONObject availableRequests = Constants.REQUEST_TYPES;
      log.info("user Request " + userRequest.getRequestType());
      if (!availableRequests.has(userRequest.getRequestType())) {

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }

      userRequest.setCreatedAt(new Date());
      userRequest.setStatus("yetToStart");
      userRequest.setCurrentTask("none");
      List<Long> taskList = new ArrayList<>();
      userRequest.setTasks(taskList);
      userRequest.setAssignedTo(Long.valueOf(0));
      userRequestRepo.save(userRequest);


      log.info("here hagaya");

      JSONArray currentRequestTasks = Constants.REQUEST_TYPES.getJSONArray(
          userRequest.getRequestType());

      log.info("currentRequestTasks" + currentRequestTasks.toString());


      JSONObject taskDetails = (JSONObject) currentRequestTasks.get(0);

      log.info("taskDetails" + taskDetails.toString());


      Task newTask = taskService.createTask(userRequest, taskDetails, Optional.empty());

      log.info("newTask" + newTask.toString());


      if (newTask.getTaskId() >= 0) {
        //update user task list
        taskList.add(newTask.getTaskId());
        userRequest.setCurrentTask(newTask.getTaskName());
        userRequest.setCurrentTask(newTask.getTaskName());
        userRequest.setStatus("active");
        // update user request if task is assigned to any expert
        if (newTask.getAssignedTo() >= 0) {
          userRequest.setAssignedTo(newTask.getAssignedTo());

        }
        userRequestRepo.save(userRequest);
      }
      ;

      return new ResponseEntity<>(userRequest, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      log.log(Level.SEVERE, "Exception occurred in create Request :: {0}", e.getMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

  public ResponseEntity<List<UserRequest>> getAllRequests(long userId) {

    try {
//      if (userId == 0) {
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//      }

      List<UserRequest> allRequests = new ArrayList<>();

      userRequestRepo.findByCreatedBy(userId).forEach(allRequests::add);

      if (allRequests.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(allRequests, HttpStatus.OK);

    } catch (Exception e) {
      log.log(Level.INFO, "Exception occurred in get all requests :: {0}", e.getMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
