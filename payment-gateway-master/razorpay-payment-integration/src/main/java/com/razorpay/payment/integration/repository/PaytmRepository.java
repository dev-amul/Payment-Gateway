package com.razorpay.payment.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.razorpay.payment.integration.entity.Paytm;

@Repository
public interface PaytmRepository extends JpaRepository<Paytm,Long>{

}
