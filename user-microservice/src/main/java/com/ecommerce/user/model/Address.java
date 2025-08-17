package com.ecommerce.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@Entity
//@Table(name = "addresses")
public class Address {      //address doesnt need to be an entity now, it will be part of 'users' collection

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}
