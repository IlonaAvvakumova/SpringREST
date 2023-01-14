package com.filesrest.service;
import com.filesrest.model.UserEntity;
import com.filesrest.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;
    @MockBean
    UserEntity user;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAll() {
        userService.getAll();
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getById() {
        when(userRepository.findById(Mockito.anyInt())).thenReturn(java.util.Optional.of(new UserEntity()));
        userService.getById(1);
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void create() {
        when(userRepository.save(Mockito.mock(UserEntity.class))).thenReturn(user);
        userService.create(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void update() {
        when(userRepository.save(Mockito.mock(UserEntity.class))).thenReturn(user);
        userService.create(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteById() {
        userService.deleteById(1);
        verify(userRepository, times(1)).deleteById(1);
    }
}