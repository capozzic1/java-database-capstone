package com.project.back_end.controllers;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Patient;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.UtilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/patient")
public class PatientController {

// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to define it as a REST API controller for patient-related operations.
//    - Use `@RequestMapping("/patient")` to prefix all endpoints with `/patient`, grouping all patient functionalities under a common route.

    @Autowired
    private PatientService patientService;

    @Autowired
    private UtilityService utilityService;
// 2. Autowire Dependencies:
//    - Inject `PatientService` to handle patient-specific logic such as creation, retrieval, and appointments.
//    - Inject the shared `Service` class for tasks like token validation and login authentication.


    // 3. Define the `getPatient` Method:
//    - Handles HTTP GET requests to retrieve patient details using a token.
//    - Validates the token for the `"patient"` role using the shared service.
//    - If the token is valid, returns patient information; otherwise, returns an appropriate error message.
    @GetMapping()
    public ResponseEntity<?> getPatient(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Map<String, String> tm = utilityService.getTokenValidationResponse(token);

        if (!tm.isEmpty()) {
            return patientService.getPatientDetails(token);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("missing or invalid token");
    }

    // 4. Define the `createPatient` Method:
//    - Handles HTTP POST requests for patient registration.
//    - Accepts a validated `Patient` object in the request body.
//    - First checks if the patient already exists using the shared service.
//    - If validation passes, attempts to create the patient and returns success or error messages based on the outcome.
    @PostMapping()
    public ResponseEntity<?> createPatient(@RequestBody Patient patient) {
        Map<String, String> resp = new HashMap<>();
        if (!utilityService.validatePatient(patient)) {
            resp.put("message", "patient already exists");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
        } else {
            int result = patientService.createPatient(patient);
            if (result == 1) {
                resp.put("message", "Sign up successful");
                return ResponseEntity.ok(resp);
            } else {
                resp.put("error", "An error occurred while saving the patient");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(resp);
            }
        }

    }

    // 5. Define the `login` Method:
//    - Handles HTTP POST requests for patient login.
//    - Accepts a `Login` DTO containing email/username and password.
//    - Delegates authentication to the `validatePatientLogin` method in the shared service.
//    - Returns a response with a token or an error message depending on login success.
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login login) {

        return utilityService.validatePatientLogin(login);

    }

    // 6. Define the `getPatientAppointment` Method:
//    - Handles HTTP GET requests to fetch appointment details for a specific patient.
//    - Requires the patient ID, token, and user role as path variables.
//    - Validates the token using the shared service.
//    - If valid, retrieves the patient's appointment data from `PatientService`; otherwise, returns a validation error.
    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientAppointment(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");

        Map<String, String> tm = utilityService.getTokenValidationResponse(token);
        if (!tm.isEmpty()) {
            return patientService.getPatientAppointment(id, token);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid token");
    }

// 7. Define the `filterPatientAppointment` Method:
//    - Handles HTTP GET requests to filter a patient's appointments based on specific conditions.
//    - Accepts filtering parameters: `condition`, `name`, and a token.
//    - Token must be valid for a `"patient"` role.
//    - If valid, delegates filtering logic to the shared service and returns the filtered result.
// 7. Define the `filterPatientAppointment` Method:
//    - Handles HTTP GET requests to filter a patient's appointments based on specific conditions.
//    - Accepts filtering parameters: `condition`, `name`, and a token.
//    - Token must be valid for a `"patient"` role.
//    - If valid, delegates filtering logic to the service layer and returns the filtered result.

    @GetMapping("/filter/{condition}/{name}/{token}")
    public ResponseEntity<?> filterPatientAppointment(@PathVariable String condition,
                                                      @PathVariable String name,
                                                      @PathVariable String token) {

        Map<String, String> tokenMap = utilityService.getTokenValidationResponse(token);
        if (tokenMap.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or missing token");
        }

        return utilityService.filterPatient(condition, name, token);

    }
}


