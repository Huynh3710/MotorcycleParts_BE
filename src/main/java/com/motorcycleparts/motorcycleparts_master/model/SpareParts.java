package com.motorcycleparts.motorcycleparts_master.model;


//phụ tùng

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


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

    @Column(columnDefinition = "integer default 0")
    private int sellNumber;

    @Column(length = 1000)
    private String description;
    private float unitPrice;
    private String image;
    //khối lượng
    private float weight;
    //kích thước
    private String size;
    //tuổi thọ trung bình
    private float averageLifespan;

    //FLOAT cho null
    //công suất (nếu có)
    private Float wattage;
    //đện áp (nếu có)
    private Float voltage;

    //loại của phụ tùng đó (ví dụ: đèn led, đèn halogen, đèn xenon)
    private String type;
    //năm sản xuất
    private int year;

    //nơi xuất xứ
    private String origin;

    //số lượng còn lại
    private int inventory;
    //số sao đánh giá
    private float start;

    //Bảo hành;
    private Float warranty;

    //Chứng chỉ ISO
    private String iso;

    private String status;

    private String brandMotor;

    @ManyToOne(fetch = FetchType.EAGER)
//    @JsonIgnore
    @JoinColumn(name = "spare_part_type_id")
    private SparePartsType sparePartsType;


    @ManyToOne
//    @JsonIgnore
    @JoinColumn(name = "brand_id")
    private BrandParts brandParts;

    @OneToMany(mappedBy = "spareParts", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetail;

    @OneToMany(mappedBy = "spareParts", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Parts_MotorType> partMotorTypes;

    @OneToMany(mappedBy = "spareParts", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reviews> reviews;

    @ManyToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;
}
