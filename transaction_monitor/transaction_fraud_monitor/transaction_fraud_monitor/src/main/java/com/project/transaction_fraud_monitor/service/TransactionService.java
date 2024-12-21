package com.project.transaction_fraud_monitor.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.transaction_fraud_monitor.DTO.CustomResponse;
import com.project.transaction_fraud_monitor.DTO.Transaction;

@Service
public class TransactionService {
	@Autowired
	FraudDetectionService fraudDetectionService;
	
	private final ArrayList<Transaction> transactions=new ArrayList<Transaction>();
	
	
	
		
		 public ArrayList<CustomResponse> processTransaction(List<Transaction> transaction) {
			 ArrayList<CustomResponse> res=new ArrayList<CustomResponse>();
			 for(Transaction currenttransaction:transaction) {
				 CustomResponse response=new CustomResponse();
			 
			 LocalDateTime fiveMinutesAgo=currenttransaction.getTransactionTime();
			 try {
		         fiveMinutesAgo = currenttransaction.getTransactionTime().minusMinutes(5);
			 }catch(Exception e) {
				 e.printStackTrace();
			 }
		        List<Transaction> recentTransactions = new ArrayList<>();
		        
		        
		        for (Transaction t : transactions) {
		            if (t.getAccountNumber().equals(currenttransaction.getAccountNumber()) &&
		               t.getTransactionTime().isAfter(fiveMinutesAgo)) {
		                recentTransactions.add(t);
		            }
		        }

		        //FraudDetectionService fraudDetectionService = new FraudDetectionService();
		        try {
		        String fraudReason = fraudDetectionService.detectFraud(currenttransaction, recentTransactions);
		        
		        
		        if (fraudReason != null) {
		        	currenttransaction.setFlagged(true);
		        	response.setStatus("Fraud");
		        	response.setReason(fraudReason);    	 
		        }
		        else {
		        	response.setStatus("Success");
		        	response.setMessage("Transaction is valid.");
		        	 
		        }}
		        catch(Exception e){
		        	transactions.clear();
		        	
		        	return res;
		        }
		        finally {
		        	res.add(response);
		        	transactions.add(currenttransaction);
		        }

		       
		       
		    }
			 
			 return res;
			
		 }
		    public List<Transaction> getFlaggedTransactions() {
		        List<Transaction> flaggedTransactions = new ArrayList<>();
		        
		        for (Transaction t : transactions) {
		            if (t.isFlagged()) {
		                flaggedTransactions.add(t);
		            }
		        }
		        return flaggedTransactions;
		    }
		 
	

}
