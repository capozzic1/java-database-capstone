// doctorDashboard.js

import { getAllAppointments } from './services/appointmentService.js';
import { createPatientRow } from './components/patientRow.js';
import { renderContent } from './layout/render.js';

// Get DOM elements
const tableBody = document.querySelector('#appointmentsTable tbody');
const searchBar = document.getElementById('searchBar');
const datePicker = document.getElementById('datePicker');
const todayButton = document.getElementById('todayBtn');

// Initialize state
let selectedDate = new Date().toISOString().split('T')[0]; // 'YYYY-MM-DD'
let token = localStorage.getItem('token');
let patientName = null;

// Event Listener: Search by Patient Name
searchBar.addEventListener('input', () => {
  const input = searchBar.value.trim();
  patientName = input !== '' ? input : null;
  loadAppointments();
});

// Event Listener: "Today" Button
todayButton.addEventListener('click', () => {
  selectedDate = new Date().toISOString().split('T')[0];
  datePicker.value = selectedDate;
  loadAppointments();
});

// Event Listener: Date Picker
datePicker.addEventListener('change', () => {
  selectedDate = datePicker.value;
  loadAppointments();
});

// Function: Load and Display Appointments
async function loadAppointments() {
  try {
    // Step 1: Fetch appointments
    const appointments = await getAllAppointments(selectedDate, patientName, token);

    // Step 2: Clear current table
    tableBody.innerHTML = '';

    // Step 3: Handle no appointments
    if (!appointments || appointments.length === 0) {
      const row = document.createElement('tr');
      row.innerHTML = `<td colspan="4">No Appointments found for today.</td>`;
      tableBody.appendChild(row);
      return;
    }

    // Step 4: Render appointments
    appointments.forEach(app => {
      const patient = {
        id: app.patientId,
        name: app.patientName,
        phone: app.patientPhone,
        email: app.patientEmail
      };
      const row = createPatientRow(patient, app);
      tableBody.appendChild(row);
    });

  } catch (error) {
    // Step 5: Handle errors
    console.error('Error fetching appointments:', error);
    const row = document.createElement('tr');
    row.innerHTML = `<td colspan="4">Error loading appointments. Try again later.</td>`;
    tableBody.appendChild(row);
  }
}

// On page load
document.addEventListener('DOMContentLoaded', () => {
  renderContent();
  datePicker.value = selectedDate;
  loadAppointments();
});
