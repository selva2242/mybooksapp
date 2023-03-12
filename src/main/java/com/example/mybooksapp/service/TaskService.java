package com.example.mybooksapp.service;

import com.example.mybooksapp.Constants;
import com.example.mybooksapp.model.Task;
import com.example.mybooksapp.model.UserAvailability;
import com.example.mybooksapp.model.UserRequest;
import com.example.mybooksapp.repo.TaskRepo;
import com.example.mybooksapp.repo.UserAvailabilityRepo;
import com.example.mybooksapp.repo.UserRequestRepo;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
public class TaskService {

  @Autowired
  private UserService userService;

  @Autowired
  private UserAvailabilityRepo userAvailabilityRepo;

  @Autowired
  private TaskRepo taskRepo;

  @Autowired
  private UserRequestRepo userRequestRepo;

  int workingHours = 8;

  private static Logger log = Logger.getLogger(UserRequestService.class.getSimpleName());


  public ResponseEntity<List<Task>> getAllTasks(String filter, String status, int page,
      Long userId) {

    try {
      List<Task> allTasks = new ArrayList<>();

      if (filter.equals("my")) {
        taskRepo.findByAssignedToAndStatusOrderByDeadlineAsc(userId, status)
            .forEach(allTasks::add);
      } else {
        taskRepo.findAllByStatusOrderByDeadlineAsc(status).forEach(allTasks::add);
      }

      if (allTasks.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(allTasks, HttpStatus.OK);

    } catch (Exception e) {
      log.log(Level.INFO, "Exception occurred in get all Tasks :: {0}", e.getMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public Task createTask(UserRequest userRequest, JSONObject taskDetails, Optional<Long> userId) {

    try {

      Task newTask = new Task();

      log.info(taskDetails.toString());
      newTask.setTaskName(taskDetails.getString("name"));
      newTask.setCreatedAt(new Date());
      newTask.setRequestId(userRequest.getRequestId());

      //update Deadline
      DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(new Date());
      calendar.add(Calendar.DAY_OF_YEAR, taskDetails.getInt("deadline"));
      Date deadline = calendar.getTime();
      newTask.setDeadline(formatter.parse(formatter.format(deadline)));


      UserAvailability availableUserDetails;
      if (userId.isPresent()) {
        log.info("The request with follow up tasks so assigned experts by default ");
        // get the availability of the user who did previous task of same request
        Long assignedUserId = userId.get();
        availableUserDetails = userAvailabilityRepo.findByUserId(assignedUserId);
        newTask.setAssignedTo(assignedUserId);
      } else {
        // get UserAvailability
        availableUserDetails = userService.getUserAvailability(deadline);

        log.info("availableUserDetails " + availableUserDetails.toString());
      }


      if (availableUserDetails.getUserId() <= 0) {
        log.info("no user available block");

        // queue the task since no expert available 
        newTask.setStatus("queued");
      } else {
        int occupiedHours = availableUserDetails.getOccupiedHours();
        int remainingHours = workingHours - occupiedHours;
        int requiredHours = taskDetails.getInt("requiredHours");
        Date occupiedUntil = availableUserDetails.getOccupiedUntil();

        if ((occupiedHours == remainingHours || remainingHours < requiredHours) &&
            occupiedUntil.compareTo(deadline) == 0
        ) {
          log.info("user has less time within the deadline");

          // deadline equals occupiedUntil and not enough hours to ge the task done so queue task
          newTask.setStatus("queued");

        } else {

          if (remainingHours < requiredHours) {
            // Update the occupiedUntil date to be next date 
            calendar.setTime(occupiedUntil);
            calendar.add(Calendar.DAY_OF_YEAR, 1);

            availableUserDetails.setOccupiedUntil(
                formatter.parse(formatter.format(calendar.getTime())));
            availableUserDetails.setOccupiedHours(requiredHours - remainingHours);
          } else {
            availableUserDetails.setOccupiedHours(occupiedHours + requiredHours);
            newTask.setAssignedTo(availableUserDetails.getUserId());
          }

          newTask.setStatus("active");
          newTask.setAssignedTo(availableUserDetails.getUserId());
        }

        userAvailabilityRepo.save(availableUserDetails);

      }
      log.info(newTask.toString());

      taskRepo.save(newTask);

      return newTask;

    } catch (Exception e) {
      e.printStackTrace();
      log.log(Level.SEVERE, "Exception occurred in create Task :: {0}", e.getMessage());
    }
    return new Task();

  }


  public ResponseEntity<Task> resolveTask(long taskId) {
    try {

      Optional<Task> existingTask = taskRepo.findById(taskId);

      if (!existingTask.isPresent()) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      Task updatedTask = existingTask.get();
      updatedTask.setStatus("resolved");

      Task updatedTaskObj = taskRepo.save(updatedTask);

      checkAndCreateFollowUpTasks(updatedTask);

      return new ResponseEntity<>(updatedTaskObj, HttpStatus.OK);
    } catch (Exception e) {
      log.log(Level.INFO, "Exception occurred in resolving  task :: {0}", e.getMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public void checkAndCreateFollowUpTasks(Task currentTask) {
    try {
      Optional<UserRequest> userRequest = userRequestRepo.findById(currentTask.getRequestId());

      if (userRequest.isPresent()) {
        UserRequest existingRequest = userRequest.get();

        String taskName = currentTask.getTaskName();

        JSONArray currentRequestTasks = Constants.REQUEST_TYPES.getJSONArray(
            existingRequest.getRequestType());

        // check if there are any followup tasks exist for the request
        int nextTaskIndex = 0;
        for (int i = 0; i < currentRequestTasks.length(); i++) {
          JSONObject taskDetails = currentRequestTasks.getJSONObject(i);

          if (taskDetails.has("name") && taskDetails.getString("name")
              .equals(taskName)) {
            nextTaskIndex = i + 1;
            break;
          }
        }

        if (nextTaskIndex < currentRequestTasks.length()) {
          JSONObject nextTaskDetails = currentRequestTasks.getJSONObject(nextTaskIndex);

          // create followup task and assign it to the same expert
          Task nextTask = createTask(existingRequest, nextTaskDetails,
              Optional.of(existingRequest.getAssignedTo()));

          if (nextTask.getTaskId() > 0) {
            log.info("coming here");
            //update task list in request details
            List<Long> taskList = existingRequest.getTasks();
            taskList.add(nextTask.getTaskId());
            existingRequest.setTasks(taskList);
            existingRequest.setCurrentTask(nextTask.getTaskName());

//            // update current task in request details
//            if (nextTask.getAssignedTo() >= 0) {
//              existingRequest.setCurrentTask(taskName);
//            }
          }

        } else {
          existingRequest.setCurrentTask("completed-all");
          existingRequest.setStatus("completed");
        }

        userRequestRepo.save(existingRequest);

      }

    } catch (Exception e) {
      log.log(Level.INFO, "Exception occurred in resolving  task :: {0}", e.getMessage());
    }
  }


}
