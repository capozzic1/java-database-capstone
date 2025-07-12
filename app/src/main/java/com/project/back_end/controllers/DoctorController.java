package com.project.back_end.controllers;

import com.project.back_end.DTO.DoctorDTO;
import com.project.back_end.DTO.Login;
import com.project.back_end.models.Doctor;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.UtilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.path}doctor")
public class DoctorController {

// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to define it as a REST controller that serves JSON responses.
//    - Use `@RequestMapping("${api.path}doctor")` to prefix all endpoints with a configurable API path followed by "doctor".
//    - This class manages doctor-related functionalities such as registration, login, updates, and availability.


    // 2. Autowire Dependencies:
//    - Inject `DoctorService` for handling the core logic related to doctors (e.g., CRUD operations, authentication).
//    - Inject the shared `Service` class for general-purpose features like token validation and filtering.
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private UtilityService utilityService;

// 3. Define the `getDoctorAvailability` Method:
//    - Handles HTTP GET requests to check a specific doctor’s availability on a given date.
//    - Requires `user` type, `doctorId`, `date`, and `token` as path variables.
//    - First validates the token against the user type.
//    - If the token is invalid, returns an error response; otherwise, returns the availability status for the doctor.
    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<?> getDoctorAvailability(@PathVariable String user, @PathVariable Long doctorId, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @PathVariable String token) {
        Map<String, String> tokenMap = utilityService.getTokenValidationResponse(token);
        if (!tokenMap.isEmpty()) {
            List<String> avail = doctorService.getDoctorAvailability(doctorId, date);
            return ResponseEntity.ok(avail);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("no token");
        }
    }

// 4. Define the `getDoctor` Method:
//    - Handles HTTP GET requests to retrieve a list of all doctors.
//    - Returns the list within a response map under the key `"doctors"` with HTTP 200 OK status.
    @GetMapping
    public ResponseEntity<?> getDoctors() {
        return ResponseEntity.ok(doctorService.getDoctors());
    }

// 5. Define the `saveDoctor` Method:
//    - Handles HTTP POST requests to register a new doctor.
//    - Accepts a validated `Doctor` object in the request body and a token for authorization.
//    - Validates the token for the `"admin"` role before proceeding.
//    - If the doctor already exists, returns a conflict response; otherwise, adds the doctor and returns a success message.
    @PostMapping("/save/{token}")
    public ResponseEntity<?> saveDoctor(@RequestBody Doctor doctor,@PathVariable String token) {
        Map<String, String> resp = new HashMap<>();
        Map<String, String> tokenMap = utilityService.getTokenValidationResponse(token);
        if (!tokenMap.isEmpty()) {
             Map<String, Object> doc = doctorService.findDoctorByName(doctor.getName());
             if (!doc.isEmpty()) {
                 resp.put("message", "doc already exists");
                 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
             }
            int result = doctorService.saveDoctor(doctor);
             if (result == 1) {
                 resp.put("message","doctor has been saved");
                 return ResponseEntity.ok(resp);
             }
        } else {
            resp.put("message","no token");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("no token");

        }
        resp.put("message","bad request");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("bad request");
    }

// 6. Define the `doctorLogin` Method:
//    - Handles HTTP POST requests for doctor login.
//    - Accepts a validated `Login` DTO containing credentials.
//    - Delegates authentication to the `DoctorService` and returns login status and token information.
    @PostMapping("/login")
    public ResponseEntity<?> doctorLogin(@RequestBody Login login) {
        return doctorService.validateDoctor(login);
    }

// 7. Define the `updateDoctor` Method:
//    - Handles HTTP PUT requests to update an existing doctor's information.
//    - Accepts a validated `Doctor` object and a token for authorization.
//    - Token must belong to an `"admin"`.
//    - If the doctor exists, updates the record and returns success; otherwise, returns not found or error messages.
    @PutMapping("/{token}")
    public ResponseEntity<?> updateDoctor(@RequestBody DoctorDTO dto, @PathVariable String token) {
        Map<String, String> tokenMap = utilityService.getTokenValidationResponse(token);
        if (!tokenMap.isEmpty()) {
            int result = doctorService.updateDoctor(dto);
            if (result == 1) {
                return ResponseEntity.ok("doctor has been updated");
            } else if (result == -1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Doctor not found");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid or missing token");

    }

// 8. Define the `deleteDoctor` Method:
//    - Handles HTTP DELETE requests to remove a doctor by ID.
//    - Requires both doctor ID and an admin token as path variables.
//    - If the doctor exists, deletes the record and returns a success message; otherwise, responds with a not found or error message.
    @DeleteMapping("/delete/{id}/{token}")
    public ResponseEntity<?> deleteDoctor(@PathVariable Long id, @PathVariable String token) {
        Map<String, String> resp = new HashMap<>();
        Map<String, String> tokenMap = utilityService.getTokenValidationResponse(token);
        if (!tokenMap.isEmpty()) {
            int result = doctorService.deleteDoctor(id);
            if (result == 1) {
                resp.put("message", "doctor deleted successfully");
                return ResponseEntity.ok(resp);
            } else if (result == -1) {
                resp.put("message", "doctor not found");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid token");

    }

// 9. Define the `filter` Method:
//    - Handles HTTP GET requests to filter doctors based on name, time, and specialty.
//    - Accepts `name`, `time`, and `speciality` as path variables.
//    - Calls the shared `Service` to perform filtering logic and returns matching doctors in the response.
    @GetMapping("/filter/{name}/{time}/{specialty}")
    public ResponseEntity<?> filterDoctor(@PathVariable String name, @PathVariable String time, @PathVariable String specialty) {
        return ResponseEntity.ok(utilityService.filterDoctor(name,specialty,time));
    }

}
