package org.example.webclient.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Global exception handler for the web client.
 * Handles errors from the API Gateway and provides user-friendly error messages.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handle 4xx client errors from the Gateway (e.g., 400 Bad Request, 404 Not Found)
     */
    @ExceptionHandler(HttpClientErrorException.class)
    public String handleClientError(HttpClientErrorException ex, Model model) {
        logger.error("Client error from Gateway: {} - {}", ex.getStatusCode(), ex.getMessage());

        String errorMessage;
        String errorTitle;

        switch (ex.getStatusCode().value()) {
            case 400:
                errorTitle = "Invalid Request";
                errorMessage = extractErrorMessage(ex, "The request contains invalid data. Please check your input.");
                break;
            case 404:
                errorTitle = "Not Found";
                errorMessage = extractErrorMessage(ex, "The requested resource was not found.");
                break;
            case 409:
                errorTitle = "Conflict";
                errorMessage = extractErrorMessage(ex, "There is a conflict with the current state. Please try again.");
                break;
            default:
                errorTitle = "Request Error";
                errorMessage = extractErrorMessage(ex, "Unable to process your request. Please try again.");
        }

        model.addAttribute("errorTitle", errorTitle);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("errorCode", ex.getStatusCode().value());
        model.addAttribute("alertType", "warning");

        return "error";
    }

    /**
     * Handle 5xx server errors from the Gateway
     */
    @ExceptionHandler(HttpServerErrorException.class)
    public String handleServerError(HttpServerErrorException ex, Model model) {
        logger.error("Server error from Gateway: {} - {}", ex.getStatusCode(), ex.getMessage());

        String errorMessage = extractErrorMessage(ex, 
                "The service is temporarily unavailable. Please try again later.");

        model.addAttribute("errorTitle", "Service Error");
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("errorCode", ex.getStatusCode().value());
        model.addAttribute("alertType", "danger");

        return "error";
    }

    /**
     * Handle connection errors (e.g., Gateway is down)
     */
    @ExceptionHandler(ResourceAccessException.class)
    public String handleConnectionError(ResourceAccessException ex, Model model) {
        logger.error("Connection error to Gateway: {}", ex.getMessage());

        model.addAttribute("errorTitle", "Connection Error");
        model.addAttribute("errorMessage", 
                "Unable to connect to the warehouse service. Please ensure all services are running.");
        model.addAttribute("errorCode", "Connection Failed");
        model.addAttribute("alertType", "danger");

        return "error";
    }

    /**
     * Handle 404 - Page Not Found
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFound(NoHandlerFoundException ex, Model model) {
        logger.error("Page not found: {}", ex.getRequestURL());

        model.addAttribute("errorTitle", "Page Not Found");
        model.addAttribute("errorMessage", "The page you are looking for does not exist.");
        model.addAttribute("errorCode", "404");
        model.addAttribute("alertType", "warning");

        return "error";
    }

    /**
     * Handle all other exceptions
     */
    @ExceptionHandler(Exception.class)
    public String handleGeneralError(Exception ex, Model model) {
        logger.error("Unexpected error: ", ex);

        model.addAttribute("errorTitle", "Unexpected Error");
        model.addAttribute("errorMessage", 
                "An unexpected error occurred. Please try again or contact support if the problem persists.");
        model.addAttribute("errorCode", "Error");
        model.addAttribute("alertType", "danger");

        return "error";
    }

    /**
     * Extract a user-friendly error message from the exception response body.
     * Falls back to a default message if extraction fails.
     */
    private String extractErrorMessage(HttpClientErrorException ex, String defaultMessage) {
        try {
            String responseBody = ex.getResponseBodyAsString();
            if (responseBody != null && !responseBody.isEmpty()) {
                // Try to extract error message from JSON response
                if (responseBody.contains("\"message\"")) {
                    int start = responseBody.indexOf("\"message\"") + 11;
                    int end = responseBody.indexOf("\"", start);
                    if (end > start) {
                        return responseBody.substring(start, end);
                    }
                }
                // If not JSON, return the raw response (but limit length)
                if (responseBody.length() < 200) {
                    return responseBody;
                }
            }
        } catch (Exception e) {
            logger.debug("Could not extract error message from response", e);
        }
        return defaultMessage;
    }

    /**
     * Extract a user-friendly error message from server exception.
     */
    private String extractErrorMessage(HttpServerErrorException ex, String defaultMessage) {
        try {
            String responseBody = ex.getResponseBodyAsString();
            if (responseBody != null && !responseBody.isEmpty() && responseBody.length() < 200) {
                return responseBody;
            }
        } catch (Exception e) {
            logger.debug("Could not extract error message from response", e);
        }
        return defaultMessage;
    }
}

