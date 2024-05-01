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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "brand_motor")
//Hãng xe máy (Honda, Yamaha, Suzuki, ...)
public class BrandMotor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String image;

    @OneToMany(mappedBy = "brandMotor")
    @JsonIgnore
    List<MotorType> motorTypes;
}
