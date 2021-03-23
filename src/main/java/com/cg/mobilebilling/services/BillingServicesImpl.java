package com.cg.mobilebilling.services;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.cg.mobilebilling.beans.Bill;
import com.cg.mobilebilling.beans.Customer;
import com.cg.mobilebilling.beans.Plan;
import com.cg.mobilebilling.beans.PostpaidAccount;
import com.cg.mobilebilling.daoservices.BillingDAOServices;
import com.cg.mobilebilling.daoservices.CustomerDAOServices;
import com.cg.mobilebilling.daoservices.PlanDAOServices;
import com.cg.mobilebilling.daoservices.PostpaidDAOServices;
import com.cg.mobilebilling.exceptions.BillDetailsNotFoundException;
import com.cg.mobilebilling.exceptions.BillingServicesDownException;
import com.cg.mobilebilling.exceptions.CustomerDetailsNotFoundException;
import com.cg.mobilebilling.exceptions.InvalidBillMonthException;
import com.cg.mobilebilling.exceptions.PlanDetailsNotFoundException;
import com.cg.mobilebilling.exceptions.PostpaidAccountNotFoundException;
@Component("billingServices")
public class BillingServicesImpl implements BillingServices {
	@Autowired
	BillingDAOServices billingDAO;
	@Autowired
	CustomerDAOServices customerDAO;
	@Autowired
	PlanDAOServices planDAO;
	@Autowired
	PostpaidDAOServices postpaidDAO;
	private static int flag=0;
	@Override
	public List<Plan> getPlanAllDetails() throws BillingServicesDownException {
		return planDAO.findAll();
	}

	@Override
	public Customer acceptCustomerDetails(Customer customer) throws BillingServicesDownException {
		
		 
			Plan plan1=new Plan(1,499,100,100,50,50,1000,0.10f,0.20f,0.05f,0.07f,0.03f,"Mumbai-Pune", "Diwali-Dhamaka");
			planDAO.save(plan1);
			Plan plan2=new Plan(2,99,50,50,25,25,100,0.10f,0.20f,0.05f,0.07f,0.03f,"Rajasthan-Gujarat", "Diwali-Special-Dhamaka");
			planDAO.save(plan2);
			Plan plan3=new Plan(3,999,500,500,500,500,10000,0.10f,0.20f,0.05f,0.07f,0.03f,"Pune-Maharashtra", "Diwali-Hungama");
			planDAO.save(plan3);
			Plan plan4=new Plan(4,899,700,600,400,530,990,0.14f,0.22f,0.08f,0.06f,0.04f,"Kota-Juipur", "New-Year-Hungama");
			planDAO.save(plan4);
			Plan plan5=new Plan(5,1099,520,510,490,400,10000,0.12f,0.21f,0.06f,0.11f,0.04f,"Delhi-Hariyana", "New-Year-Speacial");
			planDAO.save(plan5);
			Plan plan6=new Plan(6,870,400,550,560,450,9000,0.12f,0.23f,0.15f,0.17f,0.03f,"Chennai", "Diwali-Special-Dhamaka");
			planDAO.save(plan6);
			
		return customerDAO.save(customer);
	}

	@Override
	public long openPostpaidMobileAccount(int customerID, int planID)
			throws PlanDetailsNotFoundException, CustomerDetailsNotFoundException, BillingServicesDownException {
		Customer customer=customerDAO.findById(customerID).orElseThrow(()->
		new CustomerDetailsNotFoundException("Customer Details Not Found With CustomerID="+customerID));
		Plan plan=planDAO.findById(planID).orElseThrow(()->
		new PlanDetailsNotFoundException("Plan Details Not Found With PlanID="+planID));
		PostpaidAccount postpaid=new PostpaidAccount(plan, customer);
		postpaidDAO.save(postpaid);
		return postpaid.getMobileNo();
	}

