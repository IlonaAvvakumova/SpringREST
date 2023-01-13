package com.filesrest.service;

import com.filesrest.model.EventEntity;
import com.filesrest.model.FileEntity;
import com.filesrest.model.UserEntity;
import com.filesrest.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRep;

    @Value("${basket.path}")
    private final String s3BaketPath;

    private final S3Service s3Service;

    public List<FileEntity> getAll() {
        return fileRep.findAll();
    }


    public FileEntity getById(Integer id) {
        Optional<FileEntity> optional = fileRep.findById(id);
        return optional.orElse(null);
    }


    public FileEntity create(MultipartFile multipartFile, UserEntity userEntity) {
        s3Service.uploadFile(multipartFile);
        FileEntity fileEntity = FileEntity.builder().name(multipartFile.getName())
                .filePath(s3BaketPath + multipartFile.getOriginalFilename())
                .build();
        FileEntity createdFile = fileRep.save(fileEntity);
        EventEntity eventEntity = EventEntity.builder()
                .user(userEntity)
                .fileEntity(createdFile)
                .build();
        return createdFile;
    }


    public void deleteById(Integer id) {
        fileRep.deleteById(id);
    }
}