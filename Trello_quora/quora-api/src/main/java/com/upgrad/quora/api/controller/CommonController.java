package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.CommonBusinessService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * This Class intend to handle user profile requests
 */
@RestController
@RequestMapping("/")
public class CommonController {

    @Autowired
    private CommonBusinessService commonBusinessService;

    @RequestMapping(method = RequestMethod.GET, path = "/userprofile/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDetailsResponse> getUser(@PathVariable("userId") final String userId,
                                                       @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        try {
            String comm_token = authorization.split("Bearer ")[1];
            UserEntity userEntity = commonBusinessService.get_user(userId, comm_token);
            UserDetailsResponse userDetailsResponse = new UserDetailsResponse();
            userDetailsResponse.setUserName(userEntity.getUserName());
            userDetailsResponse.setAboutMe(userEntity.getAboutMe());
            userDetailsResponse.setContactNumber(userEntity.getContactNumber());
            userDetailsResponse.setCountry(userEntity.getCountry());
            userDetailsResponse.setDob(userEntity.getDob());
            userDetailsResponse.setEmailAddress(userEntity.getEmail());
            userDetailsResponse.setFirstName(userEntity.getFirstName());
            userDetailsResponse.setLastName(userEntity.getLastName());
            return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);
        }
        catch (Exception e)
        {throw e;}
    }
}