package app.services;

import app.dao.AddressRepository;
import app.entities.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public List<Address> getFilteredAndSorted(String search, String sortBy) {
        List<Address> addresses = addressRepository.findAll();

        // Фильтрация по стране, городу или улице
        if (search != null && !search.trim().isEmpty()) {
            String s = search.trim().toLowerCase();
            addresses = addresses.stream()
                    .filter(a -> a.getCountry().toLowerCase().contains(s)
                            || a.getCity().toLowerCase().contains(s)
                            || a.getStreet().toLowerCase().contains(s))
                    .collect(Collectors.toList());
        }

        // Сортировка
        if ("city".equalsIgnoreCase(sortBy)) {
            addresses.sort(Comparator.comparing(Address::getCity));
        } else if ("street".equalsIgnoreCase(sortBy)) {
            addresses.sort(Comparator.comparing(Address::getStreet));
        } else if ("country".equalsIgnoreCase(sortBy)) {
            addresses.sort(Comparator.comparing(Address::getCountry));
        }

        return addresses;
    }
}
