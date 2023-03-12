package com.example.mybooksapp.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Task {


//  Task table:
//      ———————
//  requestId : string
//  taskName: string
//  Deadline:  date
//  Created : dateTime
//  assignedTo : string ( userId)
//  Status: string. ( queued, active, completed, expired)

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long taskId;

  private long requestId;

  private String taskName;

  private Date deadline;

  private Date createdAt;

  private long assignedTo;

  private String status;

}
