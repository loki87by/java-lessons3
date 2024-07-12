package Item;

import org.example.config.PersistenceConfig;
import org.example.config.TestConfig;
import org.example.item.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {TestConfig.class, PersistenceConfig.class})
@Transactional
class ItemServiceImplTest {

    @Mock
    //@Qualifier("ItemJPARepository")
    private ItemJPARepository itemJPARepository;

    @Mock
    private UrlMetadataRetriever urlMetadataRetriever;

    @InjectMocks
    private ItemServiceImpl itemService;

    Long userId = 1L;
    Long itemId = 1L;
    String url = "https://gclnk.com/BY0oD7IO";
    String resUrl = "https://www.pochta.ru/tracking?barcode=RO440456063RU";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        itemService = new ItemServiceImpl(itemJPARepository, urlMetadataRetriever);
    }
    @Test
    public void saveTest() {
        Item item = new Item();
        item.setId(itemId);
        item.setUserId(userId);
        item.setUrl(url);

        UrlMetadataRetriever.UrlMetadata meta = Mockito.mock(UrlMetadataRetriever.UrlMetadata.class);
        when(meta.getResolvedUrl()).thenReturn(resUrl);
        when(urlMetadataRetriever.retrieve(eq(url))).thenReturn(meta);

        when(itemService.save(item)).thenReturn(item);
        Item result = itemService.save(item);
        Mockito.verify(itemJPARepository, Mockito.times(1)).saveAndFlush(result);


        assertThat(result, notNullValue());
        assertThat(result.getId(), equalTo(itemId));
        assertThat(result.getUserId(), equalTo(userId));
        assertThat(result.getUrl(), equalTo(url));
        assertThat(result.getResolvedUrl(), equalTo(resUrl));
        assertThat(result.isHasImage(), equalTo(false));
        assertThat(result.isHasVideo(), equalTo(false));
        assertThat(result.isUnread(), equalTo(true));
    }

    @Test
    @Transactional
    @Rollback
    public void updateTest() {
        ModifyItemRequest req = new ModifyItemRequest();
        req.setUrl(url);
        req.setItemId(itemId);
        req.setUserId(userId);
        req.setReplaceTags(false);

        Item item = new Item();
        item.setId(itemId);
        item.setUserId(userId);
        item.setUrl("ya.ru");
        List<Item> list = List.of(item);


        UrlMetadataRetriever.UrlMetadata meta = Mockito.mock(UrlMetadataRetriever.UrlMetadata.class);
        when(meta.getResolvedUrl()).thenReturn(resUrl);
        when(urlMetadataRetriever.retrieve(anyString())).thenReturn(meta);

        Item savedItem = itemService.save(item);
        System.out.println("savedItem: "+savedItem);

        Item resItem = new Item();
        resItem.setId(itemId);
        resItem.setUserId(userId);
        resItem.setUrl(url);
        System.out.println("resItem: "+resItem);

        when(itemJPARepository.findItemsByUserId(userId)).thenReturn(list);
        /*when(itemService.save(any(Item.class))).thenReturn(item);
        System.out.println(item);*/
        //when(itemService.update(any(ModifyItemRequest.class))).thenReturn(resItem);
        //when(itemService.update(req)).thenReturn(resItem);
        when(itemService.update(any(ModifyItemRequest.class))).thenReturn(resItem);
        Item result = itemService.update(req);

        assertThat(resItem, notNullValue());
        assertEquals(resItem, result);
        assertThat(resItem.getId(), equalTo(itemId));
        assertThat(resItem.getUserId(), equalTo(userId));
        assertThat(resItem.getUrl(), equalTo(url));
        assertThat(resItem.getResolvedUrl(), equalTo(resUrl));
        assertThat(resItem.isHasImage(), equalTo(false));
        assertThat(resItem.isHasVideo(), equalTo(false));
        assertThat(resItem.isUnread(), equalTo(true));
    }
}
