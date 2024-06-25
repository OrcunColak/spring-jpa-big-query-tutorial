package com.colak.springtutorial.service;

import com.colak.springtutorial.entity.Product;
import com.colak.springtutorial.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final TransactionTemplate transactionTemplate;
    private final ProductRepository productRepository;
    private final EntityManager entityManager;
    private final ObjectMapper objectMapper;

    public StreamingResponseBody getResult() {
        return outputStream ->
                // Since we are using a stream to return the data, we need to manually control the transaction using
                // the TransactionTemplate.
                transactionTemplate.execute(
                        new TransactionCallbackWithoutResult() {
                            @Override
                            protected void doInTransactionWithoutResult(TransactionStatus status) {
                                fillStream(outputStream);
                            }
                        });
    }

    private void fillStream(OutputStream outputStream) {
        try (Stream<Product> productStream = productRepository.findAllBy()) {
            productStream.forEach(
                    product -> {
                        try {
                            String json = objectMapper.writeValueAsString(product);
                            outputStream.write(json.getBytes(StandardCharsets.UTF_8));

                            // To ensure that JPA does not keep the entity in memory after processing it, we manually detach it using the EntityManager.
                            entityManager.detach(product);

                        } catch (IOException exception) {
                            throw new RuntimeException(exception);
                        }
                    });
        }
    }
}
