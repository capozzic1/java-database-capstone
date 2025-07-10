// patientServices.js
import { API_BASE_URL } from "../config/config.js";

// Set base patient API endpoint
const PATIENT_API = `${API_BASE_URL}/patient`;

/**
 * Patient Signup
 * Registers a new patient in the system.
 * @param {Object} data - Patient registration info
 * @returns {Object} - { success: Boolean, message: String }
 */
export async function patientSignup(data) {
  try {
    const response = await fetch(`${PATIENT_API}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    });

    const result = await response.json();

    if (!response.ok) {
      throw new Error(result.message);
    }

    return { success: true, message: result.message };
  } catch (error) {
    console.error("Error :: patientSignup ::", error);
    return { success: false, message: error.message };
  }
}

/**
 * Patient Login
 * Logs in a patient using credentials.
 * @param {Object} data - { email, password }
 * @returns {Response} - Raw fetch response (caller will extract token)
 */
export async function patientLogin(data) {
  return await fetch(`${PATIENT_API}/login`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });
}

/**
 * Get Patient Data
 * Retrieves patient details using token (used for booking, profile, etc.)
 * @param {String} token - Auth token
 * @returns {Object|null} - Patient object or null if failed
 */
export async function getPatientData(token) {
  try {
    const response = await fetch(`${PATIENT_API}/${token}`);
    const data = await response.json();
    return response.ok ? data.patient : null;
  } catch (error) {
    console.error("Error fetching patient details:", error);
    return null;
  }
}

/**
 * Get Patient Appointments
 * Shared API for both patient & doctor dashboards.
 * @param {String} id - Patient ID
 * @param {String} token - Auth token
 * @param {String} user - "patient" or "doctor"
 * @returns {Array|null} - Appointment list or null if failed
 */
export async function getPatientAppointments(id, token, user) {
  try {
    const response = await fetch(`${PATIENT_API}/${id}/${user}/${token}`);
    const data = await response.json();
    return response.ok ? data.appointments : null;
  } catch (error) {
    console.error("Error fetching appointments:", error);
    return null;
  }
}

/**
 * Filter Appointments
 * Filters appointments by condition and patient/doctor name.
 * @param {String} condition - "pending", "consulted", etc.
 * @param {String} name - Patient or doctor name
 * @param {String} token - Auth token
 * @returns {Object} - { appointments: Array }
 */
export async function filterAppointments(condition, name, token) {
  try {
    const response = await fetch(`${PATIENT_API}/filter/${condition}/${name}/${token}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (response.ok) {
      const data = await response.json();
      return data;
    } else {
      console.error("Failed to filter appointments:", response.statusText);
      return { appointments: [] };
    }
  } catch (error) {
    console.error("Error filtering appointments:", error);
    alert("Something went wrong!");
    return { appointments: [] };
  }
}
