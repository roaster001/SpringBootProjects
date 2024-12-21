package com.project.transaction_fraud_monitor.filter;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.time.Duration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



public class IpRateLimiterFilter extends OncePerRequestFilter {

	    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
	 // The bucket can hold up to 5 tokens and is refilled with 5 tokens every 1 minute.
	    private final Bandwidth limit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)));

    private Bucket resolveBucket(String ip) {
        return buckets.computeIfAbsent(ip, k -> Bucket.builder().addLimit(limit).build());
    }
	   @Override
	    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	            throws java.io.IOException, ServletException {
		   InetAddress localHost = InetAddress.getLocalHost();
	        String clientIp = localHost.toString();
	        System.out.println(clientIp);
	        Bucket bucket = resolveBucket("hello"+clientIp);

	        if (bucket.tryConsume(1)) {
	            filterChain.doFilter(request, response);
	        } else {
	            response.setStatus(429);
	            response.getWriter().write("Rate limit exceeded");
	        }

}
}