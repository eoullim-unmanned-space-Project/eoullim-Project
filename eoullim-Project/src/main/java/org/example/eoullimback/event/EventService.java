package org.example.eoullimback.event;

public interface EventService {

    EventResponse.DetailDTO createEvent(Long id);

    EventResponse.DetailDTO getDetailEvent(Long id);
}
