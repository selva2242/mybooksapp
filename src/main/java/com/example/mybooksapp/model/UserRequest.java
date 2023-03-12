package com.example.mybooksapp.model;

import java.util.Date;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long requestId;

  private String requestType;

  private long createdBy;

  private String status;

  private long assignedTo;

  private Date createdAt;

  @ElementCollection
  @CollectionTable(name = "user_request_tasks", joinColumns = @JoinColumn(name = "request_id"))
  @Column(name = "task_id")
  private List<Long> tasks;

  private String currentTask;

}
