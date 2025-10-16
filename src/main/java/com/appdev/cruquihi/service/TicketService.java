package com.appdev.cruquihi.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appdev.cruquihi.entity.TicketEntity;
import com.appdev.cruquihi.repository.TicketRepository;

@Service
public class TicketService {

    @Autowired
    TicketRepository trepo;

    public TicketService() {
        super();
    }

    public TicketEntity createTicket(TicketEntity ticket) {
        return trepo.save(ticket);
    }

    public List<TicketEntity> getAllTickets() {
        return trepo.findAll();
    }

    public Optional<TicketEntity> getTicketById(Integer id) {
        return trepo.findById(id);
    }

    public TicketEntity updateTicket(Integer id, TicketEntity newDetails) {
        TicketEntity t = new TicketEntity();
        try {
            t = trepo.findById(id).get();
            t.setEvent(newDetails.getEvent());
            t.setTicketPrice(newDetails.getTicketPrice());
            t.setTicketType(newDetails.getTicketType());
            t.setAvailability(newDetails.getAvailability());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Ticket with ID " + id + " not found.");
        } finally {
            return trepo.save(t);
        }
    }

    public String deleteTicket(Integer id) {
        String msg = "";
        if (trepo.findById(id).isPresent()) {
            trepo.deleteById(id);
            msg = "Ticket with ID " + id + " has been deleted.";
        } else {
            msg = "Ticket with ID " + id + " not found.";
        }
        return msg;
    }
}
