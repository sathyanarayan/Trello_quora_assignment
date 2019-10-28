package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SigninResponse;
import com.upgrad.quora.api.model.SignoutResponse;
import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.*;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This Class intend to handle user signup,signin,signout features
 */

@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserBusinessService userBusinessService;

    @RequestMapping(method = RequestMethod.POST, path = "/user/signup/", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupUserResponse> user_sign_up( final SignupUserRequest signupUserRequest) throws SignUpRestrictedException {
        try{
        final UserEntity userEntity = new UserEntity();
        userEntity.setUuid(UUID.randomUUID().toString());
        userEntity.setRole("nonadmin");
        userEntity.setUserName(signupUserRequest.getUserName());
        userEntity.setPassword(signupUserRequest.getPassword());
        userEntity.setFirstName(signupUserRequest.getFirstName());
        userEntity.setLastName(signupUserRequest.getLastName());
        userEntity.setEmail(signupUserRequest.getEmailAddress());
        final UserEntity createdUserEntity = userBusinessService.signup(signupUserRequest.getUserName(),signupUserRequest.getEmailAddress(),userEntity);
        SignupUserResponse userResponse = new SignupUserResponse().id(createdUserEntity.getUuid()).status("User Successfully Registered");
        return new ResponseEntity<SignupUserResponse>(userResponse, HttpStatus.CREATED);}
        catch (Exception e)
        {throw e;}

    }

    @RequestMapping(method = RequestMethod.POST, path = "/user/signin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SigninResponse> user_sign_in(@RequestHeader("authorization") final String authorization) throws AuthenticationFailedException {

        try{
        byte[] decrypt = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
        String decrypt_string = new String(decrypt);
        String[] decrypted_vals = decrypt_string.split(":");
        UserAuthEntity userAuthEntity = userBusinessService.signin(decrypted_vals[0], decrypted_vals[1]);
        UserEntity userEntity = userAuthEntity.getUser();
        HttpHeaders header = new HttpHeaders();
        SigninResponse signinResponse = new SigninResponse().id(userEntity.getUuid()).message("Signed in successfully");
        header.add("access-token", userAuthEntity.getAccessToken());
        return new ResponseEntity<>(signinResponse, HttpStatus.OK);
        }
        catch (Exception e)
        {throw  e;}
    }

    @RequestMapping(method = RequestMethod.POST, path = "/user/signout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignoutResponse> user_sign_out(@RequestHeader("authorization") final String authorization) throws SignOutRestrictedException, AuthorizationFailedException {
        String auth_token = authorization.split("Bearer ")[1];
        UserAuthEntity userAuthEntity = null;
        try {
            userAuthEntity = userBusinessService.signout(auth_token);
            SignoutResponse signoutResponse = new SignoutResponse().id(userAuthEntity.getUuid()).message("Signed out  successfully");
            return new ResponseEntity<SignoutResponse>(signoutResponse,HttpStatus.OK);
        }
        catch (Exception e)
        {throw  e;
        }
    }
}
