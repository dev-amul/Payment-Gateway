/**
 *
 */

 // first request to server to create Order

 const myPayment = () => {

    console.log("payment started...");
    var amount = $("#subscription_amount").val();
    console.log(amount);
 //code
 //from here we are send request to server

$.ajax({
  url:"/razorpaypaymentgateway",
  data:JSON.stringify({amount:amount,info:"order_request"}),
  contentType:"application/json",//we will use  this because we're using utf-8 meta version/view of html
  type:"POST",
  dataType:"json",
  success:function(response){
    //invoked where success
    console.log(response);

    if(response.status == "created"){
      //open payment form
      let options={
        key:'rzp_test_Sp0TxZ70PsKMQg',
        amount: response.amount,
        currency:'INR',
        name:'Food For Foody',
        description:'Pay for order',
        image:'https://mir-s3-cdn-cf.behance.net/projects/404/c3a1e861370751.Y3JvcCw5MTgsNzE4LDM5OCwyNjA.jpg',
        order_id: response.id,

        handler:function(response){
	      console.log(response.razorpay_payment_id);
	      console.log(response.razorpay_order_id);
	      console.log(response.razorpay_signature);
          console.log("payment successfull!! ");
          updatePaymentOnServer(response.razorpay_payment_id,response.razorpay_order_id,"paid");
        },
        prefill: {
        name: "",
        email: "",
        contact: "",
       },

       notes: {
        address: "aakash bhaba food for foody ",
      },

      theme: {
        "color": "#3399cc",
      },
    };

    let rzp = new Razorpay(options);
    rzp.on('payment.failed', function (response){
          swal("Something missMatched!", "Ops payment failed", "error");
      });
     rzp.open();
    }
  },
  error:function(error){
    console.log(error)
    alert("something went wrogn !!")
  },
 });
};

//update data on server
function updatePaymentOnServer(payment_id,order_id,status)
{
	console.log("update data on server");
	
	$.ajax({		
		url:"/updatepayment",
		data:JSON.stringify({
			payment_id: payment_id,
			order_id: order_id,
			status: status,
	     }),
	    
	    contentType: "application/json",
	    type: "POST",
	    dataType: "json",
	    
	    success:function(response){
	         swal("Payment Done", "Payment Done Successfully", "success");
	    },
	    error:function(error){
		   swal("Something missMatched!", "payment done, but unable to update date", "error");
	    }
	});
	
};
