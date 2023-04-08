package com.razorpay.payment.integration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.razorpay.payment.integration.entity.Razorpay;
import com.razorpay.payment.integration.repository.RazorpayRepository;

@Service
public class RazorpayService {

	@Autowired private RazorpayRepository razorpayRepository;

	public Razorpay SavePaymentObject(Razorpay payment) {	
		Razorpay payment1 = razorpayRepository.save(payment);
		return payment1;
	}
}
