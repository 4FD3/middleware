package ca.dheri.digitalizingreceipts.controller;
import ca.dheri.digitalizingreceipts.service.GoogleTokenVerifier;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

@RestController
public class AuthenticationController {

    Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    GoogleTokenVerifier googleTokenVerifier;
    @PostMapping("/api/auth/google")
    public ResponseEntity<?> authenticateUser(@RequestBody Map<String, String> tokenPayload) {

        String token = tokenPayload.get("token");
        logger.info(" /api/auth/google authenticateUser");
        logger.info(token);

        // Verify the token

        try {
            // Your method to verify the token and extract user details
            UserDetails userDetails = verifyToken(token);
            // Your logic to create or update the user in your database

            // Authenticate the user in Spring Security context
            authenticateUserInSpringContext(userDetails);

            return ResponseEntity.ok().body("User authenticated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }

    @GetMapping("/api/auth/google")
    public void redirectToGoogle(HttpServletResponse response) throws IOException {
        String oauth2Url = "http://localhost:8080/oauth2/authorization/google";
        response.sendRedirect(oauth2Url);
    }

    private UserDetails verifyToken(String token) throws GeneralSecurityException, IOException {
        return googleTokenVerifier.verifyToken(token);
    }

    private void authenticateUserInSpringContext(UserDetails userDetails) {
        // Create an authentication object using UserDetails
        // Set the authentication in the SecurityContextHolder
    }
}
