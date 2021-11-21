/*
 *
 * Author: Lina Walaa
 * Date: August 2021
 * Course: Java Web Developer Nanodegree Program by Udacity
 *
 * This is the File class that maps to a record in the DB's "FILE" table
 *
 */
package com.udacity.jwdnd.course1.cloudstorage.model;

public class File {

    private Integer fileid, userid;
    private String filename, contenttype;
    private long filesize;
    private byte[] filedata;

    public File() {
    }

    public File(Integer fileid, Integer userid, String filename, String contenttype, long filesize, byte[] filedata) {
        this.fileid = fileid;
        this.userid = userid;
        this.filename = filename;
        this.contenttype = contenttype;
        this.filesize = filesize;
        this.filedata = filedata;
    }

    public File(Integer userid, String filename, String contenttype, long filesize, byte[] filedata) {
        this.userid = userid;
        this.filename = filename;
        this.contenttype = contenttype;
        this.filesize = filesize;
        this.filedata = filedata;
    }

    public Integer getFileid() {
        return fileid;
    }

    public void setFileid(Integer fileid) {
        this.fileid = fileid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContenttype() {
        return contenttype;
    }

    public void setContenttype(String contenttype) {
        this.contenttype = contenttype;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    public byte[] getFiledata() {
        return filedata;
    }

    public void setFiledata(byte[] filedata) {
        this.filedata = filedata;
    }
}
