package com.project.loancalculator.loan_eligibility_calculator.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.loancalculator.loan_eligibility_calculator.DTO.LoanEligibilityResponse;
import com.project.loancalculator.loan_eligibility_calculator.DTO.UserFinancialDetails;

@Service
public class LoanEligibilityService {
	ArrayList<LoanEligibilityResponse> approvedloans = new ArrayList<>();
	ArrayList<LoanEligibilityResponse> rejectedloans = new ArrayList<>();
	ArrayList<LoanEligibilityResponse> response = new ArrayList<>();
	ArrayList<UserFinancialDetails> reflst=new ArrayList<>();
	
	double avgcreditscore=0;
	
	@Cacheable(value = "loanDetailsCache", key = "#userdetails")
	public ArrayList<LoanEligibilityResponse> calculateloaneligibility(ArrayList<UserFinancialDetails> userdetails) {
		
	 reflst=userdetails;
		for (UserFinancialDetails user : userdetails) {
			
			LoanEligibilityResponse loanresponse = new LoanEligibilityResponse();

			boolean criteria_1 = incomecriteria(user.getMonthlyIncome());
			boolean criteria_2 = loanobligations(user.getExistingLoanObligations(), user.getMonthlyIncome());
			boolean criteria_3 = creditcheck(user.getCreditScore());
			boolean criteria_4 = loanrequest(user.getRequestedLoanAmount(), user.getMonthlyIncome());

			if (criteria_1 && criteria_2 && criteria_3 && criteria_4) {

				int interest = 10;
				int tenure = 10;
				int approvedammount = (int) calculateEligibleLoanAmount(user.getMonthlyIncome(),
						user.getExistingLoanObligations(), user.getCreditScore(), user.getRequestedLoanAmount(),
						interest, tenure);
				HashMap<String, Object> emiBreakdown = calculateLoanEMIs(user.getMonthlyIncome(),
						user.getExistingLoanObligations(), user.getCreditScore(), user.getRequestedLoanAmount(),
						tenure);

				loanresponse.setEligible(true);
				loanresponse.setApprovedLoanAmount(approvedammount);
				loanresponse.setEmiBreakdown(emiBreakdown);
				approvedloans.add(loanresponse);
				response.add(loanresponse);
				
				int len=userdetails.size();
				double totalcreditscore=0;
				for(int i=0;i<len;i++) {
					totalcreditscore+=userdetails.get(i).getCreditScore();
				}
				
				avgcreditscore=totalcreditscore/len;
				
				

			} else {
				
				loanresponse.setEligible(false); // Include reasons for ineligibility
				
				if (!criteria_1)
					loanresponse.setReason("Monthly income is below the required minimum.");
				else if (!criteria_2)
					loanresponse.setReason("Existing loan obligations exceed 40% of monthly income.");
				else if (!criteria_3)
					loanresponse.setReason("Credit score is below the required minimum.");
				else if (!criteria_4)
					loanresponse.setReason("Requested loan amount exceeds the maximum allowable amount.");
				
				rejectedloans.add(loanresponse);

				response.add(loanresponse);
			}

			// TODO Auto-generated method stub
		}
		return response;

	}
	
	
	@Cacheable(value = "approvedloanDetailsCache")
	public ArrayList<LoanEligibilityResponse> getApprovedLoans() 
	
	
	{ if(approvedloans!=null) {	return approvedloans; }
	return null;
	
	}
	
	
	@Cacheable(value = "rejectedloanDetailsCache")
	public ArrayList<LoanEligibilityResponse> getrejectedLoans() 
	{ 
		if(rejectedloans!=null) {return rejectedloans; }
		return null;
		
	}
	
	@Cacheable(value = "adminDetailsCache")
	public HashMap<String, Object> getadmindata() {
		
		HashMap<String,Object> map=new HashMap<>();
		if(reflst==null) {
			map.put(null, null);
			return map;
			
		}
		// TODO Auto-generated method stub
		
		
		
		double Avgloanappproved=getavgloanapproveddata();
		Map<String,Integer> rejectionreason=getrejectionreason();
		double avgcreditscore=getavgcreditscore();
		Map<String,Object> percentageapproved=getpercentage();
		
		map.put("AVG_Apporved_Loans INR:", Avgloanappproved);
		map.put("Rejection_Reason_Frequency", rejectionreason);
		map.put("Avg_Credit_Score", avgcreditscore);
		map.put("Percentage_Stas", percentageapproved);
		
		return map;
	}
	
	
	
	
	///all calling method ends above below is logic

	public boolean incomecriteria(Integer income) {
		if (income >= 30000) {
			return true;
		}
		return false;
	}

	public boolean loanobligations(Integer loanamount, Integer income) {
		float oblig = (float) (0.4 * income);
		if (loanamount <= oblig) {
			return true;
		} else {
			return false;
		}
	}

	public boolean creditcheck(Integer creditscore) {
		if (creditscore >= 700) {
			return true;
		}
		return false;
	}

