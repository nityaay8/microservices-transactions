package com.n9.dto;

public class TransactionDTO {

    private Long fromActId;

    private Float amount;

    private Long toActId;

    public Long getFromActId() {
        return fromActId;
    }

    public void setFromActId(Long fromActId) {
        this.fromActId = fromActId;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Long getToActId() {
        return toActId;
    }

    public void setToActId(Long toActId) {
        this.toActId = toActId;
    }
}
