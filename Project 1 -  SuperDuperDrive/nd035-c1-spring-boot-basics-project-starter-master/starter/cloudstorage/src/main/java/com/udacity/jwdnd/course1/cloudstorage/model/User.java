/*
 *
 * Author: Lina Walaa
 * Date: August 2021
 * Course: Java Web Developer Nanodegree Program by Udacity
 *
 * This is the User class that maps to a record in the DB's "USER" table
 *
 */
package com.udacity.jwdnd.course1.cloudstorage.model;

public class User {

    private Integer userid;
    private String username, salt, password, firstname, lastname;

    public User(Integer userid, String username, String salt, String password, String firstname, String lastname) {
        this.userid = userid;
        this.username = username;
        this.salt = salt;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
