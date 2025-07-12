// Import the openModal function to handle showing login popups/modals
import { openModal } from "../components/modals.js";

// Import the base API URL from the config file
import { API_BASE_URL } from "../config/config.js";

// Define constants for the admin and doctor login API endpoints
const ADMIN_API = `${API_BASE_URL}/admin/login`;
const DOCTOR_API = `${API_BASE_URL}/doctor/login`;

// Wait until DOM is loaded
window.onload = () => {
  const adminLoginBtn = document.getElementById("adminLogin");
  const doctorLoginBtn = document.getElementById("doctorLogin");

  // Admin login button logic
  if (adminLoginBtn) {
    adminLoginBtn.addEventListener("click", () => {
      openModal("adminLogin");
    });
  }

  // Doctor login button logic
  if (doctorLoginBtn) {
    doctorLoginBtn.addEventListener("click", () => {
      openModal("doctorLogin");
    });
  }
};

// === ADMIN LOGIN HANDLER ===
window.adminLoginHandler = async function () {
  try {
    // Step 1: Get credentials
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    // Step 2: Create admin object
    const admin = { username, password };

    // Step 3: Send POST request
    const res = await fetch(ADMIN_API, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(admin),
    });

    // Step 4: If success
    if (res.ok) {
      const data = await res.json();
      document.cookie = `token=${data.token}; path=/; secure; SameSite=Lax; max-age=3600`;
      localStorage.setItem("token", data.token);
      selectRole("admin");
    } else {
      // Step 5: Login failed
      alert("Invalid admin credentials. Please try again.");
    }
  } catch (error) {
    // Step 6: Catch errors
    console.error("Admin login error:", error);
    alert("Something went wrong. Please try again later.");
  }
};

// === DOCTOR LOGIN HANDLER ===
window.doctorLoginHandler = async function () {
  try {
    // Step 1: Get credentials
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    // Step 2: Create doctor object
    const doctor = { email, password };

    // Step 3: Send POST request
    const res = await fetch(DOCTOR_API, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(doctor),
    });

    // Step 4: If success
    if (res.ok) {
      const data = await res.json();
      document.cookie = `token=${data.token}; path=/; secure; SameSite=Lax; max-age=3600`;

      localStorage.setItem("token", data.token);
      selectRole("doctor");
    } else {
      // Step 5: Login failed
      alert("Invalid doctor credentials. Please try again.");
    }
  } catch (error) {
    // Step 6: Catch errors
    console.error("Doctor login error:", error);
    alert("Something went wrong. Please try again later.");
  }
};

// Optional: Simple role selector to redirect or customize UI
//function selectRole(role) {
//  localStorage.setItem("userRole", role);
//  if (role === "admin") {
//    window.location.href = "/adminDashboard.html";
//  } else if (role === "doctor") {
//    window.location.href = "/doctorDashboard.html";
//  }
//}
// === Updated selectRole Function ===
function selectRole(role) {
  localStorage.setItem("userRole", role);
  if (role === "admin") {
    window.location.href = "/adminDashboard";
  } else if (role === "doctor") {
    window.location.href = "/doctorDashboard";
  }
}



