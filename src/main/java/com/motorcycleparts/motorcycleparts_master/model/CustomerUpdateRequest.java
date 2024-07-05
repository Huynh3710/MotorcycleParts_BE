package com.motorcycleparts.motorcycleparts_master.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerUpdateRequest {
    private Long id;
    private String name;
    private String phoneNumber;
    private String sex;
//    private String avatar;

}
