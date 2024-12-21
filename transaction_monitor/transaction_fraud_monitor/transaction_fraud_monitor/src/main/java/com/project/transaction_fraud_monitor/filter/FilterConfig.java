package com.project.transaction_fraud_monitor.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FilterConfig  {
	
	@Bean public FilterRegistrationBean<IpRateLimiterFilter> loggingFilter()
	{ FilterRegistrationBean<IpRateLimiterFilter> registrationBean = new FilterRegistrationBean<>();
	registrationBean.setFilter(new IpRateLimiterFilter()); registrationBean.addUrlPatterns("/*");
	return registrationBean; }
}