	@Override
	public Bill generateMonthlyMobileBill(int customerID, long mobileNo, String billMonth, int noOfLocalSMS,
			int noOfStdSMS, int noOfLocalCalls, int noOfStdCalls, int internetDataUsageUnits)
					throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, InvalidBillMonthException,
					BillingServicesDownException, PlanDetailsNotFoundException, BillDetailsNotFoundException {
		customerDAO.findById(customerID).orElseThrow(()->
		new CustomerDetailsNotFoundException("Customer Details Not Found With CustomerID="+customerID));
		PostpaidAccount postpaid=getPostPaidAccountDetails(customerID, mobileNo);
		if(postpaid==null) throw new PostpaidAccountNotFoundException("Post Paid Account Details Not Found With Mobile No="+mobileNo);
		Bill bill1=getMobileBillDetails(customerID, mobileNo, billMonth);
		if(bill1!=null) throw new BillDetailsNotFoundException("Bill Details Already Exists With Bill Month="+billMonth);
		Bill bill=new Bill(noOfLocalSMS, noOfStdSMS, noOfLocalCalls, noOfStdCalls, internetDataUsageUnits, postpaid, billMonth);
		if(noOfLocalSMS>postpaid.getPlan().getFreeLocalSMS())
			bill.setLocalSMSAmount((noOfLocalSMS-postpaid.getPlan().getFreeLocalSMS())*postpaid.getPlan().getLocalSMSRate());
		if(noOfStdSMS>postpaid.getPlan().getFreeStdSMS())
			bill.setStdSMSAmount((noOfStdSMS-postpaid.getPlan().getFreeStdSMS())*postpaid.getPlan().getStdSMSRate());
		if(noOfLocalCalls>postpaid.getPlan().getFreeLocalCalls())
			bill.setLocalCallAmount((noOfLocalCalls-postpaid.getPlan().getFreeLocalCalls())*postpaid.getPlan().getLocalCallRate());
		if(noOfStdCalls>postpaid.getPlan().getFreeStdCalls())
			bill.setStdCallAmount((noOfStdCalls-postpaid.getPlan().getFreeStdCalls())*postpaid.getPlan().getStdCallRate());
		if(internetDataUsageUnits>postpaid.getPlan().getFreeInternetDataUsageUnits())
			bill.setInternetDataUsageAmount((internetDataUsageUnits-postpaid.getPlan().getFreeInternetDataUsageUnits())*postpaid.getPlan().getInternetDataUsageRate());
		float initialAmount=bill.getLocalSMSAmount()+bill.getLocalCallAmount()+bill.getStdSMSAmount()+bill.getStdCallAmount()+bill.getInternetDataUsageAmount()+postpaid.getPlan().getMonthlyRental();
		bill.setCgst((initialAmount*5)/100);
		bill.setSgst((initialAmount*5)/100);
		bill.setTotalBillAmount(initialAmount+bill.getCgst()+bill.getSgst());
		billingDAO.save(bill);
		return bill;
	}

	@Override
	public Customer getCustomerDetails(int customerID)
			throws CustomerDetailsNotFoundException, BillingServicesDownException {
		return customerDAO.findById(customerID).orElseThrow(()->
		new CustomerDetailsNotFoundException("Customer Details Not Found With CustomerID="+customerID));
	}

	@Override
	public List<Customer> getAllCustomerDetails() throws BillingServicesDownException {
		return customerDAO.findAll();
	}

	@Override
	public PostpaidAccount getPostPaidAccountDetails(int customerID, long mobileNo)
			throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, BillingServicesDownException {
		Customer customer=customerDAO.findById(customerID).orElseThrow(()->
		new CustomerDetailsNotFoundException("Customer Details Not Found With CustomerID="+customerID));
		PostpaidAccount postpaid=postpaidDAO.getPostPaidAccountDetails(customer, mobileNo);
		return postpaid;
	}

	@Override
	public List<PostpaidAccount> getCustomerAllPostpaidAccountsDetails(int customerID)
			throws CustomerDetailsNotFoundException, BillingServicesDownException, PostpaidAccountNotFoundException {
		Customer customer=customerDAO.findById(customerID).orElseThrow(()->
		new CustomerDetailsNotFoundException("Customer Details Not Found With CustomerID="+customerID));
		List<PostpaidAccount> postpaid=new ArrayList<>();
		postpaid=postpaidDAO.getCustomerAllPostpaidAccountsDetails(customer);
		return postpaid;
	}

	@Override
	public Bill getMobileBillDetails(int customerID, long mobileNo, String billMonth)
			throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, InvalidBillMonthException,
			BillDetailsNotFoundException, BillingServicesDownException {
		PostpaidAccount postpaid=postpaidDAO.findById(mobileNo).orElseThrow(()->
		new PostpaidAccountNotFoundException("Post Paid Account Details NotFound With Mobile Number="+mobileNo));
		Bill bill=billingDAO.getMobileBillDetails(postpaid ,billMonth);
		return bill;
	}

