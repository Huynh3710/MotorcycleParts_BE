package com.motorcycleparts.motorcycleparts_master.Dto;

import com.motorcycleparts.motorcycleparts_master.model.Order;
import com.motorcycleparts.motorcycleparts_master.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusDto {
    private long id;
    private Status status;
}