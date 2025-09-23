package com.otulp.pluto_payments_backend.Security;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

public class SessionChecker {
    public static String getSessionEmail() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        // If no authentication or anonymous user → 401 Unauthorized
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return null;
        }

        return auth.getName();
    }
}
