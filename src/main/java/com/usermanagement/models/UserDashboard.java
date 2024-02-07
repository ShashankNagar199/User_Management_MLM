package com.usermanagement.models;

import javax.persistence.*;

@Entity
@Table(name = "user_dashboard")
public class UserDashboard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "reward_amount_for_day")
    private Double rewardAmountForDay;

    @Column(name = "current_balance")
    private Double currentBalance;

    @Column(name = "user_rank")
    private String rank;

    @Column(name = "points_until_next_rank")
    private Integer pointsUntilNextRank;

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

    public Double getRewardAmountForDay() {
        return rewardAmountForDay;
    }

    public void setRewardAmountForDay(Double rewardAmountForDay) {
        this.rewardAmountForDay = rewardAmountForDay;
    }

    public Double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Integer getPointsUntilNextRank() {
        return pointsUntilNextRank;
    }

    public void setPointsUntilNextRank(Integer pointsUntilNextRank) {
        this.pointsUntilNextRank = pointsUntilNextRank;
    }
}
