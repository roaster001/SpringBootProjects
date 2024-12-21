package com.project.loancalculator.loan_eligibility_calculator.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserFinancialDetails {

	private String userId;
	private int monthlyIncome;
	private int existingLoanObligations;
	private int creditScore;
	private int requestedLoanAmount;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getMonthlyIncome() {
		return monthlyIncome;
	}
	public void setMonthlyIncome(int monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}
	public int getExistingLoanObligations() {
		return existingLoanObligations;
	}
	public void setExistingLoanObligations(int existingLoanObligations) {
		this.existingLoanObligations = existingLoanObligations;
	}
	public int getCreditScore() {
		return creditScore;
	}
	public void setCreditScore(int creditScore) {
		this.creditScore = creditScore;
	}
	public int getRequestedLoanAmount() {
		return requestedLoanAmount;
	}
	public void setRequestedLoanAmount(int requestedLoanAmount) {
		this.requestedLoanAmount = requestedLoanAmount;
	}
	
	
	

}
