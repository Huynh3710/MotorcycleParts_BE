package com.motorcycleparts.motorcycleparts_master.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reivews")
public class Reviews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private Integer rating;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Reviews reviews;


    @OneToMany(mappedBy = "reviews", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Reviews> childReviews = new ArrayList<>();

    //find rp by id
    //vi: rp.add(rc)
    public void addChildReview(Reviews childReview){
        this.childReviews.add(childReview);
        childReview.setReviews(this);
    }

    @ManyToOne
    @JoinColumn(name = "spare_parts_id")
    private SpareParts spareParts;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

}
