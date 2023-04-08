package com.razorpay.payment.integration.controller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.razorpay.*;
import com.razorpay.payment.integration.service.RazorpayService;
import com.razorpay.payment.integration.entity.Razorpay;
import com.razorpay.payment.integration.repository.RazorpayRepository;

@Controller
public class RazorpayController {

	@Autowired private RazorpayService paymentService;
	@Autowired private RazorpayRepository repository;
	
	
	@GetMapping("/razorpay") 
	public String GetPaymentHomePage(Model model) {
	//	model.addAttribute("payment", new Payment());
		return "razorpay";
	}
	
	/*------------------------------------- creating payment api for buyer ------------------------------------------- */
	@PostMapping("/razorpaypaymentgateway")
	@ResponseBody
	public String Order(@RequestBody Map<String,Object> data) throws RazorpayException{
	
		int amt = Integer.parseInt(data.get("amount").toString());
		
		System.out.println("your current data:- "+data);
		
		RazorpayClient razorpay = new RazorpayClient("rzp_test_Sp0TxZ70PsKMQg", "hM6satmKx1iEZxa3TezeCJuW");
//creating an order 
		JSONObject orderRequest = new JSONObject();
		orderRequest.put("amount", amt*100);// amount in the smallest currency unit
		orderRequest.put("currency", "INR");
		orderRequest.put("receipt", "order_rcptid_11");
		
		Order order = razorpay.Orders.create(orderRequest);	
		
		//String order_id = order.get("id");
		//fetching order details using order_id
		//Order order1 = razorpay.Orders.fetch("order_IWqiYY6h0D0UYg");
		//System.out.println("refunded order status is:- "+order1);
		
		SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
		Date date = new Date();
		
		Razorpay payment = new Razorpay();
		payment.setAmount(order.get("amount")+"");
		payment.setOrderid(order.get("id")); 
		payment.setStatus(order.get("status")); 
		payment.setOrderdate(s.format(date));
		
		System.out.println("payment detail:- "+payment);
		Razorpay payment1 = paymentService.SavePaymentObject(payment);
		System.out.println("payment detail:- "+payment1);
		
		return order.toString();
	}
	
	@PostMapping("/updatepayment")
	public ResponseEntity<?> updatepayment(@RequestBody Map<String, Object> data){
		System.out.println("update payment created"+data);
		
		Razorpay payment = this.repository.findByOrderid(data.get("order_id").toString());
		payment.setPaymentid(data.get("payment_id").toString());
		payment.setStatus(data.get("status").toString());
		repository.save(payment);
		
		System.out.println("data successfully updated: ");	
		return ResponseEntity.ok(Map.of("msg","updated"));
	}
	
	
	
	
	@GetMapping("/googlePay") 
	public String GooglePay(Model model) {
	//	model.addAttribute("payment", new Payment());
		return "googlepay";
	}

}