	@Override
	public List<Bill> getCustomerPostPaidAccountAllBillDetails(int customerID, long mobileNo)
			throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, BillingServicesDownException,
			BillDetailsNotFoundException {
		Customer customer=customerDAO.findById(customerID).orElseThrow(()->
		new CustomerDetailsNotFoundException("Customer Details Not Found With CustomerID="+customerID));
		PostpaidAccount postpaid=postpaidDAO.getPostPaidAccountDetails(customer, mobileNo);
		if(postpaid==null) throw new PostpaidAccountNotFoundException("Post Paid Account Details NotFound With Mobile Number="+mobileNo);
		List<Bill> bill=new ArrayList<>();
		bill=postpaidDAO.getCustomerPostPaidAccountAllBillDetails(customer, mobileNo);
		if(bill==null) throw new BillDetailsNotFoundException("No Bills Generated For Mobile No="+mobileNo);
		return bill;
	}

	@Override
	public boolean changePlan(int customerID, long mobileNo, int planID) throws CustomerDetailsNotFoundException,
	PostpaidAccountNotFoundException, PlanDetailsNotFoundException, BillingServicesDownException {
		Customer customer=customerDAO.findById(customerID).orElseThrow(()->
		new CustomerDetailsNotFoundException("Customer Details Not Found With CustomerID="+customerID));
		Plan plan=planDAO.findById(planID).orElseThrow(()->
		new PlanDetailsNotFoundException("Plan Details Not Found With PlanID="+planID));
		PostpaidAccount postpaid=new PostpaidAccount(mobileNo, plan, customer);
		postpaidDAO.save(postpaid);
		return true;
	}

	@Override
	public boolean closeCustomerPostPaidAccount(int customerID, long mobileNo)
			throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, BillingServicesDownException {
		Customer customer=customerDAO.findById(customerID).orElseThrow(()->
		new CustomerDetailsNotFoundException("Customer Details Not Found With CustomerID="+customerID));
		PostpaidAccount postpaidAccount=postpaidDAO.findById(mobileNo).get();
		if(postpaidAccount==null) throw new PostpaidAccountNotFoundException("Post Paid Account Details Not Found With Mobile No="+mobileNo);
		billingDAO.deleteAllBills(postpaidAccount);
		postpaidDAO.closeCustomerPostPaidAccount(customer, mobileNo);
		return true;
	}

	@Override
	public boolean deleteCustomer(int customerID)
			throws BillingServicesDownException, CustomerDetailsNotFoundException, PostpaidAccountNotFoundException {
		ArrayList<PostpaidAccount>postpaidAccounts=(ArrayList<PostpaidAccount>) getCustomerAllPostpaidAccountsDetails(customerID);
		for(PostpaidAccount postpaidAccount:postpaidAccounts) {
			closeCustomerPostPaidAccount(customerID, postpaidAccount.getMobileNo());			
		}
		customerDAO.deleteById(customerID);
		return true;
	}

	@Override
	public Plan getCustomerPostPaidAccountPlanDetails(int customerID, long mobileNo)
			throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, BillingServicesDownException,
			PlanDetailsNotFoundException {
		Customer customer=customerDAO.findById(customerID).orElseThrow(()->
		new CustomerDetailsNotFoundException("Customer Details Not Found With CustomerID="+customerID));
		/*PostpaidAccount postpaidAccount=postpaidDAO.findById(mobileNo).orElseThrow(()->
		new PostpaidAccountNotFoundException("Post Paid Account Details Not Found With Mobile No="+mobileNo));*/
		PostpaidAccount postpaidAccount=postpaidDAO.getPostPaidAccountDetails(customer, mobileNo);
		if(postpaidAccount==null) throw new PostpaidAccountNotFoundException("Post Paid Account Details Not Found With CustomerId="+customerID);
		Plan plan=postpaidDAO.getCustomerPostPaidAccountPlanDetails(customer, mobileNo);
		if(plan==null)throw new PlanDetailsNotFoundException("Plan Details Not Found!!!");
		return plan;
	}
}