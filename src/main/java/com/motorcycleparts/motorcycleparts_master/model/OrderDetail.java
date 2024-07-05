package com.motorcycleparts.motorcycleparts_master.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "order_detail")
public class OrderDetail {
    @Id
    @GeneratedValue
    private Long id;
    private int quantity;
    // Price after discount
    private Float price;

    @ManyToOne
//    @JsonBackReference
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "spareparts_id")
    private SpareParts spareParts;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order_;

}
