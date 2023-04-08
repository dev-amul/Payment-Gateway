package com.razorpay.payment.integration.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Paytm {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String bankname;
	
	private String banktxid;
	
	private String orderid;
	
	private String status;
	
	private String amount;
	
	private String txndate;
	
	private String txnid;

	public Paytm() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Paytm(long id, String bankname, String banktxid, String orderid, String status, String amount,
			String txndate, String txnid) {
		super();
		this.id = id;
		this.bankname = bankname;
		this.banktxid = banktxid;
		this.orderid = orderid;
		this.status = status;
		this.amount = amount;
		this.txndate = txndate;
		this.txnid = txnid;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getBanktxid() {
		return banktxid;
	}

	public void setBanktxid(String banktxid) {
		this.banktxid = banktxid;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getTxndate() {
		return txndate;
	}

	public void setTxndate(String txndate) {
		this.txndate = txndate;
	}

	public String getTxnid() {
		return txnid;
	}

	public void setTxnid(String txnid) {
		this.txnid = txnid;
	}

	@Override
	public String toString() {
		return "Paytm [id=" + id + ", bankname=" + bankname + ", banktxid=" + banktxid + ", orderid=" + orderid
				+ ", status=" + status + ", amount=" + amount + ", txndate=" + txndate + ", txnid=" + txnid + "]";
	}

}
