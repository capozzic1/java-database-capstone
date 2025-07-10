// doctorServices.js

// Import the base API URL from the config file
import { BASE_API } from "/js/config.js";

// Define the full endpoint for doctor-related actions
const DOCTOR_API = `${BASE_API}/doctor`;

// === Function: getDoctors ===
// Purpose: Fetch the list of all doctors
export async function getDoctors() {
  try {
    const res = await fetch(`${DOCTOR_API}`);
    const data = await res.json();
    return data.doctors || [];
  } catch (error) {
    console.error("Failed to fetch doctors:", error);
    return [];
  }
}

// === Function: deleteDoctor ===
// Purpose: Delete a specific doctor using their ID and a token
export async function deleteDoctor(doctorId, token) {
  try {
    const res = await fetch(`${DOCTOR_API}/delete/${doctorId}/${token}`, {
      method: "DELETE",
    });
    const data = await res.json();
    return {
      success: res.ok,
      message: data.message || "Doctor deleted.",
    };
  } catch (error) {
    console.error("Error deleting doctor:", error);
    return {
      success: false,
      message: "Unable to delete doctor due to an error.",
    };
  }
}

// === Function: saveDoctor ===
// Purpose: Create a new doctor (admin only)
export async function saveDoctor(doctor, token) {
  try {
    const res = await fetch(`${DOCTOR_API}/save/${token}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(doctor),
    });
    const data = await res.json();
    return {
      success: res.ok,
      message: data.message || "Doctor saved.",
    };
  } catch (error) {
    console.error("Error saving doctor:", error);
    return {
      success: false,
      message: "Failed to save doctor.",
    };
  }
}

// === Function: filterDoctors ===
// Purpose: Search/filter doctors by name, time, specialty
export async function filterDoctors(name, time, specialty) {
  try {
    const res = await fetch(
      `${DOCTOR_API}/filter/${encodeURIComponent(name)}/${encodeURIComponent(time)}/${encodeURIComponent(specialty)}`
    );

    if (res.ok) {
      const data = await res.json();
      return data;
    } else {
      console.warn("Filtering doctors failed:", res.statusText);
      return { doctors: [] };
    }
  } catch (error) {
    console.error("Error filtering doctors:", error);
    alert("Could not retrieve filtered doctor list.");
    return { doctors: [] };
  }
}
