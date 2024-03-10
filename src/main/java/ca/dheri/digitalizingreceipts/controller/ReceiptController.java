package ca.dheri.digitalizingreceipts.controller;

import ca.dheri.digitalizingreceipts.model.Receipt;
import ca.dheri.digitalizingreceipts.repository.ReceiptRepository;
import ca.dheri.digitalizingreceipts.service.OCRService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/receipts")
public class ReceiptController {

    Logger logger = LoggerFactory.getLogger(ReceiptController.class);
    @Autowired
    private ReceiptRepository receiptRepository;
    @Autowired
    private OCRService ocrService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadReceipt(@RequestParam("file") MultipartFile file, Principal principal) {
        logger.debug("uploadReceipt ");
        logger.debug("principal: " + principal.toString());
        logger.debug("MultipartFile file: " + file);
        try {
            ocrService.processReceipt(file, principal);
            return ResponseEntity.ok().body(Map.of("message", "Receipt processed successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error processing receipt: " + e.getMessage()));
        }
    }

    // Endpoint to post a new receipt
    @PostMapping
    public Receipt addReceipt(@RequestBody Receipt receipt, Principal principal) {
        receipt.setUserId(principal.getName());
        return receiptRepository.save(receipt);
    }

    // Endpoint to get receipts for the logged-in user
    @GetMapping
    public List<Receipt> getReceipts(Principal principal) {
        return receiptRepository.findByUserId(principal.getName());
    }
}
