package com.filesrest.service;

import com.filesrest.model.UserEntity;
import com.filesrest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRep;

    public List<UserEntity> getAll() {
        log.info("In method getAll() UserService ");
        return userRep.findAll();
    }


    public UserEntity getById(Integer id) {
        Optional<UserEntity> optional = userRep.findById(id);
        log.info("UserService с id " + id);
        return optional.orElse(null);
    }


    public UserEntity create(UserEntity user) {
        log.info("Created User");
        return userRep.save(user);
    }


    public UserEntity update(UserEntity user) {
        log.info("Updated User");
        return userRep.save(user);
    }


    public void deleteById(Integer id) {
        log.info("Deleted User");
        userRep.deleteById(id);
    }
}
