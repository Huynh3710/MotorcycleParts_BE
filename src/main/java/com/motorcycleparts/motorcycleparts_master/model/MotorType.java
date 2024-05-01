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
@Table(name = "motor_type")
//Tên xe máy (SH, Wave, Exciter, ...)
public class MotorType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String carYear;
    private String image;

    @ManyToOne
    @JoinColumn(name = "brand_motor_id")
    @JsonIgnore
    private BrandMotor brandMotor;

    @OneToMany(mappedBy = "motorType")
    @JsonIgnore
    private List<Parts_MotorType> partMotorTypes;
}
