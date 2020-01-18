package it.unical.demacs.asde.signme.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Invitation")
public class Invitation {

 @Id
 private String invitationId;
 private Integer course;
 private String email;
 
 public Invitation() {
 }

 public Invitation(String invitationId, String email, Integer course, Boolean pending) {
  super();
  this.invitationId = invitationId;
  this.email = email;
  this.course = course;
 }

 public String getInvitationId() {
  return invitationId;
 }

 public void setInvitationId(String invitationId) {
  this.invitationId = invitationId;
 }

 public Integer getCourse() {
  return course;
 }

 public void setCourse(Integer course) {
  this.course = course;
 }
 
 public String getEmail() {
  return email;
 }
 
 public void setEmail(String email) {
  this.email = email;
 }
 


}