package org.example.eoullimback.timeslot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseTimeSlotService {

    private final TimeSlotRepository timeSlotRepository;

    private final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    public SseEmitter createConnection(Long userId, Long roomId) {

        String key = userId + "_" + roomId;
        SseEmitter emitter = new SseEmitter(60L * 60 * 1000);

        emitterMap.put(key, emitter);

        log.info("SSE TimeSlots 연결 추가 : {}", key);

        emitter.onCompletion(() -> emitterMap.remove(key));
        emitter.onTimeout(() -> emitterMap.remove(key));
        emitter.onError((e) -> emitterMap.remove(key));

        try {

            emitter.send(SseEmitter.event().name("connect").data("연결되었습니다."));

        } catch (Exception e) {
            log.error("전송 실패 : ", e);
        }

        return emitter;
    }

    public void timeSlotBroadcast(Long roomId, List<TimeSlot> timeSlotsIds) {

            emitterMap.forEach((key, emitter) -> {
                if (key.endsWith("_" + roomId)) {
                    List<TimeSlotResponse.DetailDTO> timeSlots = timeSlotsIds.stream()
                            .map(TimeSlotResponse.DetailDTO::new)
                            .toList();

                    try  {
                        emitter.send(SseEmitter.event()
                                .name("timeSlotUpdate")
                                .data(timeSlots)
                        );
                    }  catch (IOException e) {
                        emitterMap.remove(key);
                    }
                }
            });
    }

}
