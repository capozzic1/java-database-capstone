package com.project.back_end.mvc;

import com.project.back_end.services.UtilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@Controller
public class DashboardController {

    @Autowired
    private UtilityService utilityService;

    @GetMapping("/adminDashboard")
    public String adminDashboard(@CookieValue(value = "token", required = false) String token) {

        if (token != null) {
            Map<String, String> result = utilityService.validateToken(token, "admin");
            if (!result.isEmpty()) {
                return "admin/adminDashboard";
            }
        }
        return "redirect:/";
    }

    @GetMapping("/doctorDashboard")
    public String doctorDashboard(@RequestHeader(value = "Authorization", required = true) String authHeader) {
        String token = extractToken(authHeader);
        if (token != null) {
            Map<String, String> result = utilityService.validateToken(token, "doctor");
            if (!result.isEmpty()) {
                return "doctor/doctorDashboard";
            }
        }
        return "redirect:/";
    }

    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Remove "Bearer " prefix
        }
        return null;
    }
}
