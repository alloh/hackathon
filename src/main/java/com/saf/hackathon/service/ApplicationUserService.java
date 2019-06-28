package com.saf.hackathon.service;

import com.saf.hackathon.pojo.UserObj;
/**
 * 
 * @author user
 * Holds all user related operations
 * @param <T>
 */
public interface ApplicationUserService<T>{
 T prepareAppUserForInsert(UserObj filter) throws Exception;
}

