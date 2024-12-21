package com.project.transaction_fraud_monitor.DTO;

import java.util.List;

public class AdminOps {
	
	private List<String> blockedAccounts;
	private String action;
	
	

	public List<String> getBlockedAccounts() {
		return blockedAccounts;
	}
	public void setBlockedAccounts(List<String> blockedAccounts) {
		this.blockedAccounts = blockedAccounts;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	

}
