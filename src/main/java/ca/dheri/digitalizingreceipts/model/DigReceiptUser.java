package ca.dheri.digitalizingreceipts.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Data
@RequiredArgsConstructor
@Document(collection = "users")
public class DigReceiptUser {

    @Id
    @NonNull
    private String googleId;
    @NonNull
    private String name;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private String email;

    private List<Receipt> receipts; // Embedding receipts within the user document

}