package com.iremayvaz.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Kullanıcı kayıt isteği yapılır.")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoUserInsert {
    // IU : Insert - Update
    // REQUEST işlemlerinde kullanılır.
    // @PostMapping veya @PutMapping işlemlerinde kullanılır
    // Validation'lar bu sınıfta yapılır.

    @NotBlank(message = "Email girilmeli!")
    @Email(message="Geçerli bir e-posta girin")
    private String email;

    @Pattern(regexp = "^5\\d{9}$", message = "Telefon numarasını başında 0 olmadan giriniz!")
    private String phoneNum;

    @Size(min=2, max=30, message = "İsim en az 2 en fazla 30 karakter uzunluğunda olabilir!")
    @NotBlank(message = "İsim girilmeli!")
    private String firstName;

    @Size(min=2, max=30, message = "Soyisim en az 2 en fazla 30 karakter uzunluğunda olabilir!")
    @NotBlank(message = "Soyisim girilmeli!")
    private String lastName;

    @NotBlank
    @Size(min = 8, max = 64)
    @Pattern(regexp="^(?=.*[A-Z])(?=.*\\d).+$",
            message="En az bir büyük harf ve bir rakam içermeli")
    private String password;

}
