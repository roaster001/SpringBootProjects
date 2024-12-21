	package com.project.transaction_fraud_monitor.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.transaction_fraud_monitor.DTO.AdminOps;
import com.project.transaction_fraud_monitor.DTO.CustomResponse;
import com.project.transaction_fraud_monitor.DTO.Transaction;
import com.project.transaction_fraud_monitor.service.FraudDetectionService;
import com.project.transaction_fraud_monitor.service.TransactionService;

@RestController

public class TransactionController {

	// I hardcoded my system ip in Base64 and stored it
	String encodedIP = "XXXXXXXX"; //THIS IS ENCODED IP OF SYSTEM THAT CAN ACCESS IT FOR TESTING I HAVE USED CONDITION 1==1
	String errorResponse = "You are Not Authorised";
	String transresponse = "Transaction Failed";
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private FraudDetectionService frauddetectionservice;
	
	
	@PostMapping("/transactions/process")
	public ResponseEntity<Object> processTransaction(@RequestBody List<Transaction> transaction) {
		ArrayList<CustomResponse> processedTransaction;
		try {
			processedTransaction = transactionService.processTransaction(transaction);
		} catch (Exception e) {
			return new ResponseEntity<>(transresponse, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return new ResponseEntity<>(processedTransaction, HttpStatus.OK);
	}

	@GetMapping("/admin/flagged-transactions")
	public ResponseEntity<Object> getFlaggedTransactions() {
		List<Transaction> flaged = new ArrayList<>();
		try {
			// Get the local host's InetAddress instance
			InetAddress localHost = InetAddress.getLocalHost();
			// Get the IP address
			String ipAddress = localHost.getHostAddress();

			// decodingmyipaddress
			byte[] decodedBytes = Base64.getDecoder().decode(encodedIP);
			String decodedIP = new String(decodedBytes);

			if (ipAddress.equals(decodedIP)|1==1) //CONDITION 1==1 USED FOR TESTING PURPOSE WHEN SYSTEM IP MATCH HARDCODED IP THEN WILL EXECUTE
			
			{
				System.out.println("Authorised");
				flaged = transactionService.getFlaggedTransactions();
			} else {
				System.out.println("Not authorised");
				return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
			}

		} catch (UnknownHostException e) {
			System.err.println("Unable to get the system IP address: " + e);
			return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch(Exception e) {
			System.err.println("System.error" + e);
			return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(flaged, HttpStatus.OK);
	}
	

	@PostMapping("/admin/action_on_tansaction")
	public ResponseEntity<Object> actionontransaction(@RequestBody List<AdminOps> ops) {
		String action = "Action performed";
		if(ops.get(0).getAction().equalsIgnoreCase("add")) {
			
			try {
				action=frauddetectionservice.actionsonblockedaccounts(ops.get(0).getBlockedAccounts());
			}
			catch(Exception e){
				System.out.println("exception");
			}	
		}
		else {
			action=frauddetectionservice.actionsonblockedaccounts(ops.get(0).getBlockedAccounts(),ops.get(0).getAction());
			
		}

		return new ResponseEntity<>(action, HttpStatus.OK);
	}

	
	
	
	@GetMapping("/hello")
	public String hellotest() {

		return "hello";
	}
}
