package com.usermanagement.models;

import java.util.Collection;
import java.util.List;

import javax.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
public class User  {
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

		@Id
	    @Column(name = "user_id")
	    private String userId;

	    @Column(name = "email")
	    private String email;

	    @Column(name = "password")
	    private String password;

		@Transient
		private String confirmPassword;
		@Column(name = "phone")
		private String phone;

//		@ManyToOne(fetch = FetchType.EAGER)
//		@JoinColumn(name = "referrer_id")
        @Column(name = "referrer_id")
		private String referrer;

		@Column(name = "wallet_address")
		private String walletAddress;

		@Column(name = "level")
		private Integer level;

		@Column(name = "sales")
		private Double sales;

		@Column(name = "balance")
		private Double balance;
		@ManyToOne
		@JoinColumn(name = "rank_id")
		private UserRank userRank;
		@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
		private List<SalesRecord> salesRecords;

	public User() {

	}

	public List<SalesRecord> getSalesRecords() {
		return salesRecords;
		}

		public void setSalesRecords(List<SalesRecord> salesRecords) {
		this.salesRecords = salesRecords;
		}

		public UserRank getUserRank() {
		return userRank;
		}

		public void setUserRank(UserRank userRank) {
		this.userRank = userRank;
		}
		public Double getSales() {
		return sales;
		}

		public void setSales(Double sales) {
			sales = sales;
		}

		public Double getBalance() {
		return balance;
		}

		public void setBalance(Double balance) {
		this.balance = balance;
		}

		public Integer getLevel() {
		return level;
		}

		public void setLevel(Integer level) {
		this.level = level;
		}

		public String getWalletAddress() {
		return walletAddress;
		}

		public void setWalletAddress(String walletAddress) {
		this.walletAddress = walletAddress;
		}

		public String getReferrer() {
		return referrer;
		}

		public void setReferrer(String referrer) {
		this.referrer = referrer;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getConfirmPassword() {
		return confirmPassword;
		}

		public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
		}

		public String getUserId() {
		return userId;
		}

		public void setUserId(String userId) {
		this.userId = userId;
		}

		public String getPhone() {
		return phone;
	}

		public void setPhone(String phone) {
		this.phone = phone;
	}

		public User(String email, String password) {
			super();
			this.email = email;
			this.password = password;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

	}
