package com.example.mybooksapp.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
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
public class UserAvailability {
//  userAvailabiility table:
//
//      ——————————
//
//  userId  :string
//  occupiedUntil :Date
//  occupiedHours : Integer

  @Id
  private long userId;

  private Date occupiedUntil;

  private Integer occupiedHours;


}
