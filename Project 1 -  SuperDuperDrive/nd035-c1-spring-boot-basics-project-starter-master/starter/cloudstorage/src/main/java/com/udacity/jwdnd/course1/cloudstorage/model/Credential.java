/*
 *
 * Author: Lina Walaa
 * Date: August 2021
 * Course: Java Web Developer Nanodegree Program by Udacity
 *
 * This is the Credential class that maps to a record in the DB's "CREDENTIAL" table
 *
 */
package com.udacity.jwdnd.course1.cloudstorage.model;

public class Credential {

    private Integer credentialid, userid;
    private String url, username, key, password;

    public Credential() {
        this.userid = -1;
        this.credentialid = -1;
        this.url = "";
        this.username = "";
        this.key = "";
        this.password = "";
    }

    public Credential(Integer userid, Integer credentialid, String url, String username, String key, String password) {
        this.userid = userid;
        this.credentialid = credentialid;
        this.url = url;
        this.username = username;
        this.key = key;
        this.password = password;
    }

    public Credential(Integer userid, String url, String username, String key, String password) {
        this.userid = userid;
        this.url = url;
        this.username = username;
        this.key = key;
        this.password = password;
    }

    public Integer getCredentialid() {
        return credentialid;
    }

    public void setCredentialid(Integer credentialid) {
        this.credentialid = credentialid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
