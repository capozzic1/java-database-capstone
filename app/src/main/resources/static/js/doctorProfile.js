// profile-update.js

document.addEventListener('DOMContentLoaded', () => {
  const btn = document.getElementById('profile-submit-btn');
  btn.addEventListener('click', async (event) => {
    event.preventDefault();

    // 1. Pull token from localStorage
    const token = localStorage.getItem('token');
    if (!token) {
      alert('No authentication token found.');
      return;
    }
    const role = localStorage.getItem('userRole');
    // 2. Build the URL
    const url = `http://localhost:8080/doctor/${token}`;

    // 3. Collect form values
    const email       = document.getElementById('email').value;
    const phone       = document.getElementById('phone').value;
    const name        = document.getElementById('name').value;
    const id          = document.getElementById('doctor-id').textContent;
    const specialty   = document.getElementById('specialty').value;
    // Gather checked timeslots
    const availableTimes = Array.from(
      document.querySelectorAll('.form-check-input')
    )
      .filter(cb => cb.checked)
      .map(cb => cb.value);

    // 4. Assemble payload matching the Doctor model
//    public class DoctorDTO {
//        private Long id;
//        private String name;
//        private String specialty;
//        private String email;
//        private String phone;
//        private List<String> availableTimes;
//    }
    const payload = {
      id,
      email,
      phone,
      name,
      specialty,
      availableTimes
    };

    // 5. Send PUT request
    try {
      const response = await fetch(url, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      if (response.ok) {
        alert('The profile has been updated.');
        setTimeout(() => {
          console.log("this is the third message");
          window.location.href = '/doctorDashboard';
        }, 1000);
      } else {
        console.error('Update failed:', response.status, await response.text());
      }
    } catch (err) {
      console.error('Network error:', err);
    }
  });
});
