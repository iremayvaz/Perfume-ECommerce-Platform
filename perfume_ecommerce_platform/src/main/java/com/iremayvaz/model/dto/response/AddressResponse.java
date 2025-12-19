package com.iremayvaz.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AddressResponse {
    private Long id;
    private String address_type;
    private String shippingCity;
    private String shippingStreet;
    private String shippingDetail;
}
