package ca.dheri.digitalizingreceipts.service;

import ca.dheri.digitalizingreceipts.controller.ApiController;
import ca.dheri.digitalizingreceipts.model.Receipt;
import ca.dheri.digitalizingreceipts.repository.ReceiptRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.Principal;

@Service
public class OCRService {
    Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private ReceiptRepository receiptRepository;

    @Value("${ocr.url}")
    private String ocrUrl;

    public void processReceipt(MultipartFile file, Principal principal) throws Exception {
        WebClient webClient = webClientBuilder.build();
        logger.debug(String.valueOf(file));

        // Assuming the OCR service expects a file with the form field 'file'
        String ocrResponse = webClient.post()
                .uri(ocrUrl)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("file", file.getResource()))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // Assuming ocrResponse contains the necessary data to create a Receipt object
        // You will need to parse this response and map it to your Receipt model
        Receipt receipt = parseOCRResponseToReceipt(ocrResponse, principal.getName());

        // Save the receipt
        receiptRepository.save(receipt);
    }

    private Receipt parseOCRResponseToReceipt(String ocrResponse, String userId) {
        logger.debug(String.valueOf(ocrResponse));

        Receipt receipt = new Receipt();
        receipt.setUserId(userId);
        // Set other fields based on OCR response
        return receipt;
    }
}
