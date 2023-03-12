package com.example.mybooksapp.repo;

import com.example.mybooksapp.model.Task;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepo extends JpaRepository<Task, Long> {

  List<Task> findByAssignedToOrderByDeadlineAsc(long assignedTo);

  List<Task> findAllByStatusOrderByDeadlineAsc(String status);

  List<Task> findByAssignedToAndStatusOrderByDeadlineAsc(long assignedTo, String status);

}
