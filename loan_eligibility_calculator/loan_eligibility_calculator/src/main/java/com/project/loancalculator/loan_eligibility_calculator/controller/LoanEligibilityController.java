package com.project.loancalculator.loan_eligibility_calculator.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.loancalculator.loan_eligibility_calculator.DTO.LoanEligibilityResponse;
import com.project.loancalculator.loan_eligibility_calculator.DTO.UserFinancialDetails;
import com.project.loancalculator.loan_eligibility_calculator.service.LoanEligibilityService;

@RestController
public class LoanEligibilityController {
	
	 @Autowired 
	 private LoanEligibilityService loaneligibilityservice;
	 
	 
	
	@PostMapping("/loans/eligiblity")
	public ArrayList<LoanEligibilityResponse> checkEligibility(@RequestBody ArrayList<UserFinancialDetails> userdetails){	
		ArrayList<LoanEligibilityResponse> response=loaneligibilityservice.calculateloaneligibility(userdetails);	
	return response;
		}
	
	@GetMapping("/admin/approvedloans")
	public ResponseEntity<ArrayList<LoanEligibilityResponse>> approvedloans(){	
		ArrayList<LoanEligibilityResponse> response=loaneligibilityservice.getApprovedLoans();	
		
		if(response.isEmpty()) {
			return new ResponseEntity<>(response,HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(response,HttpStatus.ACCEPTED);
		}
	
	@GetMapping("/admin/rejectedloans")
	public ResponseEntity<ArrayList<LoanEligibilityResponse>> rejectedloans(){	
		ArrayList<LoanEligibilityResponse> response=loaneligibilityservice.getrejectedLoans();	
		if(response.isEmpty()) {
			return new ResponseEntity<>(response,HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(response,HttpStatus.ACCEPTED);
		}
	
	@GetMapping("/admin/loan-statistics")
	public ResponseEntity<HashMap<String,Object>> adminstats(){
		HashMap<String,Object> map=new HashMap<>();
		map=loaneligibilityservice.getadmindata();
		
		return new ResponseEntity<>(map,HttpStatus.ACCEPTED);
		
	}
	
//	
//	@GetMapping("/hello")
//	public String  hello(){
//		return "hello";
//		
//		}

}
