package com.byoon.lastminuteflix.entity;

public class User {
  private int userId;

  private String username;
  private String password;
  private boolean isAdmin;

  public User(String username, String password, boolean isAdmin) {
    this.username = username;
    this.password = password;
    this.isAdmin = isAdmin;
  }
}
