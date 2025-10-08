package com.iremayvaz.model.entity;

import com.iremayvaz.model.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data

// Role bilgileri
public class Role {
    private Long id;
    private RoleName roleName;
}
