package com.bestarch.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bestarch.demo.domain.UserProfile;

@Repository
public interface UserProfileCrudRepository extends CrudRepository<UserProfile, String> {
    
}