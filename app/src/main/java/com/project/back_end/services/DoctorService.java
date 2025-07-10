package com.project.back_end.services;

import com.project.back_end.DTO.DoctorDTO;
import com.project.back_end.DTO.Login;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorService {

// 1. **Add @Service Annotation**:
//    - This class should be annotated with `@Service` to indicate that it is a service layer class.
//    - The `@Service` annotation marks this class as a Spring-managed bean for business logic.
//    - Instruction: Add `@Service` above the class declaration.

    // 2. **Constructor Injection for Dependencies**:
//    - The `DoctorService` class depends on `DoctorRepository`, `AppointmentRepository`, and `TokenService`.
//    - These dependencies should be injected via the constructor for proper dependency management.
//    - Instruction: Ensure constructor injection is used for injecting dependencies into the service.
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    public DoctorService(DoctorRepository doctorRepository, AppointmentRepository appointmentRepository, TokenService tokenService) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;

    }
// 3. **Add @Transactional Annotation for Methods that Modify or Fetch Database Data**:
//    - Methods like `getDoctorAvailability`, `getDoctors`, `findDoctorByName`, `filterDoctorsBy*` should be annotated with `@Transactional`.
//    - The `@Transactional` annotation ensures that database operations are consistent and wrapped in a single transaction.
//    - Instruction: Add the `@Transactional` annotation above the methods that perform database operations or queries.

    // 4. **getDoctorAvailability Method**:
    //    - Retrieves the available time slots for a specific doctor on a particular date and filters out already booked slots.
    //    - The method fetches all appointments for the doctor on the given date and calculates the availability by comparing against booked slots.
    //    - Instruction: Ensure that the time slots are properly formatted and the available slots are correctly filtered.
    @Transactional
    public List<String> getDoctorAvailability(Long doctorId, LocalDate date) {
        List<String> availableTimeSlots = new ArrayList<>();

        // Step 1: Fetch doctor by ID
        Optional<Doctor> doctorOptional = doctorRepository.findById(doctorId);
        if (doctorOptional.isEmpty()) {
            return availableTimeSlots; // No doctor found, return empty list
        }

        Doctor doctor = doctorOptional.get();
        List<String> doctorAvailableTimes = doctor.getAvailableTimes(); // Strings like "   09:00", "10:00", etc.

        // Step 2: Fetch appointments for that doctor on the given date
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, start, end);

        // Step 3: Extract booked time strings (formatted as "HH:mm")
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        Set<String> bookedTimeSlots = appointments.stream()
                .map(a -> a.getAppointmentTime().toLocalTime().format(formatter))
                .collect(Collectors.toSet());

        // Step 4: Filter out booked times from the doctor's available list
        for (String timeSlot : doctorAvailableTimes) {
            if (!bookedTimeSlots.contains(timeSlot)) {
                availableTimeSlots.add(timeSlot);
            }
        }

        return availableTimeSlots;
    }

    // 5. **saveDoctor Method**:
//    - Used to save a new doctor record in the database after checking if a doctor with the same email already exists.
//    - If a doctor with the same email is found, it returns `-1` to indicate conflict; `1` for success, and `0` for internal errors.
//    - Instruction: Ensure that the method correctly handles conflicts and exceptions when saving a doctor.
    @Transactional
    public int saveDoctor(Doctor doctor) {
        Doctor doc = doctorRepository.findByEmail(doctor.getEmail());
        if (doc != null) {
            return -1;
        }
        try {
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }

    }

    // 6. **updateDoctor Method**:
