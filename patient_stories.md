
## Patient User Stories

### **User Story 1**

**Title:**
*As a patient, I want to view a list of doctors without logging in, so that I can explore available options before registering.*

**Acceptance Criteria:**

1. The homepage displays a list of available doctors with names, specialties, and locations.
2. No login or registration is required to view the list.
3. The list is searchable and filterable by specialty or location.

**Priority:** Medium
**Story Points:** 3
**Notes:**

* Edge case: If the doctor list fails to load, an error message should be shown with a retry option.

---

### **User Story 2**

**Title:**
*As a patient, I want to sign up using my email and password, so that I can book appointments.*

**Acceptance Criteria:**

1. The sign-up form requires a valid email address and a password.
2. The system validates email uniqueness and password strength.
3. After successful sign-up, the user is redirected to the dashboard or login page.

**Priority:** High
**Story Points:** 3
**Notes:**

* Consider adding optional fields like name and phone number for future use.

---

### **User Story 3**

**Title:**
*As a patient, I want to log into the portal, so that I can manage my bookings.*

**Acceptance Criteria:**

1. The login form accepts registered email and password.
2. Incorrect credentials show an error message.
3. On successful login, the user is taken to the dashboard with booking options.

**Priority:** High
**Story Points:** 2
**Notes:**

* Include “Remember Me” and “Forgot Password?” options.

---

### **User Story 4**

**Title:**
*As a patient, I want to book an hour-long appointment with a doctor, so that I can receive a consultation.*

**Acceptance Criteria:**

1. Patient can choose a doctor and see available time slots.
2. Patient can select a time slot and confirm the 1-hour appointment.
3. Appointment is saved and reflected in the patient’s upcoming appointments.

**Priority:** High
**Story Points:** 5
**Notes:**

* Edge case: Prevent double-booking the same slot.

---

### **User Story 5**

**Title:**
*As a patient, I want to view my upcoming appointments, so that I can prepare accordingly.*

**Acceptance Criteria:**

1. Logged-in users can see a list of their upcoming appointments.
2. Each appointment shows date, time, doctor name, and location or mode (online/in-person).
3. Past appointments are excluded from this list.

**Priority:** Medium
**Story Points:** 3
**Notes:**

* Option to reschedule or cancel may be handled in a separate user story.

---
