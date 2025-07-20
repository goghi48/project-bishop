package t1.homework.controller;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import t1.homework.Command;
import t1.homework.audit.WeylandWatchingYou;
import t1.homework.service.CommandQueueService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/commands")
public class PrototypeController {
    
    private final CommandQueueService commandService;
    
    @WeylandWatchingYou
    @PostMapping
    public ResponseEntity<String> addCommand(@RequestBody @Valid Command command) {
        commandService.processCommand(command);
        return ResponseEntity.accepted().body("Команда принята");
    }

    @WeylandWatchingYou
    @GetMapping("/status")
    public String status() {
        return "Active";
    }
}