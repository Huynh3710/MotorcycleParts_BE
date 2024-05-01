package com.motorcycleparts.motorcycleparts_master.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "part-motortype")
public class Parts_MotorType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "motor_type_id")
    private MotorType motorType;

    @ManyToOne
    @JoinColumn(name = "part_id")
    private SpareParts spareParts;
}
