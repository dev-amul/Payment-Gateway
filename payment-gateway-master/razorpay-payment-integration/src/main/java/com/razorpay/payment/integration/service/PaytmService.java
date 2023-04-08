package com.razorpay.payment.integration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.razorpay.payment.integration.entity.Paytm;
import com.razorpay.payment.integration.repository.PaytmRepository;

@Service
public class PaytmService {

	@Autowired private PaytmRepository paytmRepository;

	public Paytm SavePayment(Paytm paytm) {
		Paytm p = paytmRepository.save(paytm);
		return p;
	}
}
