package com.nexdin.clothingstore.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customers")
public class Customers {
    @Id
    private String customerID;
    private String fullName;
    private String email;
    private String phone;
    private String address;
}
