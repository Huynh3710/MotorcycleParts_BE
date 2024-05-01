package com.motorcycleparts.motorcycleparts_master.model.provinces;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequest {
    private Long customerId;
    private String provinceCode;
    private String districtCode;
    private String wardCode;
    private String addressDetail;
    private String fullName;
    private String phoneNumBer;
//    private boolean isDefault;
}
