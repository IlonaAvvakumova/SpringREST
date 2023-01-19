
package com.filesrest.service;
import com.filesrest.model.EventEntity;
import com.filesrest.model.FileEntity;
import com.filesrest.model.UserEntity;
import com.filesrest.repository.FileRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = {"aws.access-key=**",
        "aws.secret-key=yQ**",
        "baсket-path=https://s3.console.aws.amazon.com/s3/object/avvakumovailona2?region=us-west-2&prefi="
})
class FileServiceTest {

    @Autowired
    FileService fileService;

    @MockBean
    FileRepository fileRepository;

    FileEntity fileEntity2  ;

    UserEntity userEntity ;

    @MockBean
    S3Service s3Service;

    @Value("${baсket-path}")
    private  String s3BaketPath ;

    EventEntity eventEntity ;

    @Autowired
    private WebApplicationContext webApplicationContext;
    MockMultipartFile multipartFile
            = new MockMultipartFile(
            "file",
            "hello.txt",
            MediaType.TEXT_PLAIN_VALUE,
            "Hello, World!".getBytes()
    );


    @Test
    @WithMockUser(roles = "ADMIN")
    void getAll() {
        fileService.getAll();
        verify(fileRepository, times(1)).findAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getById() {
        when(fileRepository.findById(Mockito.anyInt())).thenReturn(java.util.Optional.of(new FileEntity()));
        fileService.getById(1);
        verify(fileRepository, times(1)).findById(1);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void create() {
        fileEntity2= FileEntity.builder().name(multipartFile.getName())
                .filePath(s3BaketPath + multipartFile.getOriginalFilename())
                .build();
        when(fileRepository.save(Mockito.mock(FileEntity.class))).thenReturn(fileEntity2);
        fileService.create(multipartFile, userEntity);
        verify(fileRepository, times(1)).save(fileEntity2);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteById() {
        fileService.deleteById(1);
        verify(fileRepository, times(1)).deleteById(1);
    }
}
