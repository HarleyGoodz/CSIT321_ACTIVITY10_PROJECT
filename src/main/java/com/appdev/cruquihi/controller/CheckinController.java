package com.appdev.cruquihi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.appdev.cruquihi.entity.CheckinEntity;
import com.appdev.cruquihi.repository.CheckinRepository;

@RestController
@RequestMapping(method = RequestMethod.GET, path = "/api/checkin")
@CrossOrigin
public class CheckinController {

    private final CheckinRepository checkinRepo;

    public CheckinController(CheckinRepository checkinRepo) {
        this.checkinRepo = checkinRepo;
    }

    // CREATE
    @PostMapping("/add")
    public CheckinEntity createCheckin(@RequestBody CheckinEntity checkin) {
        return checkinRepo.save(checkin);
    }

    // READ ALL
    @GetMapping("/all")
    public List<CheckinEntity> getAllCheckins() {
        return checkinRepo.findAll();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public CheckinEntity getCheckinById(@PathVariable Integer id) {
        return checkinRepo.findById(id).orElse(null);
    }

    // UPDATE
    @PutMapping("/update/{id}")
    public CheckinEntity updateCheckin(@PathVariable Integer id, @RequestBody CheckinEntity updated) {
        return checkinRepo.findById(id).map(c -> {
            c.setUser(updated.getUser());
            c.setEvent(updated.getEvent());
            c.setCheckinDate(updated.getCheckinDate());
            c.setCheckinStatus(updated.getCheckinStatus());
            return checkinRepo.save(c);
        }).orElse(null);
    }

    // DELETE
    @DeleteMapping("/delete/{id}")
    public String deleteCheckin(@PathVariable Integer id) {
        checkinRepo.deleteById(id);
        return "Checkin deleted successfully.";
    }
}
