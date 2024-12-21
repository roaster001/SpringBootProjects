package com.project.loancalculator.loan_eligibility_calculator.DTO;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class LoanEligibilityResponse {

	 private boolean eligible; 
	 private int approvedLoanAmount;
	 private String reason; 
	 private Map<String, Object> emiBreakdown;
	public boolean isEligible() {
		return eligible;
	}
	public void setEligible(boolean eligible) {
		this.eligible = eligible;
	}
	public int getApprovedLoanAmount() {
		return approvedLoanAmount;
	}
	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	public void setApprovedLoanAmount(int approvedLoanAmount) {
		this.approvedLoanAmount = approvedLoanAmount;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Map<String, Object> getEmiBreakdown() {
		return emiBreakdown;
	}
	public void setEmiBreakdown(Map<String, Object> emiBreakdown) {
		this.emiBreakdown = emiBreakdown;
	}
	
	
}
