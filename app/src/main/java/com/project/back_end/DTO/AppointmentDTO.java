package com.project.back_end.DTO;

import com.project.back_end.models.Appointment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class AppointmentDTO {
    private Long id;
    private Long doctorId;
    private String doctorName;
    private Long patientId;
    private String patientName;
    private String patientEmail;
    private String patientPhone;
    private String patientAddress;
    private LocalDateTime appointmentTime;
    private int status;

    private LocalDate appointmentDate;
    private LocalTime appointmentTimeOnly;
    private LocalDateTime endTime;

    // Constructor that initializes all core fields and computes derived fields
    public AppointmentDTO(
            Long id,
            Long doctorId,
            String doctorName,
            Long patientId,
            String patientName,
            String patientEmail,
            String patientPhone,
            String patientAddress,
            LocalDateTime appointmentTime,
            int status
    ) {
        this.id = id;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.patientId = patientId;
        this.patientName = patientName;
        this.patientEmail = patientEmail;
        this.patientPhone = patientPhone;
        this.patientAddress = patientAddress;
        this.appointmentTime = appointmentTime;
        this.status = status;

        // Compute derived fields
        this.appointmentDate = appointmentTime.toLocalDate();
        this.appointmentTimeOnly = appointmentTime.toLocalTime();
        this.endTime = appointmentTime.plusHours(1);
    }

    // Constructor that accepts an Appointment object
    public AppointmentDTO(Appointment appointment) {
        this.id = appointment.getId();
        this.doctorId = appointment.getDoctor().getId();
        this.doctorName = appointment.getDoctor().getName();
        this.patientId = appointment.getPatient().getId();
        this.patientName = appointment.getPatient().getName();
        this.patientEmail = appointment.getPatient().getEmail();
        this.patientPhone = appointment.getPatient().getPhone();
        this.patientAddress = appointment.getPatient().getAddress();
        this.appointmentTime = appointment.getAppointmentTime();
        this.status = appointment.getStatus();

        // Derived fields
        this.appointmentDate = appointmentTime.toLocalDate();
        this.appointmentTimeOnly = appointmentTime.toLocalTime();
        this.endTime = appointmentTime.plusHours(1);
    }

}
