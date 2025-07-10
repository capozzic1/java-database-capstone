package com.project.back_end.DTO;

import com.project.back_end.models.Prescription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// DTO class for Prescription – used to safely expose data in API responses
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDTO {

    private String id;
    private String patientName;
    private Long appointmentId;
    private String medication;
    private String dosage;
    private String doctorNotes;

    // Constructor to map from Prescription entity
    public PrescriptionDTO(Prescription prescription) {
        this.id = prescription.getId();
        this.patientName = prescription.getPatientName();
        this.appointmentId = prescription.getAppointmentId();
        this.medication = prescription.getMedication();
        this.dosage = prescription.getDosage();
        this.doctorNotes = prescription.getDoctorNotes();
    }
}
