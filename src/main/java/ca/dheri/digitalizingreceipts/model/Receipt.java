package ca.dheri.digitalizingreceipts.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

import java.util.List;

@Data
@Document
public class Receipt {


    @Id
    private String id;
    private Store store;
    private Date dateOfReceipt;
    private List<Item> items;
    private double totalAmount;
    private String userId; // Reference to the User's ID

}