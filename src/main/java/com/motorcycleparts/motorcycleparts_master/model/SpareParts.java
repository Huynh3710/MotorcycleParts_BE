package com.motorcycleparts.motorcycleparts_master.model;


//phụ tùng

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "spare_parts")
public class SpareParts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long sellNumber;

    @Column(length = 1000)
    private String description;
    private Double unitPrice;
    private String image;
    //số lượng còn lại
    private Long quantityRe;
    private float start;


    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "spare_part_type_id")
    private SparePartsType sparePartsType;


    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "brand_id")
    private BrandParts brandParts;



//    @OneToOne
//    @JoinColumn(name = "specifications_id", referencedColumnName = "id")
//    Specifications specifications;

}
