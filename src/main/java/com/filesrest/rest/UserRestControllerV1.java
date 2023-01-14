package com.filesrest.rest;

import com.filesrest.model.UserEntity;
import com.filesrest.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRestControllerV1 {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR') ")
    public ResponseEntity<?> getAll() throws JsonProcessingException {
        List<UserEntity> users = userService.getAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getById(@PathVariable("id") int id) throws IOException {
        UserEntity user = userService.getById(id);
        if(Objects.isNull(user)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    protected ResponseEntity<?> create(@RequestBody UserEntity user) throws IOException {
         UserEntity userEntity= userService.create(user);
         return ResponseEntity.ok(userEntity);
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')  ")
    protected ResponseEntity<?> update(@RequestBody UserEntity user, @PathVariable("id") int id) throws IOException {
        user.setId(id);
        UserEntity userEntity =  userService.update(user);
        return ResponseEntity.ok(userEntity);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    protected void delete(@PathVariable("id") int id) {
        userService.deleteById(id);
    }
}