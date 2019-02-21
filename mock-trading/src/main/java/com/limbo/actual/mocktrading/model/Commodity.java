package com.limbo.actual.mocktrading.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "commodity")
@Table
public class Commodity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 商品名称
     */
    @Column
    private String name;

    /**
     * 价格
     */
    @Column
    private Long price;

    /**
     * 商店id
     */
    @Column
    private Long storeId;
}
