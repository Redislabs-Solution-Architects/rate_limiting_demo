package com.bestarch.demo.service;

import com.bestarch.demo.util.Utility;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.bestarch.demo.domain.Customer;
import com.bestarch.demo.domain.Prospect;
import com.bestarch.demo.repository.CustomerRepository;

import java.util.*;

@Service
@Primary
public class CustomerServiceImpl extends CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private Utility util;

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void saveCustomer(Customer customer) {
        String pswdHash = BCrypt.hashpw(customer.getPassword(), BCrypt.gensalt());
        customer.setPassword(pswdHash);
        customer.setPassword2("");
        customerRepository.save(customer);
    }

    @Override
    public Optional<Customer> getCustomer() {
        return customerRepository.findById(util.getUsername());
    }

    @Override
    public List<Prospect> getProspects() {
        List<Prospect> pros = new ArrayList<>();
        RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();
        ScanOptions options = ScanOptions.scanOptions().match("prospect*").count(10).build();
        Cursor<byte[]> c = redisConnection.scan(options);
        byte[] key = null;
        Map<byte[], byte[]> map = null;
        while (c.hasNext()) {
            key = (byte[]) c.next();
            map = redisConnection.hGetAll(key);
            Map<String, String> resMap = new HashMap<>();
            map.forEach((k, v) -> resMap.put(new String(k), new String(v)));
            Prospect p = objectMapper.convertValue(resMap, Prospect.class);
            pros.add(p);
        }
        return pros;
    }

	@Override
	public void upgradeCustomer(String plan) {
		redisTemplate.opsForHash().put("customer:"+util.getUsername(), "plan", "Premium");
	}

}
