package com.n9.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "account")
@Data
@ToString
public class Account extends BaseEntity {

    @Column(name = "account_id")
    @Id
    @GeneratedValue
    private Long accountId;

    @Column(name = "amount")
    private Float amount;

    @Column(name = "name")
    private String name;


}
