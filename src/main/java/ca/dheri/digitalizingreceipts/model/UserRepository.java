package ca.dheri.digitalizingreceipts.model;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<DigReceiptUser, String> {
    Optional<DigReceiptUser> findByGoogleId(String googleId);
}
