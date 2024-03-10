package ca.dheri.digitalizingreceipts.service;

import ca.dheri.digitalizingreceipts.model.Item;
import ca.dheri.digitalizingreceipts.model.Receipt;
import ca.dheri.digitalizingreceipts.model.Store;
import ca.dheri.digitalizingreceipts.repository.ReceiptRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class OCRService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    @Autowired
    private WebClient.Builder webClientBuilder;
    @Autowired
    private ReceiptRepository receiptRepository;
    @Value("${ocr.host}")
    private String ocrHost;

    public void processReceipt(MultipartFile file, Principal principal) throws Exception {
        WebClient webClient = webClientBuilder.build();
        logger.debug("start");
        logger.debug(" file.getSize" + file.getSize());
        DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory("https://" + ocrHost);

        URI ocrUrl = uriBuilderFactory.uriString("/process_receipt")
                .build();

        // Assuming the OCR service expects a file with the form field 'file'
        String ocrResponse = webClient.post()
                .uri(ocrUrl)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("file", file.getResource()))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        logger.debug("ocrResponse: " + ocrResponse);

        // Assuming ocrResponse contains the necessary data to create a Receipt object
        // You will need to parse this response and map it to your Receipt model
        Receipt receipt = parseOCRResponseToReceipt(ocrResponse, principal.getName());
        logger.debug("receipt parseOCRResponseToReceipt");
        logger.debug("receipt " + receipt);
        // Save the receipt
        receiptRepository.save(receipt);
    }

    private Receipt parseOCRResponseToReceipt(String ocrResponse, String userId) {
        logger.debug(String.valueOf(ocrResponse));


        JsonNode root;
        try {
            root = objectMapper.readTree(ocrResponse);
            JsonNode ocrResponseNode = objectMapper.readTree(root.path("ocr_response").asText());


            Store store = new Store();
            store.setName(ocrResponseNode.path("store").asText());

            List<Item> items = new ArrayList<>();
            JsonNode itemsNode = ocrResponseNode.path("items");
            Iterator<String> itemNames = itemsNode.fieldNames();
            while (itemNames.hasNext()) {
                String itemName = itemNames.next();
                double itemPrice = itemsNode.path(itemName).asDouble();
                // Assuming each item's quantity is 1 since it's not specified
                Item item = new Item();
                item.setName(itemName);
                item.setPrice(itemPrice);
                item.setQuantity(1);
                items.add(item);
            }

            double total = ocrResponseNode.path("total").asDouble();

            Receipt receipt = new Receipt();
            receipt.setStore(store);
            receipt.setDateOfReceipt(new Date()); // Assuming current date for the receipt
            receipt.setItems(items);
            receipt.setTotalAmount(total);
            receipt.setUserId(userId);
            return receipt;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
