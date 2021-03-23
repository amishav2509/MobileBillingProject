package com.cg.mobilebilling.aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.cg.mobilebilling.customresponse.CustomResponse;
import com.cg.mobilebilling.exceptions.BillDetailsNotFoundException;
import com.cg.mobilebilling.exceptions.BillingServicesDownException;
import com.cg.mobilebilling.exceptions.CustomerDetailsNotFoundException;
import com.cg.mobilebilling.exceptions.InvalidBillMonthException;
import com.cg.mobilebilling.exceptions.PlanDetailsNotFoundException;
import com.cg.mobilebilling.exceptions.PostpaidAccountNotFoundException;
@ControllerAdvice(basePackages= {"com.cg.mobilebilling.controllers"})
public class BillingExceptionAspect {
	@ExceptionHandler({CustomerDetailsNotFoundException.class,BillingServicesDownException.class,BillDetailsNotFoundException.class,
			InvalidBillMonthException.class,PlanDetailsNotFoundException.class,PostpaidAccountNotFoundException.class})
	public ResponseEntity<CustomResponse> handleBillingException(Exception e){
		CustomResponse response=new CustomResponse(HttpStatus.EXPECTATION_FAILED.value(),e.getMessage());
		System.out.println(response);
		return new ResponseEntity<>(response,HttpStatus.EXPECTATION_FAILED);
	}
}