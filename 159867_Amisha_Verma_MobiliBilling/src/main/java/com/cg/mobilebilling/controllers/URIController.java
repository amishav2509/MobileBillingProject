package com.cg.mobilebilling.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cg.mobilebilling.beans.Customer;


@Controller
public class URIController {
	Customer customer;
	@RequestMapping("/")
	public String getIndexPage(){
		return "indexPage";
	}
	@RequestMapping("/registerCustomer")
	public String registerCustomerPage() {
		return "registerCustomerPage";
	}
	@RequestMapping("/openPostpaidAccount")
	public String createPostpaidAccountPage() {
		return "createPostpaidAccountPage";
	}
	@RequestMapping("/generateMonthlyBill")
	public String generateMobileBillPage() {
		return "generateMobileBillPage";
	}
	@RequestMapping("/getCustomerDetails")
	public String getCustomerDetailsPage() {
		return "getCustomerDetailsPage";
	}
	@RequestMapping("/getPostpaidAccountDetails")
	public String getPostpaidAccountDetailsPage() {
		return "getPostpaidAccountDetailsPage";
	}
	@RequestMapping("/getCustomerAllPostpaidAccountDetails")
	public String getCustomerAllPostpaidAccountDetailsPage() {
		return "getCustomerAllPostpaidAccountDetailsPage";
	}
	@RequestMapping("/getMobileBillDetails")
	public String getMobileBillDetailsPage() {
		return "getMobileBillDetailsPage";
	}
	@RequestMapping("/getMobileAllBillDetails")
	public String getMobileAllBillDetailsPage() {
		return "getMobileAllBillDetailsPage";
	}
	@RequestMapping("/changePlan")
	public String changePlanPage() {
		return "changePlanPage";
	}
	@RequestMapping("/getCustomerPostPaidAccountPlanDetails")
	public String getCustomerPostPaidAccountPlanPage() {
		return "getCustomerPostPaidAccountPlanPage";
	}
	@RequestMapping("/closeCustomerPostPaidAccount")
	public String closeCustomerPostPaidAccountPage() {
		return "closeCustomerPostPaidAccountPage";
	}
	@RequestMapping("/deleteCustomer")
	public String deleteCustomerPage() {
		return "deleteCustomerPage";
	}
	@ModelAttribute
	public Customer getCustomer() {
		customer=new Customer();
		return customer;
	}
}
