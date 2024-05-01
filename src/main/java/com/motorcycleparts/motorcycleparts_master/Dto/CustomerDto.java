package com.motorcycleparts.motorcycleparts_master.Dto;

import com.motorcycleparts.motorcycleparts_master.model.user.User;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private Long id;
    private String name;
    private String phoneNumber;
    private String sex;
    private String avatar;
}
