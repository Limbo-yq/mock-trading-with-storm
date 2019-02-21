package com.limbo.actual.mocktrading.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "store")
@Table
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 商店名称
     */
    @Column
    private String name;

    /**
     * 所有者id
     */
    @Column
    private Long ownerId;


}
