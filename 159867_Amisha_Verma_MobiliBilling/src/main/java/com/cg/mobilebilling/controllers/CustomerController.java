package com.cg.mobilebilling.controllers;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.cg.mobilebilling.beans.Bill;
import com.cg.mobilebilling.beans.Customer;
import com.cg.mobilebilling.beans.Plan;
import com.cg.mobilebilling.beans.PostpaidAccount;
import com.cg.mobilebilling.exceptions.BillDetailsNotFoundException;
import com.cg.mobilebilling.exceptions.BillingServicesDownException;
import com.cg.mobilebilling.exceptions.CustomerDetailsNotFoundException;
import com.cg.mobilebilling.exceptions.InvalidBillMonthException;
import com.cg.mobilebilling.exceptions.PlanDetailsNotFoundException;
import com.cg.mobilebilling.exceptions.PostpaidAccountNotFoundException;
import com.cg.mobilebilling.services.BillingServices;
@Controller
public class CustomerController {
	@Autowired
	BillingServices billingServices;
	@RequestMapping("/customerRegistration")
	public ModelAndView acceptCustomerDetails(@ ModelAttribute Customer customer,BindingResult bindingResult) throws BillingServicesDownException{
			customer=billingServices.acceptCustomerDetails(customer);
			return new ModelAndView("customerRegistrationSuccessPage","customer",customer);
	}	
	@RequestMapping("/closeCustomerPostPaidAccountSuccessful")
	public ModelAndView closeCustomerPostPaidAccount(@RequestParam int customerID,@RequestParam long mobileNo) throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, BillingServicesDownException {
			billingServices.closeCustomerPostPaidAccount(customerID, mobileNo);
			return new ModelAndView("closeCustomerPostPaidAccountSuccessfulPage","mobileNo",mobileNo);
	}
	
	@RequestMapping("/deleteCustomerSuccessful")
	public ModelAndView deleteCustomer(@RequestParam int customerID) throws BillingServicesDownException, CustomerDetailsNotFoundException, PostpaidAccountNotFoundException {
			billingServices.deleteCustomer(customerID);
			return new ModelAndView("deleteCustomerSuccessfulPage","customerID",customerID);
	}

	@RequestMapping("/postpaidAccount")
	public ModelAndView openPostpaidMobileAccount(@RequestParam int customerID,@RequestParam int planID) throws PlanDetailsNotFoundException, CustomerDetailsNotFoundException, BillingServicesDownException {
		long mobileNumber = billingServices.openPostpaidMobileAccount(customerID, planID);
			return new ModelAndView("PostpaidAccountSuccessPage","mobileNumber",mobileNumber);
	}

	@RequestMapping("/getAllPlanDetails")
	public ModelAndView getPlanAllDetails() throws BillingServicesDownException {
		List<Plan> plans = billingServices.getPlanAllDetails();
			return new ModelAndView("getAllPlanDetailsPage","plans",plans);
	}

	@RequestMapping("/mobileBillGeneration")
	public ModelAndView generateMonthlyMobileBill(@RequestParam int customerID,@RequestParam long mobileNo,@RequestParam String billMonth,@RequestParam int noOfLocalSMS,
			@RequestParam int noOfStdSMS,@RequestParam int noOfLocalCalls,@RequestParam int noOfStdCalls,@RequestParam int internetDataUsageUnits) throws BillingServicesDownException, PlanDetailsNotFoundException, CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, InvalidBillMonthException, BillDetailsNotFoundException {
		Bill bill=billingServices.generateMonthlyMobileBill(customerID, mobileNo, billMonth, noOfLocalSMS, noOfStdSMS, noOfLocalCalls, noOfStdCalls, internetDataUsageUnits);
		return new ModelAndView("mobileBillPage","bill",bill);
	}

	@RequestMapping("/getCustomer")
	public ModelAndView getCustomerDetails(@RequestParam int customerID) throws CustomerDetailsNotFoundException, BillingServicesDownException{
		Customer customer = billingServices.getCustomerDetails(customerID);
			return new ModelAndView("customerDetailsPage","customer",customer);
	}

	@RequestMapping("/postpaidAccountDetails")
	public ModelAndView getpostpaidAccountDetails(@RequestParam int customerID,@RequestParam long mobileNo) throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, BillingServicesDownException {
		PostpaidAccount postpaid = billingServices.getPostPaidAccountDetails(customerID, mobileNo);
			return new ModelAndView("postpaidAccountDetailsPage","postpaid",postpaid);
	}

	@RequestMapping("/getAllCustomerDetails")
	public ModelAndView getAllCustomerDetails() throws BillingServicesDownException  {
		List<Customer> customers = billingServices.getAllCustomerDetails();
			return new ModelAndView("getAllCustomerDetailsPage","customers",customers);
	}

	@RequestMapping("/customerAllPostpaidAccountDetails")
	public ModelAndView getCustomerAllPostpaidAccountDetails(@RequestParam int customerID) throws CustomerDetailsNotFoundException, BillingServicesDownException, PostpaidAccountNotFoundException {
		List<PostpaidAccount> postpaids = billingServices.getCustomerAllPostpaidAccountsDetails(customerID);
			return new ModelAndView("customerAllPostpaidAccountDetailsPage","postpaids",postpaids);
	}	

	@RequestMapping("/mobileBillDetails")
	public ModelAndView getmobileBillDetails(@RequestParam int customerID,@RequestParam long mobileNo,@RequestParam String billMonth) throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, InvalidBillMonthException, BillDetailsNotFoundException, BillingServicesDownException  {
			Bill bill = billingServices.getMobileBillDetails(customerID, mobileNo, billMonth);
			return new ModelAndView("mobileBillDetailsPage","bill",bill);
	}

	@RequestMapping("/getAllBillDetails")
	public ModelAndView getmobileAllBillDetails(@RequestParam int customerID,@RequestParam long mobileNo) throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, BillingServicesDownException, BillDetailsNotFoundException {
		List<Bill> bills = billingServices.getCustomerPostPaidAccountAllBillDetails(customerID, mobileNo);
			return new ModelAndView("mobileAllBillDetailsPage","bills",bills);
	}

	@RequestMapping("/changePlanDetails")
	public ModelAndView changePlanDetails(@RequestParam int customerID,@RequestParam long mobileNo, @RequestParam int planID) throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, PlanDetailsNotFoundException, BillingServicesDownException {
			boolean status = billingServices.changePlan(customerID, mobileNo, planID);
			if(status)
				return new ModelAndView("planChangeSuccessPage");
			else
				return new ModelAndView("changePlanPage");
	}

	@RequestMapping("/postpaidAccountPlanDetails")
	public ModelAndView getpostpaidAccountPlanDetails(@RequestParam int customerID,@RequestParam long mobileNo) throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, BillingServicesDownException, PlanDetailsNotFoundException {
			Plan plan = billingServices.getCustomerPostPaidAccountPlanDetails(customerID, mobileNo);
			return new ModelAndView("postpaidAccountPlanDetailsPage","plan",plan);
	}
}