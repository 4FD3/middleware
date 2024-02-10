package ca.dheri.digitalizingreceipts.service;


import ca.dheri.digitalizingreceipts.model.DigReceiptUser;
import ca.dheri.digitalizingreceipts.model.Receipt;
import ca.dheri.digitalizingreceipts.model.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public DigReceiptUser saveUser(DigReceiptUser user) {
        Optional<DigReceiptUser> existingUser = userRepository.findByGoogleId(user.getGoogleId());
        return existingUser.orElseGet(() -> userRepository.save(user));
    }
    public DigReceiptUser addReceiptToUser(String userId, Receipt receipt) {
        Optional<DigReceiptUser> userOptional = userRepository.findByGoogleId(userId);
        if (userOptional.isPresent()) {
            DigReceiptUser user = userOptional.get();
            user.getReceipts().add(receipt);
            userRepository.save(user);
            return user;
        } else {
            throw new RuntimeException("User not found");
        }
    }
}