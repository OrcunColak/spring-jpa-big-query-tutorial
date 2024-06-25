package com.colak.springtutorial.controller;

import com.colak.springtutorial.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // http://localhost:8080/product
    @GetMapping("/product")
    public ResponseEntity<StreamingResponseBody> report() {
        StreamingResponseBody body = productService.getResult();
        return ResponseEntity.ok(body);
    }
}
