/*
 *
 * Author: Lina Walaa
 * Date: August 2021
 * Course: Java Web Developer Nanodegree Program by Udacity
 *
 * This is the CredentialForm class that holds that data that will be entered in the
 * note's form
 *
 */
package com.udacity.jwdnd.course1.cloudstorage.model;

public class CredentialForm {

    private String id, url, username, password;

    public CredentialForm() {
        this.id = "";
        this.url = "";
        this.username = "";
        this.password = "";
    }

    public CredentialForm(String id, String url, String username, String password) {
        this.id = id;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
