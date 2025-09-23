package com.otulp.pluto_payments_backend.Controllers;

import com.otulp.pluto_payments_backend.Repositories.CustomerRepo;
import com.otulp.pluto_payments_backend.Models.Customer;
import com.otulp.pluto_payments_backend.Security.PlutoHasher;
import com.otulp.pluto_payments_backend.Security.SessionChecker;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.otulp.pluto_payments_backend.DTOs.LoginRequest;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/public/api/auth")
@CrossOrigin(
        origins = "http://localhost:8080",
        allowCredentials = "true"
)
public class AuthController {

    private final CustomerRepo customerRepo;

    public AuthController(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    // POST /api/auth/login
    // Checks if email + password are valid, creates a session and stores the user in it.
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletRequest req,
            HttpServletResponse res
    ) {
        // 1. Look up customer by email
        Customer user = customerRepo.findByEmail(loginRequest.getEmail());
        if (user == null) {
            return ResponseEntity.status(401).body("Invalid credentials (email)");
        }

        // Password hashing goes here:
        String hashedPassword = PlutoHasher.hashedString(loginRequest.getPassword());

        // 2. Verify password (no hashing yet!)
        if (!user.getPassword().equals(hashedPassword)) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        // 3. Create Spring Security Authentication object for this user
        var auth = new UsernamePasswordAuthenticationToken(
                user.getEmail(), // principal (username/email)
                null,            // no credentials stored
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")) // user role
        );

        // 4. Put authentication into SecurityContext
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        // 5. Ensure session exists and save context to it
        req.getSession(true);
        new HttpSessionSecurityContextRepository().saveContext(context, req, res);

        // 6. Return success
        return ResponseEntity.ok(Map.of("message", "Login ok"));
    }

    // GET /api/auth/me
    // Checks if session is valid and returns current user info.
    @GetMapping("/me")
    public ResponseEntity<?> me() {
        String email = SessionChecker.getSessionEmail();
        if (email == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        // Find the full Customer entity by email (stored in Authentication principal)
        Customer user = customerRepo.findByEmail(email);

        // Return basic info about the logged-in user
        return ResponseEntity.ok(Map.of(
                "email", user.getEmail(),
                "name", user.getFirstName()
        ));
    }

    // POST /api/auth/logout
    // Invalidates the session and clears the security context.
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest req) {
        var session = req.getSession(false);
        if (session != null) session.invalidate(); // destroy session
        SecurityContextHolder.clearContext();      // clear authentication
        return ResponseEntity.ok().build();
    }
}