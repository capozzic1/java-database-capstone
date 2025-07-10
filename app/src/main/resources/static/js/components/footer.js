function renderFooter() {
  const footer = document.getElementById("footer");
  if (!footer) return;

  footer.innerHTML = `
    <footer class="footer">
      <div class="footer-content">
        <!-- Branding -->
        <div class="footer-logo">
          <img src="/assets/images/logo/logo.png" alt="Clinic Logo" height="40" />
          <p>© ${new Date().getFullYear()} MediClinic. All rights reserved.</p>
        </div>

        <!-- Links Grid -->
        <div class="footer-links">
          <!-- Company -->
          <div class="footer-column">
            <h4>Company</h4>
            <a href="/about.html">About</a>
            <a href="/careers.html">Careers</a>
            <a href="/press.html">Press</a>
          </div>

          <!-- Support -->
          <div class="footer-column">
            <h4>Support</h4>
            <a href="/account.html">Account</a>
            <a href="/help.html">Help Center</a>
            <a href="/contact.html">Contact</a>
          </div>

          <!-- Legal -->
          <div class="footer-column">
            <h4>Legal</h4>
            <a href="/terms.html">Terms</a>
            <a href="/privacy.html">Privacy Policy</a>
            <a href="/licensing.html">Licensing</a>
          </div>
        </div>
      </div>
    </footer>
  `;
}

// Auto-call on load
renderFooter();
