package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service

public class AdminBusinessService {
    @Autowired
    private UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public String deleteUser(final String userId, final String authorizationToken) throws AuthorizationFailedException, UserNotFoundException {

        UserAuthEntity userAuthEntity = userDao.get_user_Token(authorizationToken);

        if(userAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001","user has not signed in");
        }else if(userAuthEntity.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002","user is signed out");
        }else if(userAuthEntity.getUser().getRole().toUpperCase().compareTo("admin") != 0){
            throw new AuthorizationFailedException("ATHR-003","unauthorized Access, Entered user is not an admin");
        }
        UserEntity userEntity = userDao.get_user_from_Uuid(userId);

        if(userEntity == null){
            throw new UserNotFoundException("USR-001","user with entered uuid to be deleted does not exist");
        }
        UserEntity deletedUser = userDao.delete_user(userEntity);
        return deletedUser.getUuid();
    }
}