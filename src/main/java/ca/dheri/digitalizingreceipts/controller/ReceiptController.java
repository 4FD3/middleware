package ca.dheri.digitalizingreceipts.controller;

import ca.dheri.digitalizingreceipts.model.Receipt;
import ca.dheri.digitalizingreceipts.repository.ReceiptRepository;
import ca.dheri.digitalizingreceipts.service.OCRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/receipts")
public class ReceiptController {

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private OCRService ocrService;

    @PostMapping("/upload")
    public String uploadReceipt(@RequestParam("file") MultipartFile file, Principal principal) {
        try {
            ocrService.processReceipt(file, principal);
            return "Receipt processed successfully";
        } catch (Exception e) {
            return "Error processing receipt: " + e.getMessage();
        }
    }
    // Endpoint to post a new receipt
    @PostMapping
    public Receipt addReceipt(@RequestBody Receipt receipt, Principal principal) {
        // Assuming the Principal name is the user's email
        receipt.setUserId(principal.getName());
        return receiptRepository.save(receipt);
    }

    // Endpoint to get receipts for the logged-in user
    @GetMapping
    public List<Receipt> getReceipts(Principal principal) {
        return receiptRepository.findByUserId(principal.getName());
    }
}
