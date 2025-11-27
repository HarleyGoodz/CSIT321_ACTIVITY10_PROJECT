package com.appdev.cruquihi.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appdev.cruquihi.entity.QrValidationEntity;
import com.appdev.cruquihi.repository.QrValidationRepository;

@Service
public class QrValidationService {

    @Autowired
    QrValidationRepository qrValidationRepository;

    public QrValidationService() {
        super();
    }

    // CREATE
    public QrValidationEntity postQrValidationRecord(QrValidationEntity qrValidationEntity) {
        return qrValidationRepository.save(qrValidationEntity);
    }

    // READ ALL
    public List<QrValidationEntity> getAllQrValidations() {
        return qrValidationRepository.findAll();
    }

    // READ BY ID
    public Optional<QrValidationEntity> getValidationById(Integer id) {
        return qrValidationRepository.findById(id);
    }

    // UPDATE
    public QrValidationEntity updateValidation(Integer id, QrValidationEntity newDetails) {
        QrValidationEntity validation;

        try {
            validation = qrValidationRepository.findById(id).get();

            validation.setQr_code(newDetails.getQr_code());
            validation.setValidation_status(newDetails.getValidation_status());
            validation.setValidated_by(newDetails.getValidated_by());
            validation.setGenerated_at(newDetails.getGenerated_at());
            validation.setUsed_at(newDetails.getUsed_at());

        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("QR validation with ID " + id + " not found.");
        }

        return qrValidationRepository.save(validation);
    }

    // DELETE
    public String deleteValidation(Integer id) {
        if (qrValidationRepository.findById(id).isPresent()) {
            qrValidationRepository.deleteById(id);
            return "QR validation with ID " + id + " deleted.";
        } else {
            return "QR validation with ID " + id + " not found.";
        }
    }
}
