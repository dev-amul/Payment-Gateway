package com.razorpay.payment.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.razorpay.payment.integration.entity.Razorpay;

@Repository
public interface RazorpayRepository extends JpaRepository<Razorpay,Long>{

	public Razorpay findByOrderid(String orderid);
}
