package com.filesrest.service;

import com.filesrest.model.EventEntity;
import com.filesrest.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRep;

    public List<EventEntity> getAll() {
      log.info("In method getAll() EventService");
        return eventRep.findAll();
    }

    public EventEntity getById(Integer id) {
        Optional<EventEntity> optional = eventRep.findById(id);
        log.info("EventEntity с id " + id);
        return optional.orElse(null);
    }
}
