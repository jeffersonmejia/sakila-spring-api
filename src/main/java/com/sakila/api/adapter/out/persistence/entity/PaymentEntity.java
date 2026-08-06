package com.sakila.api.adapter.out.persistence.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "payment")
public class PaymentEntity {

    @Id
    @Column(name = "payment_id")
    private Integer id;

    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @Column(name = "staff_id", nullable = false)
    private Integer staffId;

    @Column(name = "rental_id", nullable = false)
    private Integer rentalId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "payment_date", nullable = false)
    private Instant paymentDate;

    @Column(name = "uuid", nullable = false, insertable = false, updatable = false)
    private UUID uuid;

    protected PaymentEntity() {
    }

    public PaymentEntity(Integer id, Integer customerId, Integer staffId, Integer rentalId, BigDecimal amount,
            Instant paymentDate) {
        this.id = id;
        this.customerId = customerId;
        this.staffId = staffId;
        this.rentalId = rentalId;
        this.amount = amount;
        this.paymentDate = paymentDate;
    }

    public Integer getId() {
        return id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public Integer getRentalId() {
        return rentalId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Instant getPaymentDate() {
        return paymentDate;
    }
}
