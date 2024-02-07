package com.usermanagement.models;

import java.sql.Date;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "referrals")
public class Referral {

	   @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

//	    @ManyToOne
//	    @JoinColumn(name = "referrer_id")
		@Column(name="referrer_id")
	    private String referrer;
//	    @ManyToOne
//	    @JoinColumn(name = "referred_user_id")
		@Column(name="referred_user_id")
	    private String referredUser;

	    @Column(name = "referral_date")
	    private LocalDateTime referralDate;

		@Column(name = "referrer_level")
		private LocalDateTime referrerLevel;

		public LocalDateTime getReferrerLevel() {
		return referrerLevel;
		}

		public void setReferrerLevel(LocalDateTime referrerLevel) {
		this.referrerLevel = referrerLevel;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getReferrer() {
			return referrer;
		}

		public void setReferrer(String referrer) {
			this.referrer = referrer;
		}

		public String getReferredUser() {
			return referredUser;
		}

		public void setReferredUser(String referredUser) {
			this.referredUser = referredUser;
		}

		public LocalDateTime getReferralDate() {
			return referralDate;
		}

		public void setReferralDate(LocalDateTime referralDate) {
			this.referralDate = referralDate;
		}
	     
}
