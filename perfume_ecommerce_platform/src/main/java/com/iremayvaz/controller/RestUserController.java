package com.iremayvaz.controller;

import com.iremayvaz.model.dto.request.AddressRequest;
import com.iremayvaz.model.dto.response.AddressResponse;
import com.iremayvaz.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "User API", description = "Kullanıcı işlemleri")
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class RestUserController {
    private final UserService userService;

    @Operation(description = "Adres Kayıt")
    @PostMapping("/address/{user_id}")
    public AddressResponse addAdress(@PathVariable Long user_id,
                                     @RequestBody AddressRequest addressRequest) {
        return userService.addAdress(user_id, addressRequest);
    }

    @Operation(description = "Adres görüntüle")
    @GetMapping("/view/address/{user_id}")
    public List<AddressResponse> viewAddress(@PathVariable Long user_id) {
        return userService.viewAddress(user_id);
    }

    @Operation(description = "Adres düzenle")
    @PutMapping("/update/{address_id}")
    public AddressResponse updateAddress(@PathVariable(value = "address_id") @NotNull Long address_id,
                                         @RequestBody AddressRequest addressRequest) { // Address id
        return userService.updateAddress(address_id, addressRequest);
    }

    @Operation(description = "Adres sil")
    @DeleteMapping("/delete/address/{address_id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable(value = "address_id") @NotNull Long address_id) { // Address id
        userService.deleteAddress(address_id);
        return ResponseEntity.ok().build();
    }
}
