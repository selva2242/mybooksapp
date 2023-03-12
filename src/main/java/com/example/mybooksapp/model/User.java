package com.example.mybooksapp.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User {
//  Users table 
//——————
//
//
//  Name : string
//  userType : string
//  createdAt : dateTime
//  userId : string


  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long userId;

  private String userName;

  private Date createdAt;

  private String userType;
}
