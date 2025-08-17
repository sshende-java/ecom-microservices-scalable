package com.ecommerce.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequest {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private AddressDTO address;
}
