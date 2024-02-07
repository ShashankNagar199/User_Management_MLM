package com.usermanagement.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "deposit_history")
public class DepositHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "wallet_address")
    private String walletAddress;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "coin_id")
    private Long coinId;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "register_date")
    private Date registerDate;

    public DepositHistory() {
        // Default constructor
    }

    public DepositHistory(String userId, String walletAddress, String transactionId, Long coinId, Double amount, Date registerDate) {
        this.userId = userId;
        this.walletAddress = walletAddress;
        this.transactionId = transactionId;
        this.coinId = coinId;
        this.amount = amount;
        this.registerDate = registerDate;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Long getCoinId() {
        return coinId;
    }

    public void setCoinId(Long coinId) {
        this.coinId = coinId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }
}

