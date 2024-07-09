package Item;

import org.example.item.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {
    private final Long userId = 1L;
    private final String url = "john@example.com";

    private MockMvc mockMvc;
    @Mock
    private ItemServiceImpl itemService;
    @InjectMocks
    private ItemController controller;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testGetItems() throws Exception {
        int limit = 10;
        String sort = "newest";
        String contentType = "all";
        String state = "unread";
        GetItemsRequest req = new GetItemsRequest(userId, state, contentType, sort, limit);
        List<ItemDTO> itemList = new ArrayList<>();

        when(itemService.findByUserId(req)).thenReturn(itemList);

        mockMvc.perform(MockMvcRequestBuilders.get("/items")
                        .header("X-Later-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(itemList.size())));

        verify(itemService, times(1)).findByUserId(req);
    }

    @Test
    public void testSetItem() throws Exception {
        Item item = new Item();
        item.setUserId(userId);
        item.setUrl(url);
        item.setId(1L);

        when(itemService.save(any(Item.class))).thenReturn(item);

        mockMvc.perform(MockMvcRequestBuilders.post("/items")
                        .header("X-Later-User-Id", userId)
                        .param("url", "john@example.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.url").value("john@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"));

        verify(itemService, times(1)).save(any(Item.class));
    }

    //not working, need update
    /*@Test
    void updItemTest() throws Exception {
        Item item = new Item();
        item.setUserId(userId);
        item.setUrl(url);
        item.setId(1L);

        when(itemService.save(any(Item.class))).thenReturn(item);
        System.out.println(item);

        ModifyItemRequest mir = new ModifyItemRequest();
        mir.setUserId(item.getUserId());
        mir.setItemId(item.getId());
        mir.setUrl("example.com");
        System.out.println(mir);

        when(itemService.update(any(ModifyItemRequest.class))).thenReturn(item);

        mockMvc.perform(MockMvcRequestBuilders.patch("/items")
                        .header("X-Later-User-Id", userId)
                        .param("url", "example.com")
                        .param("itemId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.url", is("example.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"));

        verify(itemService, times(1)).update(any(ModifyItemRequest.class));
    }*/

    @Test
    void testDeleteItem() throws Exception {
        long id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/items/{id}", id)
                .header("X-Later-User-Id", userId))
                .andExpect(status().isOk());

        verify(itemService, times(1)).deleteByUserIdAndItemId(userId, id);
    }
}