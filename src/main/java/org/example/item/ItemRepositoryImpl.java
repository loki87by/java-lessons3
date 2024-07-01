package org.example.item;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

public class ItemRepositoryImpl  {
    private final ItemRepository itemRepository;
    public ItemRepositoryImpl(@Lazy ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    private String checkUrl(String url) {
        try {

                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
                HttpStatusCode statusCode = response.getStatusCode();
                return statusCode.toString();

        } catch (HttpServerErrorException | ResourceAccessException e) {
            return  "500";
        }
    }
}
