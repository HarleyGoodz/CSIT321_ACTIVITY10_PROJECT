package com.appdev.cruquihi.cruzquirantehisolerg6.service;

import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appdev.cruquihi.cruzquirantehisolerg6.entity.QrValidationEntity;
import com.appdev.cruquihi.cruzquirantehisolerg6.repository.QrValidationRepository;
 

@Service
public class QrValidationService {
 
    @Autowired
    QrValidationRepository qrValidationRepository;
 
    public QrValidationService(){
        super();
    }
 
    public QrValidationEntity postQrValidationRecord(QrValidationEntity qrValidationEntity){
        return qrValidationRepository.save(qrValidationEntity);
    }
 
    public List<QrValidationEntity> getAllQrValidations(){
        return qrValidationRepository.findAll();
    }
}
 