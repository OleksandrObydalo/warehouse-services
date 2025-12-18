package org.example.webclient.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor to check if user is authenticated before accessing protected pages.
 */
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        String requestURI = request.getRequestURI();

        // Allow access to login page, static resources, and error pages
        if (requestURI.equals("/login") || 
            requestURI.equals("/logout") ||
            requestURI.startsWith("/css/") ||
            requestURI.startsWith("/js/") ||
            requestURI.startsWith("/images/") ||
            requestURI.equals("/error")) {
            return true;
        }

        // Check if user is logged in
        if (session == null || session.getAttribute("userId") == null) {
            // Redirect to login page
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }
}

