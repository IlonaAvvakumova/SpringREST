package com.filesrest.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 s3client;

    public void createBucket() {
        String bucketName = "avvakumovailona2";
        if (s3client.doesBucketExistV2(bucketName)) {
             log.info("Bucket {} already exists, use a different name", bucketName);
            return;
        }
        s3client.createBucket(bucketName);
    }

    public void listBuckets() {
        List<Bucket> buckets = s3client.listBuckets();
            log.info("buckets: {}", buckets);
    }

  @SneakyThrows
  public void uploadFile(MultipartFile multipartFile) {
      String bucketName = "avvakumovailona2";
      ClassLoader loader = S3Service.class.getClassLoader();
      File file = convertMultiPartFileToFile(multipartFile);
      s3client.putObject(
              bucketName,
              multipartFile.getOriginalFilename(),
              file);
  }
    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            System.out.println(e);
        }
        return convertedFile;
    }
    public void listFiles() {
        String bucketName = "avvakumovailona2";
        ObjectListing objects = s3client.listObjects(bucketName);
        for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
                log.info("File name: {}", objectSummary.getKey());
        }
    }

    @SneakyThrows
    public void downloadFile() {
        String bucketName = "avvakumovailona2";
        S3Object s3object = s3client.getObject(bucketName, "oldFile.txt");
        S3ObjectInputStream inputStream = s3object.getObjectContent();
        File file = new File("src/main/resources/newFile.txt");
        try {
            FileCopyUtils.copy(inputStream, new FileOutputStream(file));
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
