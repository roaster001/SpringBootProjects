package com.project.transaction_fraud_monitor.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.transaction_fraud_monitor.DTO.Transaction;

@Service
public class FraudDetectionService {
	
	private static final double MAX_TRANSACTION_AMOUNT = 100000.0;
	
	
	HashSet<Object> BLACKLISTED_ACCOUNTS=new HashSet<>();
 //  List<String> BLACKLISTED_ACCOUNTS = List.of("6083103750", "4285042878");
   
   List<String> OUTSIDE_INDIA_IPS = List.of("93.252.47.153", "170.75.227.35");

    public String detectFraud(Transaction transaction, List<Transaction> recentTransactions) {
        if (transaction.getAmount() > MAX_TRANSACTION_AMOUNT) {
            return "Transaction amount exceeds the limit of ₹1,00,000.";
        }

        if (recentTransactions.size() >= 3) {
            return "More than 3 transactions within 5 minutes from the same account.";
        }

        if (BLACKLISTED_ACCOUNTS.contains(transaction.getAccountNumber())) {
            return "Account is blacklisted.";
        }

        if (OUTSIDE_INDIA_IPS.contains(transaction.getIpAddress())) {
            return "Transaction initiated from an IP address outside India.";
        }

        return null; // No fraud detected
    }
    
    public String actionsonblockedaccounts(List<String> accounts) {
    	
    	BLACKLISTED_ACCOUNTS.addAll(accounts);
    	BLACKLISTED_ACCOUNTS.removeIf(entry -> ((String) entry).trim().isEmpty());
    	BLACKLISTED_ACCOUNTS.removeIf(entry -> ((String) entry).trim().length()>10);
    	BLACKLISTED_ACCOUNTS.removeIf(entry -> ((String) entry).matches(".*[^a-zA-Z0-9].*"));
    	
    	System.out.println(BLACKLISTED_ACCOUNTS);
		
    	return "Successfully Added BlackListed Acoount";
    }
    
public String actionsonblockedaccounts(List<String> accounts,String action) {
    	
    	BLACKLISTED_ACCOUNTS.addAll(accounts);
    	BLACKLISTED_ACCOUNTS.removeIf(entry -> ((String) entry).trim().isEmpty());
    	BLACKLISTED_ACCOUNTS.removeIf(entry -> ((String) entry).matches(".*[^a-zA-Z0-9].*"));
    	BLACKLISTED_ACCOUNTS.removeIf(entry -> ((String) entry).trim().length()>10);
    	BLACKLISTED_ACCOUNTS.removeAll(accounts);
    	System.out.println(BLACKLISTED_ACCOUNTS);
		return "Records Remove Successfully";
    }
    

}
