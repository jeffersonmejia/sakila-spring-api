package com.sakila.api.adapter.out.persistence.entity;

import java.time.Instant;

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
@Table(name = "store")
public class StoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "store_seq")
    @SequenceGenerator(name = "store_seq", sequenceName = "store_store_id_seq", allocationSize = 1)
    @Column(name = "store_id")
    private Integer id;

    @Column(name = "manager_staff_id", nullable = false)
    private Integer managerStaffId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private AddressEntity address;

    @Column(name = "last_update", nullable = false, insertable = false, updatable = false)
    private Instant lastUpdate;

    protected StoreEntity() {
    }

    public StoreEntity(Integer id, Integer managerStaffId, AddressEntity address) {
        this.id = id;
        this.managerStaffId = managerStaffId;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public Integer getManagerStaffId() {
        return managerStaffId;
    }

    public AddressEntity getAddress() {
        return address;
    }
}