//    - Updates an existing doctor's details in the database. If the doctor doesn't exist, it returns `-1`.
//    - Instruction: Make sure that the doctor exists before attempting to save the updated record and handle any errors properly.
    @Transactional
    public int updateDoctor(Doctor doctor) {

        Optional<Doctor> doc = doctorRepository.findById(doctor.getId());
        try {
            if (doc.isPresent()) {
                doctorRepository.save(doctor);
                return 1;
            } else {
                System.out.println("No doctor found");
                return -1;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    // 7. **getDoctors Method**:
//    - Fetches all doctors from the database. It is marked with `@Transactional` to ensure that the collection is properly loaded.
//    - Instruction: Ensure that the collection is eagerly loaded, especially if dealing with lazy-loaded relationships (e.g., available times).
    @Transactional
    public List<DoctorDTO> getDoctors() {

        List<Doctor> doctors = doctorRepository.findAll();
        return doctors.stream()
                .map(obj -> mapToDoctorDTO(obj))
                .toList();
    }

    public DoctorDTO mapToDoctorDTO(Doctor doctor) {
        return new DoctorDTO(
                doctor.getId(),
                doctor.getName(),
                doctor.getSpecialty(),
                doctor.getEmail(),
                doctor.getPhone(),
                doctor.getAvailableTimes() != null
                        ? new ArrayList<>(doctor.getAvailableTimes())  // force init
                        : new ArrayList<>()
        );
    }

    // 8. **deleteDoctor Method**:
//    - Deletes a doctor from the system along with all appointments associated with that doctor.
//    - It first checks if the doctor exists. If not, it returns `-1`; otherwise, it deletes the doctor and their appointments.
//    - Instruction: Ensure the doctor and their appointments are deleted properly, with error handling for internal issues.
    @Transactional
    public int deleteDoctor(long id) {
        Optional<Doctor> doc = doctorRepository.findById(id);

        if (!doc.isPresent()) {
            return -1;
        }
        try {
            doctorRepository.deleteById(id);
            appointmentRepository.deleteAllByDoctorId(id);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    // 9. **validateDoctor Method**:
//    - Validates a doctor's login by checking if the email and password match an existing doctor record.
//    - It generates a token for the doctor if the login is successful, otherwise returns an error message.
//    - Instruction: Make sure to handle invalid login attempts and password mismatches properly with error responses.
    public ResponseEntity<Map<String, String>> validateDoctor(Login login) {

        Map<String, String> resp = new HashMap<>();
        Doctor doc = doctorRepository.findByEmail(login.getEmail());
        try {
            if (doc == null) {
                resp.put("message", "No doctor found with that email");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
            }
            if (!doc.getPassword().equals(login.getPassword())) {
                resp.put("message", "Invalid password");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(resp);
            }
            if (doc.getPassword().equals(login.getPassword())) {
                String token = tokenService.generateToken(login.getEmail());
                resp.put("token", token);
                return ResponseEntity.ok(resp);
            }
            resp.put("error", "there was an error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);

        } catch (Exception e) {
            resp.put("error", "there was an error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
        }
    }

    // 10. **findDoctorByName Method**:
//    - Finds doctors based on partial name matching and returns the list of doctors with their available times.
//    - This method is annotated with `@Transactional` to ensure that the database query and data retrieval are properly managed within a transaction.
//    - Instruction: Ensure that available times are eagerly loaded for the doctors.
    @Transactional
    public Map<String, Object> findDoctorByName(String name) {
        Map<String, Object> resp = new HashMap<>();
        List<Doctor> docs = doctorRepository.findByNameLike(name);
        List<DoctorDTO> dtos = docs.stream()
                .map(obj -> mapToDoctorDTO(obj))
                .toList();
        resp.put("List of doctors", dtos);
        return resp;
    }

    // 11. **filterDoctorsByNameSpecilityandTime Method**:
//    - Filters doctors based on their name, specialty, and availability during a specific time (AM/PM).
//    - The method fetches doctors matching the name and specialty criteria, then filters them based on their availability during the specified time period.
//    - Instruction: Ensure proper filtering based on both the name and specialty as well as the specified time period.
    @Transactional
    public Map<String, Object> filterDoctorsByNameSpecialtyandTime(String name, String specialty, String amOrPm) {
        Map<String, Object> result = new HashMap<>();
        List<Doctor> doctors = new ArrayList<>();

        if (name.equals("null") && specialty.equals("null")) {
            doctors = doctorRepository.findAll();
        }
        if (!name.equals("null") && !specialty.equals("null")) {
            doctors = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        } else if (name.equals("null")) {
            doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        } else if (specialty.equals("null")) {
            doctors = doctorRepository.findByNameLike(name);
        }

        if (name.equals("null") && specialty.equals("null")) {
            doctors = doctorRepository.findAll();
        }
        List<Doctor> filteredDoctors = "null".equals(amOrPm)
                ? doctors
                : filterDoctorsByTime(doctors, amOrPm);

        List<DoctorDTO> dtos = filteredDoctors.stream()
                .map(this::mapToDoctorDTO)
                .toList();

        result.put("message", "Filtered doctors by name, specialty, and time of day");
        result.put("doctors", dtos);
        return result;
    }

    private List<Doctor> filterDoctorsByTime(List<Doctor> doctors, String amOrPm) {
        List<Doctor> filtered = new ArrayList<>();

        for (Doctor doctor : doctors) {
            for (String timeSlot : doctor.getAvailableTimes()) {
                if (isTimeSlotInPeriod(timeSlot, amOrPm)) {
                    filtered.add(doctor);
                    break;
                }
            }
        }

        return filtered;
    }

// 12. **filterDoctorByTime Method**:
//    - Filters a list of doctors based on whether their available times match the specified time period (AM/PM).
//    - This method processes a list of doctors and their available times to return those that fit the time criteria.
//    - Instruction: Ensure that the time filtering logic correctly handles both AM and PM time slots and edge cases.

    // 13. **filterDoctorByNameAndTime Method**:
//    - Filters doctors based on their name and the specified time period (AM/PM).
//    - Fetches doctors based on partial name matching and filters the results to include only those available during the specified time period.
//    - Instruction: Ensure that the method correctly filters doctors based on the given name and time of day (AM/PM).
    public Map<String, Object> filterDoctorByNameAndTime(String name, String amOrPm) {
        Map<String, Object> resp = new HashMap<>();

        List<Doctor> docs = doctorRepository.findByNameLike(name);
        List<Doctor> filteredDocs = new ArrayList<>();
        for (Doctor doctor : docs) {
            for (String timeslot : doctor.getAvailableTimes()) {
                if (isTimeSlotInPeriod(timeslot, amOrPm)) {
                    filteredDocs.add(doctor);
                    break;

                }
            }
        }
        List<DoctorDTO> filteredDTOs = docs.stream()
                .map(obj -> mapToDoctorDTO(obj))
                .toList();

        resp.put("doctors", filteredDTOs);
        return resp;
    }

    // Utility method to check if a time slot is in AM or PM
    private boolean isTimeSlotInPeriod(String timeSlot, String amOrPm) {
        String startTime = timeSlot.split("-")[0];
        int hour = Integer.parseInt(startTime.split(":")[0]);

        if (amOrPm.equalsIgnoreCase("AM")) {
            return hour < 12;
        } else if (amOrPm.equalsIgnoreCase("PM")) {
            return hour >= 12;
        }
        return false;
    }

    // 14. **filterDoctorByNameAndSpecility Method**:
//    - Filters doctors by name and specialty.
//    - It ensures that the resulting list of doctors matches both the name (case-insensitive) and the specified specialty.
//    - Instruction: Ensure that both name and specialty are considered when filtering doctors.
    public Map<String, Object> filterDoctorsByNameAndSpecialty(String name, String specialty) {
        Map<String, Object> resp = new HashMap<>();
        List<Doctor> docs = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        List<DoctorDTO> dtos = docs.stream()
                .map(obj -> mapToDoctorDTO(obj))
                .toList();
        resp.put("doctors", dtos);
        return resp;
    }

    // 15. **filterDoctorByTimeAndSpecility Method**:
//    - Filters doctors based on their specialty and availability during a specific time period (AM/PM).
//    - Fetches doctors based on the specified specialty and filters them based on their available time slots for AM/PM.
//    - Instruction: Ensure the time filtering is accurately applied based on the given specialty and time period (AM/PM).
    public Map<String, Object> filterDoctorByTimeAndSpecialty(String specialty, String amOrPm) {
        Map<String, Object> response = new HashMap<>();
        List<Doctor> filteredDocs = new ArrayList<>();
        List<Doctor> docs = doctorRepository.findBySpecialtyIgnoreCase(specialty);

        for (Doctor doctor : docs) {
            for (String timeSlot : doctor.getAvailableTimes()) {
                if (isTimeSlotInPeriod(timeSlot, amOrPm)) {
                    filteredDocs.add(doctor);
                    break;
                }
            }
        }
        List<DoctorDTO> dtos = filteredDocs
                .stream()
                .map(obj -> mapToDoctorDTO(obj))
                .toList();

        response.put("doctors", dtos);
        return response;
    }

    // 16. **filterDoctorBySpecility Method**:
//    - Filters doctors based on their specialty.
//    - This method fetches all doctors matching the specified specialty and returns them.
//    - Instruction: Make sure the filtering logic works for case-insensitive specialty matching.
    public Map<String, Object> filterDoctorBySpecialty(String specialty) {
        Map<String, Object> response = new HashMap<>();
        List<Doctor> docs = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        List<DoctorDTO> dtos = docs
                .stream()
                .map(obj -> mapToDoctorDTO(obj))
                .toList();
        response.put("doctors", dtos);
        return response;
    }

    // 17. **filterDoctorsByTime Method**:
//    - Filters all doctors based on their availability during a specific time period (AM/PM).
//    - The method checks all doctors' available times and returns those available during the specified time period.
//    - Instruction: Ensure proper filtering logic to handle AM/PM time periods.
    public Map<String, Object> filterDoctorsByTime(String amOrPm) {
        List<Doctor> docs = doctorRepository.findAll();
        List<Doctor> filteredDoctors = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        for (Doctor doctor : docs) {
            for (String timeSlot : doctor.getAvailableTimes()) {
                if (isTimeSlotInPeriod(timeSlot, amOrPm)) {
                    filteredDoctors.add(doctor);
                    break;
                }
            }
        }
        List<DoctorDTO> dtos = filteredDoctors
                .stream()
                .map(obj -> mapToDoctorDTO(obj))
                .toList();
        response.put("doctors", dtos);
        return response;
    }
}


