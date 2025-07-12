// doctorCard.js

// Import the overlay function for booking appointments (e.g. modal)
import { showBookingOverlay } from "/js/loggedPatient.js";

// Import the deleteDoctor API function to remove doctors (admin role)
import { deleteDoctor } from "/js/services/doctorServices.js";

// Import function to fetch patient details (used during booking)
import { getPatientData } from "/js/services/patientServices.js";

// Function to create and return a DOM element for a single doctor card
export function createDoctorCard(doctor) {
  // Create the main container for the doctor card
  const card = document.createElement("div");
  card.classList.add("doctor-card");

  // Retrieve the current user role from localStorage
  const role = localStorage.getItem("userRole");

  // Create a div to hold doctor information
  const infoDiv = document.createElement("div");
  infoDiv.classList.add("doctor-info");

  // Create and set the doctor’s name
  const name = document.createElement("h3");
  name.textContent = doctor.name;

  // Create and set the doctor's specialization
  const specialization = document.createElement("p");
  specialization.textContent = `Specialty: ${doctor.specialty}`;

  // Create and set the doctor's email
  const email = document.createElement("p");
  email.textContent = `Email: ${doctor.email}`;

  // Create and list available appointment times
  const availability = document.createElement("p");
  availability.textContent = `Available: ${doctor.availableTimes?.join(", ") || "Not listed"}`;

  // Append all info elements to the doctor info container
  infoDiv.appendChild(name);
  infoDiv.appendChild(specialization);
  infoDiv.appendChild(email);
  infoDiv.appendChild(availability);

  // Create a container for card action buttons
  const actionsDiv = document.createElement("div");
  actionsDiv.classList.add("card-actions");

  // === ADMIN ROLE ACTIONS ===
  if (role === "admin") {
    // Create a delete button
    const deleteBtn = document.createElement("button");
    deleteBtn.textContent = "Delete";
    deleteBtn.classList.add("adminBtn");

    // Add click handler for delete button
    deleteBtn.addEventListener("click", async () => {
      const confirmed = confirm(`Are you sure you want to delete Dr. ${doctor.name}?`);
      if (!confirmed) return;

      try {
        // Get the admin token from localStorage
        const token = localStorage.getItem("token");

        // Call API to delete the doctor
        const success = await deleteDoctor(doctor.id, token);

        // Show result and remove card if successful
        if (success) {
          alert("Doctor deleted successfully.");
          card.remove();
        } else {
          alert("Failed to delete doctor.");
        }
      } catch (error) {
        console.error("Delete error:", error);
        alert("An error occurred while deleting the doctor.");
      }
    });

    // Add delete button to actions container
    actionsDiv.appendChild(deleteBtn);
  }

  // === PATIENT (NOT LOGGED-IN) ROLE ACTIONS ===
  else if (role === "patient") {
    // Create a book now button
    const bookNow = document.createElement("button");
    bookNow.textContent = "Book Now";
    bookNow.classList.add("button");

    // Alert patient to log in before booking
    bookNow.addEventListener("click", () => {
      alert("Please log in to book an appointment.");
    });

    // Add button to actions container
    actionsDiv.appendChild(bookNow);
  }

  // === LOGGED-IN PATIENT ROLE ACTIONS ===
  else if (role === "loggedPatient") {
    // Create a book now button
    const bookNow = document.createElement("button");
    bookNow.textContent = "Book Now";
    bookNow.classList.add("button");

    // Handle booking logic for logged-in patient
    bookNow.addEventListener("click", async (e) => {
      // Redirect if token not available
      const token = localStorage.getItem("token");
      if (!token) {
        alert("Session expired. Please log in again.");
        window.location.href = "/";
        return;
      }

      try {
        // Fetch patient data with token
        const patientData = await getPatientData(token);

        // Show booking overlay UI with doctor and patient info
        showBookingOverlay(e, doctor, patientData);
      } catch (err) {
        console.error("Booking error:", err);
        alert("Unable to book appointment. Please try again.");
      }
    });

    // Add button to actions container
    actionsDiv.appendChild(bookNow);
  }

  // Append doctor info and action buttons to the card
  card.appendChild(infoDiv);
  card.appendChild(actionsDiv);

  // Return the complete doctor card element
  return card;
}
