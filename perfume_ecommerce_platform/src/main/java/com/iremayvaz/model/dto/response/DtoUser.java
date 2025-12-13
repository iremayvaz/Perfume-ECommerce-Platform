package com.iremayvaz.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Kullanıcı kayıt başarılı ise kullanıcı bilgileri döndürülür.")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoUser {
    private Long id;
    private String email;
    private String password;
}
