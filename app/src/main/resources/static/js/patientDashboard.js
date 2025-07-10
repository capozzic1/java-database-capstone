// patientDashboard.js

import { getDoctors, filterDoctors } from './services/doctorServices.js';
import { createDoctorCard } from './components/doctorCard.js';
import { openModal } from './components/modals.js';
import { patientSignup, patientLogin } from './services/patientServices.js';

// Load doctor cards on page load
document.addEventListener("DOMContentLoaded", () => {
  loadDoctorCards();
  bindModalTriggers();
  bindFilterListeners();
});

// Load all doctors and render them
async function loadDoctorCards() {
  try {
    const doctors = await getDoctors();
    renderDoctorCards(doctors);
  } catch (error) {
    console.error("Failed to load doctors:", error);
    document.getElementById("content").innerHTML = "<p>❌ Failed to load doctors. Please try again later.</p>";
  }
}

// Bind signup/login modal triggers
function bindModalTriggers() {
  const signupBtn = document.getElementById("patientSignup");
  if (signupBtn) signupBtn.addEventListener("click", () => openModal("patientSignup"));

  const loginBtn = document.getElementById("patientLogin");
  if (loginBtn) loginBtn.addEventListener("click", () => openModal("patientLogin"));
}

// Attach search and filter listeners
function bindFilterListeners() {
  document.getElementById("searchBar").addEventListener("input", filterDoctorsOnChange);
  document.getElementById("filterTime").addEventListener("change", filterDoctorsOnChange);
  document.getElementById("filterSpecialty").addEventListener("change", filterDoctorsOnChange);
}

// Filter doctors based on input values
async function filterDoctorsOnChange() {
  const name = document.getElementById("searchBar").value.trim() || null;
  const time = document.getElementById("filterTime").value || null;
  const specialty = document.getElementById("filterSpecialty").value || null;

  try {
    const response = await filterDoctors(name, time, specialty);
    const doctors = response?.doctors || [];
    if (doctors.length > 0) {
      renderDoctorCards(doctors);
    } else {
      document.getElementById("content").innerHTML = "<p>No doctors found with the given filters.</p>";
    }
  } catch (error) {
    console.error("Failed to filter doctors:", error);
    alert("❌ An error occurred while filtering doctors.");
  }
}

// Render an array of doctor objects
function renderDoctorCards(doctors) {
  const contentDiv = document.getElementById("content");
  contentDiv.innerHTML = "";
  doctors.forEach(doctor => {
    const card = createDoctorCard(doctor);
    contentDiv.appendChild(card);
  });
}

// Handle patient signup
window.signupPatient = async function () {
  try {
    const name = document.getElementById("name").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const phone = document.getElementById("phone").value;
    const address = document.getElementById("address").value;

    const data = { name, email, password, phone, address };
    const { success, message } = await patientSignup(data);

    if (success) {
      alert(message);
      document.getElementById("modal").style.display = "none";
      window.location.reload();
    } else {
      alert(message);
    }
  } catch (error) {
    console.error("Signup failed:", error);
    alert("❌ An error occurred while signing up.");
  }
};

// Handle patient login
window.loginPatient = async function () {
  try {
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    const response = await patientLogin({ email, password });

    if (response.ok) {
      const result = await response.json();
      localStorage.setItem('token', result.token);
      window.location.href = '/pages/loggedPatientDashboard.html';
    } else {
      alert('❌ Invalid credentials!');
    }
  } catch (error) {
    console.error("Login failed:", error);
    alert("❌ Failed to login. Please try again later.");
  }
};