	public boolean loanrequest(Integer amount, Integer income) {
		BigInteger cap = BigInteger.valueOf(income);
		BigInteger loanamount = BigInteger.valueOf(amount);
		BigInteger loancap = cap.multiply(BigInteger.TEN);
		int comparisonResult = loanamount.compareTo(loancap);

		// Evaluate the comparison result
		if (comparisonResult < 0 || comparisonResult == 0) {
			return true;
		}
		return false;
	}

	
	
	
	
	
	public static double calculateEligibleLoanAmount(double monthlyIncome, double existingLoanObligations,
			int creditScore, double requestedLoanAmount, double interestRateAnnual, int loanTenureYears) {

		// Constants
		double maxDTIRatio = 0.4; // Maximum debt-to-income ratio (40%) for low credit scores
		int maxCreditScore = 900; // Maximum possible credit score

		// Step 1: Calculate Net Available Income (NAI)
		double netAvailableIncome = (monthlyIncome * maxDTIRatio) - existingLoanObligations;

		// If NAI is negative, the user is not eligible for a loan
		if (netAvailableIncome <= 0) {
			return 0; // Not eligible
		}

		// Step 2: Adjust based on credit score
		double adjustmentFactor = 0.5 + ((double) creditScore / maxCreditScore * 0.5);

		// Step 3: Calculate the maximum loan amount based on EMI formula
		double monthlyInterestRate = interestRateAnnual / 12 / 100; // Convert annual interest rate to monthly
		int totalMonths = loanTenureYears * 12; // Total number of months in tenure

		// EMI Formula: EMI = [P * r * (1 + r)^n] / [(1 + r)^n - 1]
		// Rearrange to find P (Principal): P = EMI * [(1 + r)^n - 1] / [r * (1 + r)^n]
		double emi = netAvailableIncome; // Maximum EMI is the Net Available Income

		double factor = Math.pow(1 + monthlyInterestRate, totalMonths);
		double maxLoanAmount = emi * (factor - 1) / (monthlyInterestRate * factor);

		// Step 4: Apply the adjustment factor
		maxLoanAmount *= adjustmentFactor;

		// Step 5: Ensure the requested loan amount does not exceed the calculated
		// amount
		return Math.min(maxLoanAmount, requestedLoanAmount);
	}

	
	
	public static HashMap<String, Object> calculateLoanEMIs(double monthlyIncome, double existingLoanObligations,
			int creditScore, double requestedLoanAmount, int loanTenureYears) {

		// Constants
		double maxDTIRatio = 0.4; // Maximum debt-to-income ratio (40%) for low credit scores
		int maxCreditScore = 900; // Maximum possible credit score

		// Step 1: Calculate Net Available Income (NAI)
		double netAvailableIncome = (monthlyIncome * maxDTIRatio) - existingLoanObligations;

		// If NAI is negative, the user is not eligible for a loan
		if (netAvailableIncome <= 0) {
			HashMap<String, Object> result = new HashMap<>();
			result.put("8%", 0);
			result.put("10%", 0);
			result.put("12%", 0);
			return result; // Not eligible
		}

		// Step 2: Adjust based on credit score
		double adjustmentFactor = 0.5 + ((double) creditScore / maxCreditScore * 0.5);

		// Step 3: Calculate the EMI for each interest rate
		HashMap<String, Object> loanEMIs = new HashMap<>();
		double[] interestRates = { 8, 10, 12 }; // Interest rates in percentages

		for (double interestRateAnnual : interestRates) {
			double monthlyInterestRate = interestRateAnnual / 12 / 100; // Convert annual interest rate to monthly
			int totalMonths = loanTenureYears * 12; // Total number of months in tenure

			// EMI Formula: EMI = [P * r * (1 + r)^n] / [(1 + r)^n - 1]
			double factor = Math.pow(1 + monthlyInterestRate, totalMonths);

			// Adjusted loan amount for this interest rate
			double maxLoanAmount = Math.min(
					(netAvailableIncome * (factor - 1)) / (monthlyInterestRate * factor) * adjustmentFactor,
					requestedLoanAmount);

			// Calculate EMI based on max loan amount
			double emi = (maxLoanAmount * monthlyInterestRate * factor) / (factor - 1);

			// Store the EMI in the JSON object
			loanEMIs.put(interestRateAnnual + "%", emi);
		}

		return loanEMIs;
	}

	
	

	
	
	public double getavgloanapproveddata() {
		
		double finalloanamount=0;
		int totalapprovedloans=approvedloans.size();
		
		for(int i=0;i<approvedloans.size();i++) {
			finalloanamount+=approvedloans.get(i).getApprovedLoanAmount();
		}
		double avgloanamount=finalloanamount/totalapprovedloans;
		
		return avgloanamount;
		
	}
	
	
	
	public Map<String, Integer> getrejectionreason(){
		Map<String, Integer> reasonCount = new TreeMap<>(); 
		for (LoanEligibilityResponse res : rejectedloans) 
		{ if (!res.isEligible())
		{ String reason = res.getReason();
			reasonCount.put(reason, reasonCount.getOrDefault(reason, 0) + 1); } } 	
		return reasonCount;
		
	}
	
	
	
	public double getavgcreditscore() {
		return avgcreditscore;
		
	}
	
	public Map<String,Object>  getpercentage() {
		HashMap<String,Object> map=new HashMap<>();
		
		
		System.out.print(approvedloans.size());
		
		System.out.print(reflst.size());
		double percentagepassed=((double)(approvedloans.size())*100)/(reflst.size());
		double percentagefailed=((double)(rejectedloans.size())*100)/(reflst.size());
		map.put("Loan_passed_percentage", percentagepassed);

		map.put("Loan_failed_percentage", percentagefailed);
		
		return map;
	}


	

}
