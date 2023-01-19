package com.filesrest.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.filesrest.model.FileEntity;
import com.filesrest.model.UserEntity;
import com.filesrest.service.FileService;
import com.filesrest.service.S3Service;
import com.filesrest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileRestControllerV1 {

    private final FileService fileService;

    private final UserService userService;

    private final S3Service s3Service;


    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    public ResponseEntity<?> getAll()  {
        List<FileEntity> files = fileService.getAll();
        return ResponseEntity.ok(files);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    public ResponseEntity<?> getById(@PathVariable("id") int id) {
        FileEntity file = fileService.getById(id);
        if (Objects.isNull(file)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(file);
    }

    @PostMapping("/create")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    protected ResponseEntity<?> create(@RequestParam("file") MultipartFile multipartFile, @ModelAttribute FileEntity fileEntity, @RequestHeader(value = "user_id") int id) {
        UserEntity user = userService.getById(id);
        if (Objects.isNull(user)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (Objects.isNull(fileEntity)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userService.update(user);
        fileEntity = fileService.create(multipartFile, user);
        return ResponseEntity.ok(fileEntity);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    protected void delete(@PathVariable("id") int id) {
        fileService.deleteById(id);
    }
}