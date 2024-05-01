package com.motorcycleparts.motorcycleparts_master.controller;

public class SparePartsType {
    private Long id;
    private String sparePartsType;

    public SparePartsType(Long id, String sparePartsType) {
        this.id = id;
        this.sparePartsType = sparePartsType;
    }

    public Long getId() {
        return id;
    }

    public String getSparePartsType() {
        return sparePartsType;
    }
}
