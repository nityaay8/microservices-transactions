package com.n9.entity;

import javax.persistence.*;

@Entity
@Table(name = "account")
public class Account extends BaseEntity {

    @Column(name = "account_id")
    @Id
    @GeneratedValue
    private Long accountId;

    @Column(name = "name")
    private String name;

    @Column(name = "amount")
    private Float amount;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
