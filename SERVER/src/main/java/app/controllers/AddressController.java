package app.controllers;

import app.entities.Address;
import app.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping
    public List<Address> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortBy
    ) {
        return addressService.getFilteredAndSorted(search, sortBy);
    }
}
