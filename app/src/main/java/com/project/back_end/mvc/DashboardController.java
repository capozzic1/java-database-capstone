package com.project.back_end.mvc;

import com.project.back_end.DTO.DoctorDTO;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.TokenService;
import com.project.back_end.services.UtilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class DashboardController {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UtilityService utilityService;

    @Autowired
    private DoctorService doctorService;

    @GetMapping("/adminDashboard")
    public String adminDashboard(@CookieValue(value = "token", required = false) String token) {

        if (token != null) {
            Map<String, String> result = utilityService.getTokenValidationResponse(token);
            if (!result.isEmpty()) {
                return "admin/adminDashboard";
            }
        }
        return "redirect:/";
    }

    @GetMapping("/doctorDashboard")
    public String doctorDashboard(@CookieValue(value = "token", required = false) String token) {
        if (token != null) {
            Map<String, String> result = utilityService.getTokenValidationResponse(token);
            if (!result.isEmpty()) {
                return "doctor/doctorDashboard";
            }
        }
        return "redirect:/";
    }
    @GetMapping("/profile")
    public String showProfile(@CookieValue(value = "token", required = false) String token, Model model) {

        Map<String, String> tokenMap = utilityService.getTokenValidationResponse(token);
        if (!tokenMap.isEmpty()) {
            String email = tokenService.extractEmail(token);
            DoctorDTO dto = doctorService.findByEmail(email);

            model.addAttribute("doctor", dto);
        }
        return "/doctorProfile.html";
    }
    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Remove "Bearer " prefix
        }
        return null;
    }
}
