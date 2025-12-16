package org.example.eoullimback.timeslot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class TimeSlotController {
    // 생성
    @GetMapping("/room/{roomId}/time-slot/save")
    public String saveForm(@PathVariable Long roomId) {

        return "timeslot/save";
    }

    @PostMapping("/room/{roomId}/time-slot/save")
    public String saveProc(@PathVariable Long roomId) {

        return "redirect:/room/detail";
    }

    // 목록 조회
    @GetMapping("/room/{roomId}/time-slot/")
    public String ListForm(@PathVariable Long roomId) {

        return "room/detail";
    }

    // 단건 조회
    @GetMapping("/time-slot/{timeSlotId}")
    public String DetailForm(@PathVariable Long timeSlotId) {

        return "timeslot/detail";
    }

    // 수정
    @GetMapping("/time-slot/{timeSlotId}/update")
    public String updateForm(@PathVariable Long timeSlotId) {

        return "timeslot/update";
    }

    @PostMapping("/time-slot/{timeSlotId}/update")
    public String updateProc(@PathVariable Long timeSlotId) {

        return "redirect:/timeslot/" + timeSlotId;
    }

    // 삭제
    @PostMapping("/time-slot/{timeSlotId}/delete")
    public String deleteProc(@PathVariable Long timeSlotId) {

        return "redirect:/room/detail";
    }
}
