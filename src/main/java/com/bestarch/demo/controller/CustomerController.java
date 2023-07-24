package com.bestarch.demo.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bestarch.demo.domain.Customer;
import com.bestarch.demo.service.CustomerService;
import com.bestarch.demo.util.Utility;

import io.micrometer.core.instrument.util.StringUtils;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private Utility util;

    @GetMapping(value = {"register"})
    public ModelAndView getNewRegistrationForm() {
        ModelAndView mv = new ModelAndView("register");
        Customer customer = new Customer();
        mv.addObject(customer);
        return mv;
    }

    @PostMapping(value = "/customer", consumes = {"application/x-www-form-urlencoded;charset=UTF-8"})
    public String saveCustomer(@ModelAttribute Customer customer, BindingResult errors, Model model) {
        customerService.saveCustomer(customer);
        return "redirect:/login";
    }

    @PostMapping(value = "/upgrade", consumes = {"application/x-www-form-urlencoded;charset=UTF-8"})
    public String upgradeCustomer(@RequestParam(name = "plan") String plan) {
        customerService.upgradeCustomer(plan);
        return "redirect:/dashboard";
    }
    
    @GetMapping(value = {"/", "/login"})
    public String login(HttpServletRequest request, HttpSession session, @RequestParam(defaultValue = "false") Boolean error) {
    	if (Boolean.valueOf(error)) {
    		session.setAttribute("error", getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));
    	}
        return "login";
    }

    @GetMapping(value = {"/logout"})
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping(value = {"/dashboard"})
    public ModelAndView dashboard(HttpSession session) {
    	session.removeAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        ModelAndView mv = new ModelAndView("welcome");
        Optional<Customer> cust = customerService.getCustomer();
        if (cust.isPresent()) {
            Customer c = cust.get();
            mv.addObject("plan", c.getPlan());
            mv.addObject("permits", util.getPermitsForLoggedInUser(c.getUsername()));
        }
        return mv;
    }

    private String getErrorMessage(HttpServletRequest request, String key) {
        Exception e = (Exception) request.getSession().getAttribute(key);
        String error = "";
        if (e instanceof BadCredentialsException) {
            error = "Invalid username or password!";
        } else {
            error = "Invalid username and password!";
        }
        return error;
    }

}