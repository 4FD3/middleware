package ca.dheri.digitalizingreceipts.service;

import ca.dheri.digitalizingreceipts.config.GoogleClientConfig;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;


@Service
public class GoogleTokenVerifier {

    private final GoogleClientConfig googleClientConfig;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public GoogleTokenVerifier(GoogleClientConfig googleClientConfig) {
        this.googleClientConfig = googleClientConfig;
    }

    public UserDetails verifyToken(String idTokenString) throws GeneralSecurityException, IOException {
        logger.info("verifyToken called " + idTokenString);

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList(googleClientConfig.getClientId()))
                .build();

        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            // Extract user information from the payload
            String email = payload.getEmail();
            boolean emailVerified = payload.getEmailVerified();
            // Additional user information can be extracted here

            // Example: Create a simple user with roles for Spring Security
            UserDetails user = User.withUsername(email)
                    .password("") // OAuth2 users won't have a password.
                    .authorities("ROLE_USER") // Define authorities or roles
                    .build();

            return user;
        } else {
            // Handle or throw an appropriate exception if the token is invalid
            throw new IllegalArgumentException("Invalid ID token.");
        }
    }
}