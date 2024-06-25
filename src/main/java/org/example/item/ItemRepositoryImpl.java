package org.example.item;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

public class ItemRepositoryImpl  implements  ItemRepositoryCustom{
    private final ItemRepository itemRepository;
    public ItemRepositoryImpl(@Lazy ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public List<ItemInfoWithUrlState> findAndCheckLinkValidityAndSaveStatus () {
        return itemRepository.findAll().stream()
                .map(item -> new ItemInfoWithUrlState(item, checkUrl(item.getUrl())))
                .collect(Collectors.toList());
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
