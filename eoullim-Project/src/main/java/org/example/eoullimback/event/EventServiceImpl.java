package org.example.eoullimback.event;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception400;
import org.example.eoullimback._common.error.exception.Exception404;
import org.example.eoullimback.geminichatbot.GeminiServiceImpl;
import org.example.eoullimback.user_auth.user.User;
import org.example.eoullimback.user_auth.user.UserRepository;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService{

    private final EventRepository eventRepository;
    private final GeminiServiceImpl geminiService;
    private final UserRepository userRepository;

    @Override
    public EventResponse.DetailDTO getDetailEvent(Long id) {
        User userEntity =  userRepository.findById(id)
                .orElseThrow(() ->  new Exception404(ErrorCode.USER_NOT_FOUND));

        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        Optional<Event> eventEntity = eventRepository.findByUserIdAndCreatedAtBetween(userEntity.getId(), start, end);

        return eventEntity.map(EventResponse.DetailDTO::new)
                .orElse(new EventResponse.DetailDTO(false));
    }

    @Override
    @Transactional
    public EventResponse.DetailDTO createEvent(Long id) {
        User userEntity =  userRepository.findById(id)
             .orElseThrow(() ->  new Exception404(ErrorCode.USER_NOT_FOUND));

        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        if(!eventRepository.existsByUserIdAndCreatedAtBetween(userEntity.getId(), start, end)){
           throw new Exception400(ErrorCode.EVENT_ALREADY_CREATED);
        }
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
