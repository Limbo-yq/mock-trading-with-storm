package com.limbo.actual.mocktrading.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity
@Table
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 姓名
     */
    @Column
    private String name;

    /**
     * 存款
     */
    @Column
    private Long deposit;
}
