package com.appdev.cruquihi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.appdev.cruquihi.entity.TicketEntity;
import com.appdev.cruquihi.service.TicketService;

@RestController
@RequestMapping(path = "/api/ticket")
public class TicketController {

    @Autowired
    TicketService sticket;

    // CREATE
    @PostMapping("/add")
    public TicketEntity createTicket(@RequestBody TicketEntity ticket) {
        return sticket.createTicket(ticket);
    }

    // READ ALL
    @GetMapping("/all")
    public List<TicketEntity> getAllTickets() {
        return sticket.getAllTickets();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public TicketEntity getTicketById(@PathVariable Integer id) {
        return sticket.getTicketById(id).orElse(null);
    }

    // UPDATE
    @PutMapping("/update/{id}")
    public TicketEntity updateTicket(@PathVariable Integer id, @RequestBody TicketEntity updated) {
        return sticket.updateTicket(id, updated);
    }

    // DELETE
    @DeleteMapping("/delete/{id}")
    public String deleteTicket(@PathVariable Integer id) {
        return sticket.deleteTicket(id);
    }
}
