package com.filesrest.service;
import com.filesrest.config.S3Config;
import com.filesrest.model.EventEntity;
import com.filesrest.repository.EventRepository;
import com.filesrest.rest.FileRestControllerV1;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = {"aws.access-key=AK**",
        "aws.secret-key=y**",
        "baсket-path=https://s3.console.aws.amazon.com/s3/object/avvakumovailona2?region=us-west-2&prefi="

         } )

public class EventServiceTest {

    @Autowired
    public EventService eventService;

    @MockBean
    private EventRepository eventRepository;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAll() {
        eventService.getAll();
        verify(eventRepository, times(1)).findAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getById() {
        when(eventRepository.findById(Mockito.anyInt())).thenReturn(java.util.Optional.of(new EventEntity()));
        eventService.getById(1);
        verify(eventRepository, times(1)).findById(1);
    }
}