package com.iremayvaz.model.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AddressRequest {
    private String address_type;
    private String shippingCity;
    private String shippingStreet;
    private String shippingDetail;
}
