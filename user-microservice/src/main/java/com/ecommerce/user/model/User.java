package com.ecommerce.user.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")     //marks this class as collection of documents
public class User {

    @Id
    private String id;      //mongodb doesnt support long datatype

    private String firstName;
    private String lastName;

    @Indexed(unique = true)
    private String email;
    private String phone;
    private UserRole role = UserRole.CUSTOMER;

//    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
