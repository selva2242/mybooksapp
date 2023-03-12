package com.example.mybooksapp.controller;

import com.example.mybooksapp.model.Task;
import com.example.mybooksapp.service.TaskService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {

  @Autowired
  private TaskService taskService;

  private static Logger log = Logger.getLogger(TaskController.class.getSimpleName());


  @GetMapping("/tasks")
  public ResponseEntity<List<Task>> getAllTasks(
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "filter", defaultValue = "all") String filter,
      @RequestParam(name = "status", defaultValue = "0") String status,
      @RequestParam(name = "userId", defaultValue = "0") Long userId

  ) {
    try {
      return taskService.getAllTasks(filter, status, page, userId);

    } catch (Exception e) {
      log.log(Level.INFO, "Exception occurred in get all tracks :: {0}", e.getMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/task/{taskId}/resolve")
  public ResponseEntity<Task> resolveTask(@PathVariable long taskId) {
    try {

      return taskService.resolveTask(taskId);

    } catch (Exception e) {
      log.log(Level.INFO, "Exception occurred in resolving  task :: {0}", e.getMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
