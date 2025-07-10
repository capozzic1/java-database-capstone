// adminDashboard.js

import { openModal } from "./components/modals.js";
import { createDoctorCard } from "./components/doctorCard.js";
import {
  getDoctors,
  filterDoctors,
  saveDoctor
} from "./services/doctorServices.js";

// DOM references
const contentDiv = document.getElementById("content");
const searchBar = document.getElementById("searchBar");
const sortByTime = document.getElementById("sortByTime");
const filterBySpecialty = document.getElementById("filterBySpecialty");
const addDoctorBtn = document.getElementById("addDocBtn");

// === Attach Add Doctor Button Listener ===
if (addDoctorBtn) {
  addDoctorBtn.addEventListener("click", () => {
    openModal("addDoctor");
  });
}

// === DOM Load Initialization ===
window.addEventListener("DOMContentLoaded", () => {
  loadDoctorCards();

  if (searchBar) {
    searchBar.addEventListener("input", filterDoctorsOnChange);
  }
  if (sortByTime) {
    sortByTime.addEventListener("change", filterDoctorsOnChange);
  }
  if (filterBySpecialty) {
    filterBySpecialty.addEventListener("change", filterDoctorsOnChange);
  }
});

// === Load All Doctor Cards ===
async function loadDoctorCards() {
  try {
    const doctors = await getDoctors();
    renderDoctorCards(doctors);
  } catch (error) {
    console.error("Failed to load doctors:", error);
  }
}

// === Filter Doctors Dynamically ===
async function filterDoctorsOnChange() {
  try {
    const name = searchBar.value.trim() || "null";
    const time = sortByTime.value || "null";
    const specialty = filterBySpecialty.value || "null";

    const data = await filterDoctors(name, time, specialty);

    if (data.doctors && data.doctors.length > 0) {
      renderDoctorCards(data.doctors);
    } else {
      contentDiv.innerHTML = `<p style="text-align:center; font-style:italic;">No doctors found with the given filters.</p>`;
    }
  } catch (error) {
    console.error("Filter error:", error);
    alert("Something went wrong while filtering doctors.");
  }
}

// === Render Helper Function ===
function renderDoctorCards(doctors) {
  contentDiv.innerHTML = ""; // clear area
  doctors.forEach(doctor => {
    const card = createDoctorCard(doctor);
    contentDiv.appendChild(card);
  });
}

// === Add New Doctor from Modal ===
export async function adminAddDoctor() {
  try {
    // Get form field values
    const name = document.getElementById("doctorName").value.trim();
    const email = document.getElementById("doctorEmail").value.trim();
    const phone = document.getElementById("doctorPhone").value.trim();
    const password = document.getElementById("doctorPassword").value.trim();
    const specialty = document.getElementById("doctorSpecialty").value.trim();
    const availableTimes = document.getElementById("doctorTimes").value
      .split(",")
      .map(t => t.trim())
      .filter(Boolean);

    // Validate token
    const token = localStorage.getItem("token");
    if (!token) {
      alert("Unauthorized. Please log in as admin.");
      return;
    }

    // Build doctor object
    const doctor = {
      name,
      email,
      phone,
      password,
      specialty,
      availableTimes
    };

    // Save doctor
    const result = await saveDoctor(doctor, token);

    if (result.success) {
      alert("Doctor added successfully!");
      document.getElementById("modal").style.display = "none";
      window.location.reload(); // Refresh to show new doctor
    } else {
      alert(`Error: ${result.message}`);
    }
  } catch (error) {
    console.error("Error adding doctor:", error);
    alert("An error occurred while adding the doctor.");
  }
}
