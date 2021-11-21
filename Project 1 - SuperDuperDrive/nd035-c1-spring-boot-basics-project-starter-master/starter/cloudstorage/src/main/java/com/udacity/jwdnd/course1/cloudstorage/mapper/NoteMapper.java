/*
 *
 * Author: Lina Walaa
 * Date: August 2021
 * Course: Java Web Developer Nanodegree Program by Udacity
 *
 * This is the NoteMapper class that maps the POJO object: Note with the DB
 * and runs queries on the respective NOTES table in the DB
 *
 */
package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface NoteMapper {

    @Insert("INSERT INTO NOTES (userid, notetitle, notedescription) VALUES (#{userid}, #{notetitle}, #{notedescription})")
    @Options(useGeneratedKeys = true, keyProperty = "noteid")
    int insertNote(Note note);

    @Select("SELECT * FROM NOTES WHERE userid = #{userid}")
    ArrayList<Note> selectAllUserNotes(User user);

    @Select("SELECT * FROM NOTES WHERE noteid = #{noteid}")
    Note selectNote(int noteid);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteid}")
    int deleteNote(int noteid);

    @Update("UPDATE NOTES SET notetitle = #{notetitle}, notedescription = #{notedescription} WHERE noteid = #{noteid}")
    int updateNote(Note note);
}
