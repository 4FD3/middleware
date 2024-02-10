package ca.dheri.digitalizingreceipts.model;

import lombok.Data;

@Data
public class Item {

    private String name;
    private Integer quantity;
    private Double price; // Price per item

    // Lombok will generate getters, setters, and constructors
}