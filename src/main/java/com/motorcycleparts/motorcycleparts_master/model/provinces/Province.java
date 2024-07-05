package com.motorcycleparts.motorcycleparts_master.model.provinces;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "provinces")
public class Province {
    @Id
    @Column(name = "code", length = 20)
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "full_name_en")
    private String fullNameEn;

    @Column(name = "code_name")
    private String codeName;

    @JoinColumn
    private Double shippingRate;

    @ManyToOne
    @JoinColumn(name = "administrative_unit_id")
    @JsonIgnore
    private AdministrativeUnit administrativeUnit;

    @ManyToOne
    @JoinColumn(name = "administrative_region_id")
    @JsonIgnore
    private AdministrativeRegion administrativeRegion;
    @JsonCreator
    public Province(@JsonProperty("code") String code, @JsonProperty("name") String name) {
        this.code = code;
        this.name = name;
    }
}
