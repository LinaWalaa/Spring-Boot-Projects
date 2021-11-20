/*
 *
 * Author: Lina Walaa
 * Date: August 2021
 * Course: Java Web Developer Nanodegree Program by Udacity
 *
 * This is the FileMapper class that maps the POJO object: File with the DB
 * and runs queries on the respective FILES table in the DB
 *
 */
package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface FileMapper {

    @Insert("INSERT INTO FILES (userid, filename, contenttype, filesize, filedata) VALUES (#{userid}, #{filename}, #{contenttype}, #{filesize}, #{filedata})")
    @Options(useGeneratedKeys = true, keyProperty = "fileid")
    int insertFile(File file);

    @Delete("DELETE FROM FILES WHERE fileid = #{fileid}")
    int deleteFile(int fileid);

    @Select("SELECT * FROM FILES WHERE fileid = #{fileid}")
    File selectFileById(int fileid);

    @Select("SELECT * FROM FILES WHERE filename = #{filename} AND userid = #{userid}")
    File selectFileByName(int userid, String filename);

    @Select("SELECT * FROM FILES WHERE userid = #{userid}")
    ArrayList<File> selectAllUserFiles(User user);
}
