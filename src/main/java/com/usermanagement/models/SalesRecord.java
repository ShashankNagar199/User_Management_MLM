package com.usermanagement.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sales_records")
public class SalesRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "product")
    private String product;

    @Column(name="interest")
    private int interest;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    // Constructors, getters, and setters

    public SalesRecord(User user, String product) {
        this.user = user;
        this.product = product;
        this.interest=0;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getInterest() {
        return interest;
    }

    public void setInterest(int interest) {
        this.interest = interest;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
