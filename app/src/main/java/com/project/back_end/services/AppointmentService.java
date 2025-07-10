package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AppointmentService {
    // 1. **Add @Service Annotation**:
//    - To indicate that this class is a service layer class for handling business logic.
//    - The `@Service` annotation should be added before the class declaration to mark it as a Spring service component.
//    - Instruction: Add `@Service` above the class definition.
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final TokenService tokenService;
    private final UtilityService utilityService;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository,
            TokenService tokenService,
            UtilityService utilityService
    ) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.tokenService = tokenService;
        this.utilityService = utilityService;
    }
// 2. **Constructor Injection for Dependencies**:
//    - The `AppointmentService` class requires several dependencies like `AppointmentRepository`, `Service`, `TokenService`, `PatientRepository`, and `DoctorRepository`.
//    - These dependencies should be injected through the constructor.
//    - Instruction: Ensure constructor injection is used for proper dependency management in Spring.

// 3. **Add @Transactional Annotation for Methods that Modify Database**:
//    - The methods that modify or update the database should be annotated with `@Transactional` to ensure atomicity and consistency of the operations.
//    - Instruction: Add the `@Transactional` annotation above methods that interact with the database, especially those modifying data.

    // 4. **Book Appointment Method**:
//    - Responsible for saving the new appointment to the database.
//    - If the save operation fails, it returns `0`; otherwise, it returns `1`.
//    - Instruction: Ensure that the method handles any exceptions and returns an appropriate result code.
    @Transactional
    public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    // 5. **Update Appointment Method**:
//    - This method is used to update an existing appointment based on its ID.
//    - It validates whether the patient ID matches, checks if the appointment is available for updating, and ensures that the doctor is available at the specified time.
//    - If the update is successful, it saves the appointment; otherwise, it returns an appropriate error message.
//    - Instruction: Ensure proper validation and error handling is included for appointment updates.
    @Transactional
    public ResponseEntity<Map<String, String>> updateAppointment(Appointment appointment) {
        Map<String, String> response = new HashMap<>();

        Optional<Appointment> appt = appointmentRepository.findById(appointment.getId());
        if (appt.isPresent() && utilityService.validateAppointment(appointment) == 1) {
            appointmentRepository.save(appointment);
            response.put("message", "update successful");
            return ResponseEntity.ok(response);
        } else if (utilityService.validateAppointment(appointment) != 1) {
            response.put("error", "please choose a different time or a different doctor");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else {
            response.put("error", "there was an error updating the appointment.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 6. **Cancel Appointment Method**:
//    - This method cancels an appointment by deleting it from the database.
//    - It ensures the patient who owns the appointment is trying to cancel it and handles possible errors.
//    - Instruction: Make sure that the method checks for the patient ID match before deleting the appointment.
    @Transactional
    public ResponseEntity<Map<String, String>> cancelAppointment(Long id) {
        //response map
        //opt appt findbyid
        //if appt is present
        //delete with repo
        //pop map with message and its successful
        //return response entity ok
        // else
        //pop map with message, no appointment was found
        //return response entity status with bad request
        Map<String, String> response = new HashMap<>();
        Optional<Appointment> appt = appointmentRepository.findById(id);
        if (appt.isPresent()) {
            appointmentRepository.delete(appt.get());
            response.put("message", "the appointment has been cancelled/deleted");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "No appointment has been found.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
// 7. **Get Appointments Method**:
//    - This method retrieves a list of appointments for a specific doctor on a particular day, optionally filtered by the patient's name.
//    - It uses `@Transactional` to ensure that database operations are consistent and handled in a single transaction.
//    - Instruction: Ensure the correct use of transaction boundaries, especially when querying the database for appointments.
    //todo double check this method.
@Transactional
public Map<String, Object> getAppointment(LocalDate date, String token) {
    Map<String, Object> response = new HashMap<>();
    String email = tokenService.extractEmail(token);
    Doctor doc = doctorRepository.findByEmail(email);
    // Step 1: Validate input
    if (doc.getId() == null || date == null) {
        response.put("message", "Doctor ID and date are required.");
        return response;
    }

    // Step 2: Create time range for the specified date
    LocalDateTime start = date.atStartOfDay();              // 00:00:00
    LocalDateTime end = date.atTime(23, 59, 59);            // 23:59:59

    try {
        // Step 3: Query appointments for that doctor and date
        List<Appointment> appointments = appointmentRepository
                .findByDoctorIdAndAppointmentTimeBetween(doc.getId(), start, end);

        // Step 4: Prepare the response
        response.put("message", "Appointments retrieved successfully.");
        response.put("appointments", appointments);
    } catch (Exception e) {
        response.put("message", "Error retrieving appointments: " + e.getMessage());
    }

    return response;
}

// 8. **Change Status Method**:
//    - This method updates the status of an appointment by changing its value in the database.
//    - It should be annotated with `@Transactional` to ensure the operation is executed in a single transaction.
//    - Instruction: Add `@Transactional` before this method to ensure atomicity when updating appointment status.

    @Transactional
    public ResponseEntity<Map<String, String>> changeStatus(Long appointmentId, int status) {
        Map<String, String> response = new HashMap<>();

        Optional<Appointment> appt = appointmentRepository.findById(appointmentId);
        if (appt.isPresent()) {
            try {
                appointmentRepository.updateStatus(status, appointmentId);
                response.put("message", "Appointment status updated successfully.");
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                response.put("error", "Error updating appointment status: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } else {
            response.put("error", "Appointment not found.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

}
