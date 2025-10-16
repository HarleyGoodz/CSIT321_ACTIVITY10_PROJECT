package com.appdev.cruquihi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.appdev.cruquihi.entity.TicketEntity;
import com.appdev.cruquihi.repository.TicketRepository;

@RestController
@RequestMapping(method = RequestMethod.GET, path = "/api/ticket")
@CrossOrigin
public class TicketController {

    private final TicketRepository ticketRepo;

    public TicketController(TicketRepository ticketRepo) {
        this.ticketRepo = ticketRepo;
    }

    // CREATE
    @PostMapping("/add")
    public TicketEntity createTicket(@RequestBody TicketEntity ticket) {
        return ticketRepo.save(ticket);
    }

    // READ ALL
    @GetMapping("/all")
    public List<TicketEntity> getAllTickets() {
        return ticketRepo.findAll();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public TicketEntity getTicketById(@PathVariable Integer id) {
        return ticketRepo.findById(id).orElse(null);
    }

    // UPDATE
    @PutMapping("/update/{id}")
    public TicketEntity updateTicket(@PathVariable Integer id, @RequestBody TicketEntity updated) {
        return ticketRepo.findById(id).map(t -> {
            t.setEvent(updated.getEvent());
            t.setTicketPrice(updated.getTicketPrice());
            t.setTicketType(updated.getTicketType());
            t.setAvailability(updated.getAvailability());
            return ticketRepo.save(t);
        }).orElse(null);
    }

    // DELETE
    @DeleteMapping("/delete/{id}")
    public String deleteTicket(@PathVariable Integer id) {
        ticketRepo.deleteById(id);
        return "Ticket deleted successfully.";
    }
}
