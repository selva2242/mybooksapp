package com.example.mybooksapp.repo;

import com.example.mybooksapp.model.UserAvailability;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAvailabilityRepo extends JpaRepository<UserAvailability, Long> {
  List<UserAvailability> findByOccupiedUntilLessThanEqualOrderByOccupiedUntilAscOccupiedHoursAsc(
      Date deadlineDay);

  UserAvailability findByUserId(long userId);
}
