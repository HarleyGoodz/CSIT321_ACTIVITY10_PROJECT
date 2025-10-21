package com.appdev.cruquihi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.appdev.cruquihi.entity.CheckinEntity;
import com.appdev.cruquihi.service.CheckinService;

@RestController
@RequestMapping(path = "/api/checkin")
public class CheckinController {

    private final CheckinService scheckin;

    public CheckinController(CheckinService scheckin) {
        this.scheckin = scheckin;
    }

    // CREATE
    @PostMapping("/add")
    public CheckinEntity createCheckin(@RequestBody CheckinEntity checkin) {
        return scheckin.createCheckin(checkin);
    }

    // READ ALL
    @GetMapping("/all")
    public List<CheckinEntity> getAllCheckins() {
        return scheckin.getAllCheckins();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public CheckinEntity getCheckinById(@PathVariable Integer id) {
        return scheckin.getCheckinById(id).orElse(null);
    }

    // UPDATE
    @PutMapping("/update/{id}")
    public CheckinEntity updateCheckin(@PathVariable Integer id, @RequestBody CheckinEntity updated) {
        return scheckin.updateCheckin(id, updated);
    }

    // DELETE
    @DeleteMapping("/delete/{id}")
    public String deleteCheckin(@PathVariable Integer id) {
        return scheckin.deleteCheckin(id);
    }
}
