package com.appdev.cruquihi.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.appdev.cruquihi.entity.QrValidationEntity;
import com.appdev.cruquihi.service.QrValidationService;

@RestController
@RequestMapping(method = RequestMethod.GET, path = "/api/qrvalidation")
@CrossOrigin(origins = "*") // optional but recommended
public class QrValidationController {

    @Autowired
    QrValidationService qrValidationService;

    // 🔵 TEST ROUTE
    @GetMapping("/print")
    public String print() {
        return "This is your QR validation!";
    }

    // 🟢 CREATE
    @PostMapping("/add")
    public QrValidationEntity addValidation(@RequestBody QrValidationEntity qrValidationEntity) {
        return qrValidationService.postQrValidationRecord(qrValidationEntity);
    }

    // 🟡 READ ALL
    @GetMapping("/get-all")
    public List<QrValidationEntity> getAllQrValidations() {
        return qrValidationService.getAllQrValidations();
    }

    // 🟡 READ BY ID
    @GetMapping("/get/{id}")
    public Optional<QrValidationEntity> getValidationById(@PathVariable Integer id) {
        return qrValidationService.getValidationById(id);
    }

    // 🟠 UPDATE
    @PutMapping("/update/{id}")
    public QrValidationEntity updateValidation(
            @PathVariable Integer id,
            @RequestBody QrValidationEntity newDetails) {

        return qrValidationService.updateValidation(id, newDetails);
    }

    // 🔴 DELETE
    @DeleteMapping("/delete/{id}")
    public String deleteValidation(@PathVariable Integer id) {
        return qrValidationService.deleteValidation(id);
    }
}
