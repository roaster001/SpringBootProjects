package com.project.loancalculator.loan_eligibility_calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableCaching
public class LoanEligibilityCalculatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoanEligibilityCalculatorApplication.class, args);
	
		System.out.println("Application Started");
	}

}
