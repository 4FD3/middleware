package ca.dheri.digitalizingreceipts.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")

public class ApiController {
    Logger logger = LoggerFactory.getLogger(ApiController.class);


    @GetMapping("/")
    public String home() {
        logger.info("api home called");
        return "home";
    }

}
