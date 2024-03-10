package ca.dheri.digitalizingreceipts.config;

import ca.dheri.digitalizingreceipts.model.DigReceiptUser;
import ca.dheri.digitalizingreceipts.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEvents {
    Logger logger = LoggerFactory.getLogger(AuthenticationEvents.class);

    @Autowired
    UserService userService;

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
        logger.info(" AuthenticationEvents onSuccess called");
        Authentication authentication = success.getAuthentication();

        DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
        String googleId = oidcUser.getSubject();
        String email = oidcUser.getEmail();
        String name = oidcUser.getFullName();
        String fName = oidcUser.getFullName();
        String lName = oidcUser.getFullName();

        DigReceiptUser user = new DigReceiptUser(googleId, name, fName, lName, email);
        logger.debug(user.toString());
        userService.saveUser(user);


    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent failures) {
        logger.error("AuthenticationEvents onFailure called");
    }
}

