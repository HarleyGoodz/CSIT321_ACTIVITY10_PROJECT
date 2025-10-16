package com.appdev.cruquihi.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appdev.cruquihi.entity.CheckinEntity;
import com.appdev.cruquihi.repository.CheckinRepository;

@Service
public class CheckinService {

    @Autowired
    CheckinRepository crepo;

    public CheckinService() {
        super();
    }

    public CheckinEntity createCheckin(CheckinEntity c) {
        return crepo.save(c);
    }

    public List<CheckinEntity> getAllCheckins() {
        return crepo.findAll();
    }

    public Optional<CheckinEntity> getCheckinById(Integer id) {
        return crepo.findById(id);
    }

    public CheckinEntity updateCheckin(Integer id, CheckinEntity newDetails) {
        CheckinEntity c = new CheckinEntity();
        try {
            c = crepo.findById(id).get();
            c.setUser(newDetails.getUser());
            c.setEvent(newDetails.getEvent());
            c.setCheckinDate(newDetails.getCheckinDate());
            c.setCheckinStatus(newDetails.getCheckinStatus());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Checkin with ID " + id + " not found.");
        } finally {
            return crepo.save(c);
        }
    }

    public String deleteCheckin(Integer id) {
        String msg = "";
        if (crepo.findById(id).isPresent()) {
            crepo.deleteById(id);
            msg = "Checkin with ID " + id + " has been deleted.";
        } else {
            msg = "Checkin with ID " + id + " not found.";
        }
        return msg;
    }
}
