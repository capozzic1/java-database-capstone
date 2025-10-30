package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.UtilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("${api.path}appointments")
public class AppointmentController {

// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to define it as a REST API controller.
//    - Use `@RequestMapping("/appointments")` to set a base path for all appointment-related endpoints.
//    - This centralizes all routes that deal with booking, updating, retrieving, and canceling appointments.


    // 2. Autowire Dependencies:
//    - Inject `AppointmentService` for handling the business logic specific to appointments.
//    - Inject the general `Service` class, which provides shared functionality like token validation and appointment checks.
    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UtilityService utilityService;

    // 3. Define the `getAppointments` Method:
//    - Handles HTTP GET requests to fetch appointments based on date and patient name.
//    - Takes the appointment date, patient name, and token as path variables.
//    - First validates the token for role `"doctor"` using the `Service`.
//    - If the token is valid, returns appointments for the given patient on the specified date.
//    - If the token is invalid or expired, responds with the appropriate message and status code.
    @GetMapping("/{date}/{patientName}/{token}")
    public ResponseEntity<?> getAppointments(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @PathVariable String patientName, @PathVariable String token) {
        try {
            Map<String, String> tokenMap = utilityService.getTokenValidationResponse(token);
            Map<String, String> resp = new HashMap<>();
            if (tokenMap.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
            }
            Map<String, Object> appt = appointmentService.getAppointment(date, patientName, token);
            return ResponseEntity.ok(appt);
        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please provide a valid date");
        }
    }

// 4. Define the `bookAppointment` Method:
//    - Handles HTTP POST requests to create a new appointment.
//    - Accepts a validated `Appointment` object in the request body and a token as a path variable.
//    - Validates the token for the `"patient"` role.
//    - Uses service logic to validate the appointment data (e.g., check for doctor availability and time conflicts).
//    - Returns success if booked, or appropriate error messages if the doctor ID is invalid or the slot is already taken.
    @PostMapping("/{token}")
    public ResponseEntity<?> bookAppointment(@RequestBody Appointment appt, @PathVariable String token) {
        Map<String, String> tokenMap = utilityService.getTokenValidationResponse(token);
        Map<String, String> resp = new HashMap<>();
        if (!tokenMap.isEmpty() && utilityService.validateAppointment(appt) == 1) {
               int result = appointmentService.bookAppointment(appt);
               if (result == 1) {
                   resp.put("message", "The appointment has been booked.");
                   return ResponseEntity.ok(resp);
               } else {
                   resp.put("error", "There was an issue saving the appointment");
                   return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
               }
        } else {
            resp.put("error", "There was an issue saving the appointment");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);

        }
    }

// 5. Define the `updateAppointment` Method:
//    - Handles HTTP PUT requests to modify an existing appointment.
//    - Accepts a validated `Appointment` object and a token as input.
//    - Validates the token for `"patient"` role.
//    - Delegates the update logic to the `AppointmentService`.
//    - Returns an appropriate success or failure response based on the update result.
    @PutMapping("/{token}")
    public ResponseEntity<?> updateAppointment(@RequestBody Appointment appt, @PathVariable String token) {
        Map<String, String> tokenMap = utilityService.getTokenValidationResponse(token);
        if (!tokenMap.isEmpty()) {
           return appointmentService.updateAppointment(appt);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There's no token");
        }
    }

// 6. Define the `cancelAppointment` Method:
//    - Handles HTTP DELETE requests to cancel a specific appointment.
//    - Accepts the appointment ID and a token as path variables.
//    - Validates the token for `"patient"` role to ensure the user is authorized to cancel the appointment.
//    - Calls `AppointmentService` to handle the cancellation process and returns the result.
    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long id, @PathVariable String token) {
        Map<String, String> tokenMap = utilityService.getTokenValidationResponse(token);
        if (!tokenMap.isEmpty()) {
            return appointmentService.cancelAppointment(id);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There's no token");
        }
    }

}
