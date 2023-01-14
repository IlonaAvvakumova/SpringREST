package com.filesrest.repository;
import com.filesrest.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface FileRepository extends JpaRepository<FileEntity, Integer> {

}
