package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * This Service class of Common Controller
 */
@Service
public class CommonBusinessService {
    @Autowired
    private UserDao userDao;

    public UserEntity get_user(String userUuid, String authorizationToken) throws AuthorizationFailedException, UserNotFoundException {

        UserAuthEntity userAuthEntity = userDao.get_user_Token(authorizationToken);
        if(userDao.get_user_Token(authorizationToken) == null){
            throw new AuthorizationFailedException("ATHR-001","user has not signed in");
        }else if(userAuthEntity.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002","user is signed out.Sign in first to get user details");
        }
        UserEntity userEntity = userDao.get_user_from_Uuid(userUuid);
        if(userEntity == null){
            throw new UserNotFoundException("USR-001","user with entered uuid does not exist");
        }
        return userEntity;
    }
}