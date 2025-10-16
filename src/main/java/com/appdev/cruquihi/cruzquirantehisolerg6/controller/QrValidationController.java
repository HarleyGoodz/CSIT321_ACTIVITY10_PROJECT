package com.appdev.cruquihi.cruzquirantehisolerg6.controller;
 
import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appdev.cruquihi.cruzquirantehisolerg6.entity.QrValidationEntity;
import com.appdev.cruquihi.cruzquirantehisolerg6.service.QrValidationService;

import org.springframework.web.bind.annotation.RequestMethod;
 
 
@RestController
@RequestMapping(method = RequestMethod.GET,path="/api/qrvalidation")
public class QrValidationController {
 
    @Autowired
    QrValidationService qrValidationService;
 
    @GetMapping("/print")
    public String print() {
        return "This is your QR validation!";
    }
 
    @PostMapping("/validation_record")
    public QrValidationEntity postQrValidationRecord(@RequestBody QrValidationEntity qrValidationEntity) {
        return qrValidationService.postQrValidationRecord(qrValidationEntity);
    }
 
    @GetMapping("/getAllQrValidations")
    public List<QrValidationEntity> getAllQrValidations() {
        return qrValidationService.getAllQrValidations();
    }
   
 
 
 
}
