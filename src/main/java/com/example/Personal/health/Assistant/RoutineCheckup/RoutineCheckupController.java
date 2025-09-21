package com.example.Personal.health.Assistant.RoutineCheckup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checkups")
@CrossOrigin(origins = "*")
public class RoutineCheckupController {

    @Autowired
    private RoutineCheckupRepository repository;

    @GetMapping
    public List<RoutineCheckup> getAllCheckups() {
        return repository.findAll();
    }

    @PostMapping
    public RoutineCheckup saveCheckup(@RequestBody RoutineCheckup checkup) {
        return repository.save(checkup);
    }
    @DeleteMapping("/{id}")
    public String deleteCheckup(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "Checkup with ID " + id + " deleted successfully!";
        } else {
            return "Checkup with ID " + id + " not found!";
        }
    }

}