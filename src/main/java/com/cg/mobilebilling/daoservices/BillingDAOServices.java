package com.cg.mobilebilling.daoservices;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.cg.mobilebilling.beans.Bill;
import com.cg.mobilebilling.beans.PostpaidAccount;
public interface BillingDAOServices extends JpaRepository<Bill, Integer>{
	@Query("select b from Bill b where b.postpaidAccount=:postpaidAccount and b.billMonth=:billMonth")
	Bill getMobileBillDetails(@Param("postpaidAccount") PostpaidAccount postpaidAccount,@Param("billMonth") String billMonth);
	@Transactional
	@Modifying
	@Query("delete from Bill b where b.postpaidAccount=:postpaidAccount")
	void deleteAllBills(@Param("postpaidAccount") PostpaidAccount postpaidAccount);	
}