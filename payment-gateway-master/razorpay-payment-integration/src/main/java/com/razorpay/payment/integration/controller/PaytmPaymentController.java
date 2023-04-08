package com.razorpay.payment.integration.controller;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.paytm.pg.merchant.PaytmChecksum;
import com.razorpay.payment.integration.entity.Paytm;
import com.razorpay.payment.integration.entity.PaytmDetailPojo;
import com.razorpay.payment.integration.service.PaytmService;


@Controller
public class PaytmPaymentController {

	@Autowired private PaytmDetailPojo paytmDetailPojo;
	@Autowired private PaytmService paytmService;
	@Autowired private Environment env;
	
	@GetMapping("/paytm")
	public String PaytmHome() {
		return "paytm";
	}
	 
	 @PostMapping(value = "/submitPaymentDetail")
	    public ModelAndView getRedirect(@RequestParam(name = "CUST_ID") String customerId,
	                                    @RequestParam(name = "TXN_AMOUNT") String transactionAmount,
	                                    @RequestParam(name = "ORDER_ID") String orderId) throws Exception {

	        ModelAndView modelAndView = new ModelAndView("redirect:" + paytmDetailPojo.getPaytmUrl());
	        TreeMap<String, String> parameters = new TreeMap<>();
	        paytmDetailPojo.getDetails().forEach((k, v) -> parameters.put(k, v));
	        parameters.put("MOBILE_NO", env.getProperty("paytm.mobile"));
	        parameters.put("EMAIL", env.getProperty("paytm.email"));
	        parameters.put("ORDER_ID", orderId);
	        parameters.put("TXN_AMOUNT", transactionAmount);
	        parameters.put("CUST_ID", customerId);
	        String checkSum = getCheckSum(parameters);
	        parameters.put("CHECKSUMHASH", checkSum);
	        modelAndView.addAllObjects(parameters);
	  
	        System.out.println("paytm payment object :- "+parameters);
	        return modelAndView;
	    }
	 
	 
	 @PostMapping(value = "/pgresponse")
	    public String getResponseRedirect(HttpServletRequest request, Model model) {

	        Map<String, String[]> mapData = request.getParameterMap();
	        TreeMap<String, String> parameters = new TreeMap<String, String>();
	        String paytmChecksum = "";
	        for (Entry<String, String[]> requestParamsEntry : mapData.entrySet()) {
	            if ("CHECKSUMHASH".equalsIgnoreCase(requestParamsEntry.getKey())){
	                paytmChecksum = requestParamsEntry.getValue()[0];
	            } else {
	            	parameters.put(requestParamsEntry.getKey(), requestParamsEntry.getValue()[0]);
	            }
	        }
	        String result;

	        Paytm paytm = new Paytm();
	        paytm.setBankname(parameters.get("BANKNAME")+"");
	        paytm.setBanktxid(parameters.get("BANKTXNID"));
	        paytm.setOrderid(parameters.get("ORDERID"));
	        paytm.setStatus(parameters.get("STATUS")+"");
	        paytm.setAmount(parameters.get("TXNAMOUNT"));
	        paytm.setTxndate(parameters.get("TXNDATE"));
	        paytm.setTxnid(parameters.get("TXNID"));
	        
	        boolean isValideChecksum = false;
	        System.out.println(parameters.get("BANKNAME")+"");
	        System.out.println("Payment:- "+paytm);
	        
	        System.out.println("RESULT : "+parameters.toString());
	        
	        try {
	            isValideChecksum = validateCheckSum(parameters, paytmChecksum);
	            if (isValideChecksum && parameters.containsKey("RESPCODE")) {
	                if (parameters.get("RESPCODE").equals("01")) {
	               	    paytmService.SavePayment(paytm);
	                    result = "Payment Successful";
	                } else {
	                	 paytmService.SavePayment(paytm);
	                    result = "Payment Failed";
	                }
	            } else {
	                result = "Checksum mismatched";
	            }
	        } catch (Exception e) {
	            result = e.toString();
	        }
	        model.addAttribute("result",result);
	        parameters.remove("CHECKSUMHASH");
	        model.addAttribute("parameters",parameters);
	        return "pgresponse";
	    }

	    private boolean validateCheckSum(TreeMap<String, String> parameters, String paytmChecksum) throws Exception {
	        return PaytmChecksum.verifySignature(parameters,
	        paytmDetailPojo.getMerchantKey(), paytmChecksum);
	    }
	private String getCheckSum(TreeMap<String, String> parameters) throws Exception {
		return PaytmChecksum.generateSignature(parameters, paytmDetailPojo.getMerchantKey());
	}
//	
//	@PostMapping("/PaytmPayment")
//	public String getPaytmPaymentPage(@RequestBody Map<String, Object> data) throws Exception {
//		
//		System.out.println("\npaytm gateway function has been called"); 
//		System.out.println(data);
////		System.out.println(data.toString());
////		System.out.println(data.get("orderid")); 
////		System.out.println(data.get("amount")); 
////		System.out.println(data.get("custid\n"));
//		
//		String id     = (String) data.get("orderid");
//		String amount = (String) data.get("amount");
//		String custid = (String) data.get("custid");
//		
//		System.out.println(id); 
//		System.out.println(amount); 
//		System.out.println(custid);
//	//	Long orderId = Long.parseLong(s);
//		JSONObject paytmParams = new JSONObject();
//
//		JSONObject body = new JSONObject();
//		body.put("requestType", "Payment");
//		body.put("mid", "BXgLzc35277917050419");
//		body.put("websiteName", "WEBSTAGING");
//		body.put("orderId", id);
//		body.put("callbackUrl", "http://localhost:8080/response");
//
//		JSONObject txnAmount = new JSONObject();
//		txnAmount.put("value", amount);
//		txnAmount.put("currency", "INR");
//
//		JSONObject userInfo = new JSONObject();
//		userInfo.put("custId",custid);
//		body.put("txnAmount", txnAmount);
//		body.put("userInfo", userInfo);
//
//		/*
//		* Generate checksum by parameters we have in body
//		* You can get Checksum JAR from https://developer.paytm.com/docs/checksum/
//		* Find your Merchant Key in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys
//		*/
//
//		String checksum = PaytmChecksum.generateSignature(body.toString(), "Au&jaeLBIZ_S1lDR");
//
//		JSONObject head = new JSONObject();
//		head.put("signature", checksum);
//
//		paytmParams.put("body", body);
//		paytmParams.put("head", head);
//		
//        System.out.println(body);
//        System.out.println(head);
//
//		String post_data = paytmParams.toString();
//		
//		URL url = new URL("https://securegw-stage.paytm.in/theia/api/v1/initiateTransaction?mid=BXgLzc35277917050419&orderId=id");
//		System.out.println(url);
//		String responseData = "";
//		try {
//			System.out.println("hiii");
//		    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//		    connection.setRequestMethod("POST");
//		    connection.setRequestProperty("Content-Type", "application/json");
//	        connection.setDoOutput(true);
//
//            DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
//	        requestWriter.writeBytes(post_data);
//	        requestWriter.close();
//		    
//		    InputStream is = connection.getInputStream();
//		    BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
//	    if ((responseData = responseReader.readLine()) != null) {
//	       System.out.append("Response: "+responseData);
//	}
//		 responseReader.close();
//		} catch (Exception exception) {
//		 exception.printStackTrace();
//		}
//System.out.println("\nhello");
//return "paytm1";
//	}
}
