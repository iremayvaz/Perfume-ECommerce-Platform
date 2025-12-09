package com.iremayvaz.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MeResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
}
