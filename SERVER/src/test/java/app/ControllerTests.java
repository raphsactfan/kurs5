package app;

import app.controllers.*;
import app.entities.*;
import app.services.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(controllers = {
        UserController.class,
        ProductController.class,
        CategoryController.class,
        SupplierController.class,
        AddressController.class,
        OrderController.class,
        ReportController.class,
        StatisticsController.class
})
@AutoConfigureMockMvc(addFilters = false)
class ControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private UserService userService;
    @MockBean private ProductService productService;
    @MockBean private CategoryService categoryService;
    @MockBean private SupplierService supplierService;
    @MockBean private AddressService addressService;
    @MockBean private OrderService orderService;
    @MockBean private ReportService reportService;
    @MockBean private StatisticsService statisticsService;

    // 1
    @Test
    void testCreateUser() throws Exception {
        User user = new User();
        user.setId(1);
        user.setLogin("newuser");
        user.setPassword("123456");
        user.setRole("USER");

        Mockito.when(userService.create(Mockito.any(User.class))).thenReturn(user);

        String json = """
            {
              "login": "newuser",
              "password": "123456",
              "role": "USER"
            }
            """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("newuser"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    // 2
    @Test
    void testGetFilteredProducts() throws Exception {
        Mockito.when(productService.getFilteredAndSorted("", "")).thenReturn(List.of());
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk());
    }

    // 3
    @Test
    void testGetAllCategories() throws Exception {
        Mockito.when(categoryService.getAllCategories()).thenReturn(List.of());
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk());
    }

    // 4
    @Test
    void testGetAllSuppliers() throws Exception {
        Mockito.when(supplierService.getFilteredAndSorted("", "")).thenReturn(List.of());
        mockMvc.perform(get("/api/suppliers"))
                .andExpect(status().isOk());
    }

    // 5
    @Test
    void testGetAllAddresses() throws Exception {
        Mockito.when(addressService.getFilteredAndSorted("", "")).thenReturn(List.of());
        mockMvc.perform(get("/api/addresses"))
                .andExpect(status().isOk());
    }

    // 6
    @Test
    void testGetAllOrders() throws Exception {
        Mockito.when(orderService.getAllOrders()).thenReturn(List.of());
        mockMvc.perform(get("/api/orders/all"))
                .andExpect(status().isOk());
    }

    // 7
    @Test
    void testDeleteOrder() throws Exception {
        Mockito.doNothing().when(orderService).deleteOrder(1);
        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isOk());
    }

    // 8
    @Test
    void testReportByDate() throws Exception {
        Mockito.when(reportService.getReportByDate(Mockito.any())).thenReturn(List.of());
        mockMvc.perform(get("/api/report?date=2025-05-01"))
                .andExpect(status().isOk());
    }

    // 9
    @Test
    void testGetCategoryStats() throws Exception {
        Mockito.when(statisticsService.getCategoryStats()).thenReturn(Map.of());
        mockMvc.perform(get("/api/statistics/categories"))
                .andExpect(status().isOk());
    }

    // 10
    @Test
    void testGetTopUsersStats() throws Exception {
        Mockito.when(statisticsService.getTopUsersByOrders()).thenReturn(Map.of());
        mockMvc.perform(get("/api/statistics/top-users"))
                .andExpect(status().isOk());
    }
}
