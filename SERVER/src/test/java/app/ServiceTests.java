package app;

import app.dao.*;
import app.dto.ProductSalesReportDTO;
import app.entities.*;
import app.services.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceTests {

    // Репозитории
    @Mock UserRepository userRepo;
    @Mock ProductRepository productRepo;
    @Mock OrderRepository orderRepo;
    @Mock OrderDetailsRepository orderDetailsRepo;
    @Mock SupplierRepository supplierRepo;
    @Mock CategoryRepository categoryRepo;
    @Mock AddressRepository addressRepo;
    @Mock PasswordEncoder encoder;

    // Сервисы
    @InjectMocks UserService userService;
    @InjectMocks AuthService authService;
    @InjectMocks ProductService productService;
    @InjectMocks OrderService orderService;
    @InjectMocks SupplierService supplierService;
    @InjectMocks CategoryService categoryService;
    @InjectMocks AddressService addressService;
    @InjectMocks ReportService reportService;
    @InjectMocks StatisticsService statisticsService;

    // 1
    @Test
    void testGetAllUsers() {
        when(userRepo.findAll()).thenReturn(List.of());
        var result = userService.getAll("", "");
        assertTrue(result.isEmpty());
    }

    // 2
    @Test
    void testDeleteCategory() {
        doNothing().when(categoryRepo).deleteById(5);
        categoryService.deleteCategory(5);
        verify(categoryRepo, times(1)).deleteById(5);
    }

    // 3
    @Test
    void testFindUserByLogin() {
        User user = new User();
        user.setLogin("admin");

        when(userRepo.findByLogin("admin")).thenReturn(Optional.of(user));

        Optional<User> found = userService.findByLogin("admin");
        assertTrue(found.isPresent());
        assertEquals("admin", found.get().getLogin());
    }

    // 4
    @Test
    void testGetFilteredProducts() {
        when(productRepo.findAll()).thenReturn(List.of());
        var result = productService.getFilteredAndSorted("", "");
        assertTrue(result.isEmpty());
    }

    // 5
    @Test
    void testCreateProduct() {
        Product p = new Product();
        p.setName("Test");
        p.setPrice(100);
        p.setQuantity(10);
        p.setCategory(new Category());
        p.setSupplier(new Supplier());

        when(productRepo.save(p)).thenReturn(p);
        var saved = productService.create(p);
        assertEquals("Test", saved.getName());
    }

    // 6
    @Test
    void testGetAllOrders() {
        when(orderRepo.findAll()).thenReturn(List.of());
        var result = orderService.getAllOrders();
        assertTrue(result.isEmpty());
    }

    // 7
    @Test
    void testGetAllCategories() {
        when(categoryRepo.findAll()).thenReturn(List.of());
        var result = categoryService.getAllCategories();
        assertTrue(result.isEmpty());
    }

    // 8
    @Test
    void testGetSuppliersFiltered() {
        when(supplierRepo.findAll()).thenReturn(List.of());
        var result = supplierService.getFilteredAndSorted("", "");
        assertTrue(result.isEmpty());
    }

    // 9
    @Test
    void testGetAddressesFiltered() {
        when(addressRepo.findAll()).thenReturn(List.of());
        var result = addressService.getFilteredAndSorted("", "");
        assertTrue(result.isEmpty());
    }

    // 10
    @Test
    void testReportByDate() {
        List<ProductSalesReportDTO> report = reportService.getReportByDate(LocalDate.now());
        assertTrue(report.isEmpty());
    }
}
