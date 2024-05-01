package com.motorcycleparts.motorcycleparts_master.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;

    @Column(length = 500)
    private String address;
    private String phoneNumBer;
    private boolean isDefault;

    private String ward;
    private String district;
    private String province;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;

    @OneToMany(mappedBy = "address")
    @JsonIgnore
    private List<Order> order;
}
