// header.js

function renderHeader() {
  const headerDiv = document.getElementById("header");
  if (!headerDiv) return;

  // Root homepage logic: reset and show basic header only
  if (window.location.pathname.endsWith("/")) {
    localStorage.removeItem("userRole");
    localStorage.removeItem("token");
    headerDiv.innerHTML = `
      <header class="header">
        <div class="logo-section">
          <img src="../assets/images/logo/logo.png" alt="Hospital CRM Logo" class="logo-img">
          <span class="logo-title">Hospital CMS</span>
        </div>
      </header>`;
    return;
  }

  // Retrieve session info
  const role = localStorage.getItem("userRole");
  const token = localStorage.getItem("token");

  // If session invalid, force logout
  if ((role === "loggedPatient" || role === "admin" || role === "doctor") && !token) {
    localStorage.removeItem("userRole");
    alert("Session expired or invalid login. Please log in again.");
    window.location.href = "/";
    return;
  }

  // Start header layout
  let headerContent = `
    <header class="header">
      <div class="logo-section">
        <img src="../assets/images/logo/logo.png" alt="Hospital CRM Logo" class="logo-img">
        <span class="logo-title">Hospital CMS</span>
      </div>
      <nav>
  `;

  // Role-specific actions
  if (role === "admin") {
    headerContent += `
      <button id="addDocBtn" class="adminBtn">Add Doctor</button>
      <a href="#" id="logoutBtn">Logout</a>`;
  } else if (role === "doctor") {
    headerContent += `
      <button class="adminBtn" onclick="selectRole('doctor')">Home</button>
      <a href="#" id="profileBtn">Profile</a>
      <a href="#" id="logoutBtn">Logout</a>`;
  } else if (role === "patient") {
    headerContent += `
      <button id="patientLogin" class="adminBtn">Login</button>
      <button id="patientSignup" class="adminBtn">Sign Up</button>`;
  } else if (role === "loggedPatient") {
    headerContent += `
      <button id="home" class="adminBtn" onclick="window.location.href='/pages/loggedPatientDashboard.html'">Home</button>
      <button id="patientAppointments" class="adminBtn" onclick="window.location.href='/pages/patientAppointments.html'">Appointments</button>
      <a href="#" id="logoutPatientBtn">Logout</a>`;
  }

  // Close nav and header
  headerContent += `
      </nav>
    </header>
  `;

  // Render it
  headerDiv.innerHTML = headerContent;

  // Attach dynamic button listeners
  attachHeaderButtonListeners();
}

function attachHeaderButtonListeners() {
//  if (addDocBtn) {
//    addDocBtn.addEventListener("click", () => openModal("addDoctor"));
//  }  const addDocBtn = document.getElementById("addDocBtn");
//

  const logoutBtn = document.getElementById("logoutBtn");
  if (logoutBtn) {
    logoutBtn.addEventListener("click", logout);
  }

  const profileBtn = document.getElementById("profileBtn");
if (profileBtn) {
    profileBtn.addEventListener("click", profile);

}
  const logoutPatientBtn = document.getElementById("logoutPatientBtn");
  if (logoutPatientBtn) {
    logoutPatientBtn.addEventListener("click", logoutPatient);
  }
}
function profile() {
window.location.href = "/profile";
}
// Admin & doctor logout
function logout() {
  localStorage.removeItem("token");
  document.cookie = "token=; path=/; max-age=0; secure; SameSite=Lax";
  localStorage.removeItem("userRole");
  window.location.href = "/";
}

// Logged-in patient logout (revert to patient role)
function logoutPatient() {
  localStorage.removeItem("token");
  document.cookie = "token=; path=/; max-age=0; secure; SameSite=Lax";
  localStorage.setItem("userRole", "patient");
  window.location.href = "/pages/patientDashboard.html";
}

// Initialize header on load
window.addEventListener("DOMContentLoaded", renderHeader);
