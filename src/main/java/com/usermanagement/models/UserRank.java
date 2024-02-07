package com.usermanagement.models;

import javax.persistence.*;

@Entity
@Table(name = "user_rank")
public class UserRank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rank_name")
    private String rankName;

    // Constructors, getters, and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getRankName() {
        return rankName;
    }
    public void setRankName(String rankName) {
        this.rankName = rankName;
    }
}
