package com.usermanagement.models;

import java.sql.Date;

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

	    @ManyToOne
	    @JoinColumn(name = "referrer_id")
	    private User referrer;

	    @ManyToOne
	    @JoinColumn(name = "referred_user_id")
	    private User referredUser;

	    @Column(name = "referral_date")
	    private Date referralDate;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public User getReferrer() {
			return referrer;
		}

		public void setReferrer(User referrer) {
			this.referrer = referrer;
		}

		public User getReferredUser() {
			return referredUser;
		}

		public void setReferredUser(User referredUser) {
			this.referredUser = referredUser;
		}

		public Date getReferralDate() {
			return referralDate;
		}

		public void setReferralDate(Date referralDate) {
			this.referralDate = referralDate;
		}
	     
}
