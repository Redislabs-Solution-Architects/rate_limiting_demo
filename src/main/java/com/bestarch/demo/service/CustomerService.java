package com.bestarch.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.bestarch.demo.domain.Customer;
import com.bestarch.demo.domain.Prospect;
import com.bestarch.demo.util.Utility;

import java.util.List;
import java.util.Optional;

@Service
public abstract class CustomerService {
	
	@Autowired
	protected RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	protected Utility utility;
	
	public abstract void saveCustomer(Customer customer);

	public abstract Optional<Customer> getCustomer();

	public abstract List<Prospect> getProspects();

	public abstract void upgradeCustomer(String plan);

}
