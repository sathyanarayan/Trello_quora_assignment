package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * This Service Class DAO layer to interact with DB
 */
@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public UserEntity create_user(UserEntity userEntity){
         entityManager.persist(userEntity);
        return userEntity;
    }

    public UserEntity get_username(final String username){
        try{
            return entityManager.createNamedQuery("username", UserEntity.class).setParameter("username",username).getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }

    public UserEntity getuser_email(final String email){
        try {
            return entityManager.createNamedQuery("userByEmail", UserEntity.class).setParameter("email", email).getSingleResult();
        }catch(NoResultException nre){
            return null;
        }

    }

    public UserAuthEntity create_Token(final UserAuthEntity userAuthEntity){
        entityManager.persist(userAuthEntity);
        return userAuthEntity;
    }

    public void update_user(final UserEntity updatedUserEntity){
        entityManager.merge(updatedUserEntity);
    }

    public void update_user_Token(final UserAuthEntity updatedUserAuthEntity){
        entityManager.merge(updatedUserAuthEntity);
    }

    public UserAuthEntity get_user_Token(final String accessToken){
        try{
            UserAuthEntity userAuthEntity = entityManager.createNamedQuery("UserAuthDetails",UserAuthEntity.class).setParameter("accessToken",accessToken).getSingleResult();
            return userAuthEntity;

        }catch (NoResultException nre){
            return null;
        }
    }

    public UserEntity get_user_from_Uuid(final String userId){
        try {
            return entityManager.createNamedQuery("userByUuid", UserEntity.class).setParameter("uuid", userId).getSingleResult();
        }catch(NoResultException nre){
            return null;
        }

    }
    public UserEntity delete_user ( UserEntity userEntity){
        entityManager.remove(userEntity);
        return userEntity;
    }

}
