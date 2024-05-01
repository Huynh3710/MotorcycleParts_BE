package com.motorcycleparts.motorcycleparts_master.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "brand_parts")
public class BrandParts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String image;
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "brandParts")
    List<SpareParts> spareParts;

}
