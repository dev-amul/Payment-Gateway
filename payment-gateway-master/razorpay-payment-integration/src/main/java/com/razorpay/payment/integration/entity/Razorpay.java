package com.razorpay.payment.integration.entity;

import javax.persistence.*;

@Entity
public class Razorpay {

	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String amount;
	
	@Column(unique = true, nullable = true)
	private String orderid;
	
	private String paymentid;
	
	private String status;
	
	private String orderdate;
	public Razorpay() {}


	public Razorpay(long id, String amount, String orderid,String paymentid, String status, String orderdate) {
		super();
		this.id          = id;
		this.amount      = amount;
		this.orderid     = orderid; 
		this.paymentid   = paymentid;
		this.status      = status;
		this.orderdate   = orderdate;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

    public void setPaymentid(String paymentid) {
    	this.paymentid = paymentid;
    }
    public String getPaymentid() {
    	return paymentid;
    }
    
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getOrderdate() {
		return orderdate;
	}
	public void setOrderdate(String currentdate) {
		this.orderdate = currentdate;
	}

	@Override
	public String toString() {
		return "Payment [id=" + id + ", amount=" + amount + ", orderid=" + orderid
				+ ", paymentid = " + paymentid + ", status=" + status + ", currentdate=" + orderdate + "]";
	}

	
}
