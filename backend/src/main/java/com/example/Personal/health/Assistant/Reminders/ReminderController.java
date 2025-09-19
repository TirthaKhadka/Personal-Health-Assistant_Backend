package com.example.Personal.health.Assistant.Reminders;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reminders")
public class ReminderController {

    private final ReminderRepository repo;

    public ReminderController(ReminderRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Reminder createReminder(@RequestBody Reminder reminder) {
        return repo.save(reminder);
    }

    @GetMapping
    public List<Reminder> getAllReminders() {
        return repo.findAll();
    }

    @PutMapping("/{id}/complete")
    public Reminder markComplete(@PathVariable Long id) {
        Reminder r = repo.findById(id).orElseThrow();
        r.setCompleted(true);
        return repo.save(r);
    }
}
