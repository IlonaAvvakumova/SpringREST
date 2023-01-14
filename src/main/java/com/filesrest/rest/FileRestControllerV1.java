package com.filesrest.rest;

import com.filesrest.model.EventEntity;
import com.filesrest.model.FileEntity;
import com.filesrest.model.UserEntity;
import com.filesrest.service.FileService;
import com.filesrest.service.S3Service;
import com.filesrest.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/api/v1/files")
//@RequiredArgsConstructor

public class FileRestControllerV1 {

    private final FileService fileService;

    private final UserService userService;

    private final S3Service s3Service;
@Autowired
    public FileRestControllerV1(FileService fileService, UserService userService, S3Service s3Service) {

        this.userService = userService;
        this.s3Service = s3Service;
        this.fileService = fileService;
    }
//    @PostMapping("/uploadFile")
//    @Transactional
//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
//    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
//        s3Service.uploadFile(file);
//        return ResponseEntity.ok().body("file received successfully");
//    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    public ResponseEntity<?> getAll() throws JsonProcessingException {
        List<FileEntity> files = fileService.getAll();
        return ResponseEntity.ok(files);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    public ResponseEntity<?> getById(@PathVariable("id") int id) throws IOException {
        FileEntity file = fileService.getById(id);
        if(Objects.isNull(file)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(file);
    }

    @PostMapping("/create")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    protected ResponseEntity<?> create( @RequestParam("file") MultipartFile multipartFile, @RequestBody FileEntity fileEntity, @RequestHeader(value = "user_id") int id) {

        UserEntity user = userService.getById(id);
        if(Objects.isNull(user) ){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if( Objects.isNull(fileEntity)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
//        List<EventEntity> eventEntities = new ArrayList<>();
//        EventEntity event = new EventEntity();
//        event.setFileEntity(fileEntity);
//        event.setUser(user);
//        eventEntities.add(event);
//        user.setEventEntities(eventEntities);
        userService.update(user);
        fileService.create(multipartFile, user);
        return ResponseEntity.ok(fileEntity);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    protected void delete(@PathVariable("id") int id) {
        fileService.deleteById(id);
    }


}