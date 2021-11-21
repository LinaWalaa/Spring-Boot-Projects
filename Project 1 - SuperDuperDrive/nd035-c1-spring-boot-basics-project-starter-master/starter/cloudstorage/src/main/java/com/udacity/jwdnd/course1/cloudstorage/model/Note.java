/*
 *
 * Author: Lina Walaa
 * Date: August 2021
 * Course: Java Web Developer Nanodegree Program by Udacity
 *
 * This is the Note class that maps to a record in the DB's "NOTE" table
 *
 */
package com.udacity.jwdnd.course1.cloudstorage.model;

public class Note {

    private Integer noteid, userid;
    private String notetitle, notedescription;

    public Note() {
        this.noteid = -1;
        this.userid = -1;
        this.notetitle = "";
        this.notedescription = "";
    }

    public Note(Integer noteid, Integer userid, String notetitle, String notedescription) {
        this.noteid = noteid;
        this.userid = userid;
        this.notetitle = notetitle;
        this.notedescription = notedescription;
    }

    public Note(Integer userid, String notetitle, String notedescription) {
        this.userid = userid;
        this.notetitle = notetitle;
        this.notedescription = notedescription;
    }

    public Integer getNoteid() {
        return noteid;
    }

    public void setNoteid(Integer noteid) {
        this.noteid = noteid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getNotetitle() {
        return notetitle;
    }

    public void setNotetitle(String notetitle) {
        this.notetitle = notetitle;
    }

    public String getNotedescription() {
        return notedescription;
    }

    public void setNotedescription(String notedescription) {
        this.notedescription = notedescription;
    }
}
