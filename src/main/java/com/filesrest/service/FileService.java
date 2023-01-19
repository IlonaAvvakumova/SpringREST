package com.filesrest.service;

import com.filesrest.model.EventEntity;
import com.filesrest.model.FileEntity;
import com.filesrest.model.UserEntity;
import com.filesrest.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRep;

    @Value("${baсket-path}")
    private String s3BaketPath;

    @Autowired
    private final S3Service s3Service;


    public List<FileEntity> getAll() {
           log.info("In method getAll() FileService");
        return fileRep.findAll();
    }


    public FileEntity getById(Integer id) {
        Optional<FileEntity> optional = fileRep.findById(id);
           log.info("FileService с id " + id);
        return optional.orElse(null);
    }


    public FileEntity create(MultipartFile multipartFile, UserEntity userEntity) {
        s3Service.uploadFile(multipartFile);
          log.info("File upload" );
        FileEntity fileEntity = FileEntity.builder().name(multipartFile.getName())
                .filePath(s3BaketPath + multipartFile.getOriginalFilename())
                .build();
        FileEntity createdFile = fileRep.save(fileEntity);
        EventEntity.builder()
                .user(userEntity)
                .fileEntity(createdFile)
                .build();
           log.info("File created in BD" );
        return createdFile;
    }


    public void deleteById(Integer id) {
         log.info("File deleted" );
        fileRep.deleteById(id);
    }
}
