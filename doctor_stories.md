## Doctor User Stories

### **User Story 1**

**Title:**
*As a doctor, I want to log into the portal, so that I can manage my appointments.*

**Acceptance Criteria:**

1. The login page accepts valid credentials (email and password).
2. Incorrect login attempts show a clear error message.
3. Successful login redirects to the doctor dashboard.

**Priority:** High
**Story Points:** 2
**Notes:**


### **User Story 2**

**Title:**
*As a doctor, I want to view my appointment calendar, so that I can stay organized.*

**Acceptance Criteria:**

1. The calendar shows daily, weekly, and monthly views of scheduled appointments.
2. Each appointment displays patient name, time, and status (confirmed, canceled, etc.).
3. Calendar is updated in real-time as appointments are booked or canceled.

**Priority:** High
**Story Points:** 5
**Notes:**


---

### **User Story 3**

**Title:**
*As a doctor, I want to mark my unavailability, so that patients can only book time slots when I'm available.*

**Acceptance Criteria:**

1. Doctor can select days and hours of unavailability from the calendar view or availability settings.
2. Marked time slots are automatically removed from the patient booking options.
3. System provides confirmation of updated availability.

**Priority:** High
**Story Points:** 4
**Notes:**

* Edge case: Prevent marking unavailability for already-booked appointments without prompting rescheduling.

---

### **User Story 4**

**Title:**
*As a doctor, I want to update my profile with specialization and contact information, so that patients have up-to-date details.*

**Acceptance Criteria:**

1. Doctor can update specialization, qualifications, contact info, and a short bio.
2. Changes are reflected on the public doctor listing and patient-facing pages.
3. Profile changes are saved and confirmed with a success message.

**Priority:** Medium
**Story Points:** 3
**Notes:**

* Optional: Upload a profile photo or office address.

---

### **User Story 5**

**Title:**
*As a doctor, I want to view patient details for upcoming appointments, so that I can be prepared in advance.*

**Acceptance Criteria:**

1. Doctor can click on an appointment to see patient name, age, reason for visit, and relevant notes.
2. Patient details are visible only for confirmed, upcoming appointments.
3. Viewing patient info complies with HIPAA or local privacy laws.

**Priority:** High
**Story Points:** 4
**Notes:**

* Data should be securely accessed and encrypted at rest and in transit.

---

Let me know if you’d like to add logout or availability recurrence features next!
