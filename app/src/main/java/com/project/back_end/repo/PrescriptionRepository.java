package com.project.back_end.repo;

import com.project.back_end.models.Prescription;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PrescriptionRepository extends MongoRepository<Prescription, Long> {
// 1. Extend MongoRepository:
//    - The repository extends MongoRepository<Prescription, String>, which provides basic CRUD functionality for MongoDB.
//    - This allows the repository to perform operations like save, delete, update, and find without needing to implement these methods manually.
//    - MongoRepository is tailored for working with MongoDB, unlike JpaRepository which is used for relational databases.

// Example: public interface PrescriptionRepository extends MongoRepository<Prescription, String> {}

    // 2. Custom Query Method:
    public List<Prescription> findByAppointmentId(Long appointmentId);
//    - **findByAppointmentId**:
//      - This method retrieves a list of prescriptions associated with a specific appointment.
//      - Return type: List<Prescription>
//      - Parameters: Long appointmentId
//      - MongoRepository automatically derives the query from the method name, in this case, it will find prescriptions by the appointment ID.


}

