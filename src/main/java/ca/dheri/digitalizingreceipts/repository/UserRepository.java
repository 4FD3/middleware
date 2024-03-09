package ca.dheri.digitalizingreceipts.repository;

import ca.dheri.digitalizingreceipts.model.DigReceiptUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<DigReceiptUser, String> {
    Optional<DigReceiptUser> findByGoogleId(String googleId);
}
