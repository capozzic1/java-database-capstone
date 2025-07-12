import { getAllAppointments } from './services/appointmentRecordService.js';
import { createPatientRow } from './components/patientRows.js';
import { renderContent } from './render.js';

// Initialize state
let selectedDate = new Date().toISOString().split('T')[0]; // 'YYYY-MM-DD'
let token = localStorage.getItem('token');
let patientName = null;

// Function: Load and Display Appointments
async function loadAppointments() {
  const tableBody = document.querySelector('#appointmentsTable tbody');
  try {
    const { appointments } = await getAllAppointments(selectedDate, patientName, token);

    tableBody.innerHTML = '';

    if (!appointments || appointments.length === 0) {
      const row = document.createElement('tr');
      row.innerHTML = `<td colspan="4">No Appointments found for today.</td>`;
      tableBody.appendChild(row);
      return;
    }
    const doctorId = appointments[0].doctorId;

    appointments.forEach(app => {
    const patientObj = app.patient;
      const patient = {
        id: app.patientId,
        name: app.patientName,
        phone: app.patientPhone,
        email: app.patientEmail
      };
      const row = createPatientRow(patient, app, doctorId);
      tableBody.appendChild(row);
    });

  } catch (error) {
    console.error('Error fetching appointments:', error);
    const row = document.createElement('tr');
    row.innerHTML = `<td colspan="4">Error loading appointments. Try again later.</td>`;
    tableBody.appendChild(row);
  }
}

// ✅ Delay all DOM-related work until DOM is ready
document.addEventListener('DOMContentLoaded', () => {
  renderContent();

  const tableBody = document.querySelector('#appointmentsTable tbody');
  const searchBar = document.getElementById('searchBar');
  const datePicker = document.getElementById('datePicker');
  const todayButton = document.getElementById('todayButton');

  if (!searchBar || !datePicker || !todayButton || !tableBody) {
    console.warn("Some DOM elements are missing. Skipping event setup.");
    return;
  }

  // Set default date
  datePicker.value = selectedDate;

  // Event listeners
  searchBar.addEventListener('input', () => {
    const input = searchBar.value.trim();
    patientName = input !== '' ? input : null;
    loadAppointments();
  });

  todayButton.addEventListener('click', () => {
    selectedDate = new Date().toISOString().split('T')[0];
    datePicker.value = selectedDate;
    loadAppointments();
  });

  datePicker.addEventListener('change', () => {
    selectedDate = datePicker.value;
    loadAppointments();
  });

  // Initial load
  loadAppointments();
});
