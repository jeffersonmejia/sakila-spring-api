package com.sakila.api.adapter.out.persistence.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "rental")
public class RentalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rental_seq")
    @SequenceGenerator(name = "rental_seq", sequenceName = "rental_rental_id_seq", allocationSize = 1)
    @Column(name = "rental_id")
    private Integer id;

    @Column(name = "rental_date", nullable = false)
    private Instant rentalDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", nullable = false)
    private InventoryEntity inventory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;

    @Column(name = "return_date")
    private Instant returnDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", nullable = false)
    private StaffEntity staff;

    @Column(name = "last_update", nullable = false, insertable = false, updatable = false)
    private Instant lastUpdate;

    @Column(name = "uuid", nullable = false, insertable = false, updatable = false)
    private UUID uuid;

    protected RentalEntity() {
    }

    public RentalEntity(Integer id, Instant rentalDate, InventoryEntity inventory, CustomerEntity customer,
            StaffEntity staff) {
        this.id = id;
        this.rentalDate = rentalDate;
        this.inventory = inventory;
        this.customer = customer;
        this.staff = staff;
    }

    public Integer getId() {
        return id;
    }

    public Instant getRentalDate() {
        return rentalDate;
    }

    public InventoryEntity getInventory() {
        return inventory;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public Instant getReturnDate() {
        return returnDate;
    }

    public StaffEntity getStaff() {
        return staff;
    }

    public void setReturnDate(Instant returnDate) {
        this.returnDate = returnDate;
    }
}
