/*
 *
 * Author: Lina Walaa
 * Date: August 2021
 * Course: Java Web Developer Nanodegree Program by Udacity
 *
 * This is the CredentialMapper class that maps the POJO object: Credential with the DB
 * and runs queries on the respective CREDENTIALS table in the DB
 *
 */
package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface CredentialMapper {

    @Insert("INSERT INTO CREDENTIALS (userid, url, username, key, password) VALUES (#{userid}, #{url}, #{username}, #{key}, #{password})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialid")
    int insertCredential(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialid}")
    int deleteCredential(int credentialid);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, password = #{password} WHERE credentialid = #{credentialid}")
    int updateCredential(Credential credential);

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userid}")
    ArrayList<Credential> selectAllUserCredentials (User user);

    //returns either one row or none because each credential has its own id
    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{credentialid}")
    Credential selectCredential (int credentialid);
}
