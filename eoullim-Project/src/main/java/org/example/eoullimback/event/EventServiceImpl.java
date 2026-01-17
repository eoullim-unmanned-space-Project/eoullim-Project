package org.example.eoullimback.event;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception404;
import org.example.eoullimback.geminichatbot.GeminiServiceImpl;
import org.example.eoullimback.user_auth.user.User;
import org.example.eoullimback.user_auth.user.UserRepository;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService{

    private final EventRepository eventRepository;
    private final GeminiServiceImpl geminiService;
    private final UserRepository userRepository;

    @Override
    public EventResponse.DetailDTO createFortune(Long id) {
        User userEntity =  userRepository.findById(id)
             .orElseThrow(() ->  new Exception404(ErrorCode.USER_NOT_FOUND));
        EventResponse.FortuneResultDTO resultDTO = geminiService.createForture(userEntity.getName());

        Event event = Event.builder()
            .user(userEntity)
            .luckyScore(resultDTO.getLuckyScore())
            .luckyItem(resultDTO.getLuckyItem())
            .content(resultDTO.getContent())
            .build();

        eventRepository.save(event);

        return new EventResponse.DetailDTO(event);
    }
}
