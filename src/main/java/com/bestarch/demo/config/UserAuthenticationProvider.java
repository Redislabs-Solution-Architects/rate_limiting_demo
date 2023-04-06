package com.bestarch.demo.config;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import com.bestarch.demo.domain.Customer;
import com.bestarch.demo.repository.CustomerRepository;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	private CustomerRepository customerRepository;
	
    @Override
    public Authentication authenticate(Authentication authentication) 
      throws AuthenticationException {
 
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        
        Optional<Customer> custOp = customerRepository.findById(username);
        if (custOp.isPresent()) {
        	String hashedPswd = custOp.get().getPassword();
        	if (BCrypt.checkpw(password, hashedPswd)) {
        		return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
        	} 
        	throw new BadCredentialsException("Invalid password");
        }
        throw new AuthenticationCredentialsNotFoundException("Invalid user");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}