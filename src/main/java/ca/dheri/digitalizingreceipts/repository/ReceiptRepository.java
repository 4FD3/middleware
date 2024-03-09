package ca.dheri.digitalizingreceipts.repository;

import ca.dheri.digitalizingreceipts.model.Receipt;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReceiptRepository extends MongoRepository<Receipt, String> {
    List<Receipt> findByUserId(String userId);
}
