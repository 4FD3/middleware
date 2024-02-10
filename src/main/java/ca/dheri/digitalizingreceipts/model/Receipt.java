package ca.dheri.digitalizingreceipts.model;


import lombok.Data;

import java.util.List;

@Data
public class Receipt {

    private String storeName;
    private List<Item> items; // Embedding items within the receipt document
    private Double totalAmount;

    // Lombok will generate getters, setters, and constructors
}