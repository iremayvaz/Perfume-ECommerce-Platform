package com.iremayvaz.service;

import com.iremayvaz.model.dto.request.AddressRequest;
import com.iremayvaz.model.dto.response.AddressResponse;
import com.iremayvaz.model.entity.Address;
import com.iremayvaz.model.entity.User;
import com.iremayvaz.model.enums.AddressType;
import com.iremayvaz.repository.AddressRepository;
import com.iremayvaz.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }

    public Long count() {
        return userRepository.count();
    }

    public User findByEmailOrNull(String email) {
        return userRepository.findByEmail(email)
                .orElse(null);
    }

    @Transactional
    public AddressResponse addAdress(Long userId, AddressRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

        Address address = new Address();
        address.setId(user.getId());
        address.setType(parseAddressType(request.getAddress_type()));
        address.setCity(request.getShippingCity());
        address.setStreet(request.getShippingStreet());
        address.setDetail(request.getShippingDetail());

        // Senin User entity’de helper var: iki tarafı da bağlar
        user.addAddress(address);

        // Cascade.ALL olduğu için user save yeter; ama address save de olur.
        userRepository.save(user);

        return mapToAddressResponse(address);
    }

    @Transactional(readOnly = true)
    public List<AddressResponse> viewAddress(Long user_id) {
        return addressRepository.findAll()
                .stream()
                .map(this::mapToAddressResponse)
                .toList();
    }

    @Transactional
    public AddressResponse updateAddress(Long addressId, AddressRequest request) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Address not found: " + addressId));

        if (request.getAddress_type() != null) {
            address.setType(parseAddressType(request.getAddress_type()));
        }
        if (request.getShippingCity() != null) {
            address.setCity(request.getShippingCity());
        }
        if (request.getShippingStreet() != null) {
            address.setStreet(request.getShippingStreet());
        }
        if (request.getShippingDetail() != null) {
            address.setDetail(request.getShippingDetail());
        }

        addressRepository.save(address);

        return mapToAddressResponse(address);
    }

    @Transactional
    public void deleteAddress(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Address not found: " + addressId));

        // orphanRemoval=true olduğu için user içinden silmek “temiz” yöntem
        User owner = address.getUser();
        if (owner != null) {
            owner.removeAddress(address);
            userRepository.save(owner);
        } else {
            addressRepository.delete(address);
        }
    }

    private AddressType parseAddressType(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("address_type cannot be null/blank");
        }
        // Örn: "HOME", "home", "Ev" gibi girişlere tolerans
        String normalized = raw.trim().toUpperCase(Locale.ROOT);

        // Eğer enum değerlerin HOME/WORK/SCHOOL gibi ise direkt parse çalışır:
        try {
            return AddressType.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            // Eğer enum’un EV/IS/OKUL gibi TR değerleri varsa buraya map koyabilirsin:
            // switch(normalized) { case "EV": return AddressType.HOME; ... }
            throw new IllegalArgumentException("Invalid address_type: " + raw);
        }
    }

    private AddressResponse mapToAddressResponse(Address address) {
        return new AddressResponse(
                address.getId(),
                address.getType().toString(),
                address.getCity(),
                address.getStreet(),
                address.getDetail()
        );
    }
}
