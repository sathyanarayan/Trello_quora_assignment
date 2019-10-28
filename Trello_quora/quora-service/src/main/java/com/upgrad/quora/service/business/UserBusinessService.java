package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.time.ZonedDateTime;

/**
 * This Service Class contains business logic for user request handling
 */

@Service
public class UserBusinessService {

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    @Autowired
    private UserDao userDao;

    /**
     * This method handles signup of user
     *
     * @param username
     * @param email
     * @param userEntity object
     * @return new userEntity or raises exception accordingly
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signup(final String username, final String email, final UserEntity userEntity) throws SignUpRestrictedException {
        if (userDao.get_username(username) == null) {
            if (userDao.getuser_email(email) == null) {
                String[] encryptedText = cryptographyProvider.encrypt(userEntity.getPassword());
                userEntity.setSalt(encryptedText[0]);
                userEntity.setPassword(encryptedText[1]);
                return userDao.create_user(userEntity);
            }
            else{
                throw new SignUpRestrictedException("SGR-002", "This user has already been registered, try with any other emailId");
            }
        }
        else{
            throw new SignUpRestrictedException("SGR-001", "Try any other Username, this Username has already been taken");
        }
    }

    /**
     * This method handles signin of user
     *
     * @param username
     * @param password
     * @return new userEntity or raises exception accordingly
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity signin(final String username, final String password) throws AuthenticationFailedException {
        UserEntity userEntity = userDao.get_username(username);
        UserAuthEntity user_authentication = new UserAuthEntity();
        if(userEntity == null){
            throw new AuthenticationFailedException("ATH-001","This user does not exist");
        }

        final String encryptedPassword = cryptographyProvider.encrypt(password, userEntity.getSalt());
        if(encryptedPassword.equals(userEntity.getPassword())){
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            final ZonedDateTime session_in = ZonedDateTime.now();
            user_authentication.setUser(userEntity);
            user_authentication.setUuid(userEntity.getUuid());
            user_authentication.setAccessToken(jwtTokenProvider.generateToken(userEntity.getUuid(),session_in,session_in.plusMinutes(480)));
            user_authentication.setLoginAt(session_in);
            user_authentication.setExpiresAt(session_in.plusMinutes(480));
            userDao.create_Token(user_authentication);
            userDao.update_user(userEntity);
            return user_authentication;

        }
        else{
            throw new AuthenticationFailedException("ATH-002","Password failed");
        }
    }

    /**
     * This method handles signin of user
     *
     * @param authorizationToken
     * @return new userEntity or raises exception accordingly
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity signout(final String authorizationToken) throws SignOutRestrictedException {
        try {
            if(userDao.get_user_Token(authorizationToken) == null){
                throw new SignOutRestrictedException("SGR-001","User is not Signed in");
            }
            final ZonedDateTime session_time = ZonedDateTime.now();
            userDao.get_user_Token(authorizationToken).setLogoutAt(session_time);
            userDao.update_user_Token(userDao.get_user_Token(authorizationToken));
            return userDao.get_user_Token(authorizationToken);
        }
        //throwing exception which are not handles via code
        catch (Exception e)
        {throw  e;}
    }
}