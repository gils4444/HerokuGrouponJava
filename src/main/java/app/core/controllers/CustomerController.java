package app.core.controllers;

import java.util.List;

import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import app.core.entities.Coupon;
import app.core.entities.Coupon.Category;
import app.core.entities.Customer;
import app.core.exception.CouponSystemException;
import app.core.service.CustomerService;
import app.core.utilities.JwtUtil;

@RestController
@RequestMapping("/customer")
@CrossOrigin
public class CustomerController {

	private CustomerService service;
	private ConfigurableApplicationContext ctx;
	
	@Autowired
	public CustomerController(CustomerService service,ConfigurableApplicationContext ctx) {
		this.service=service;
		this.ctx=ctx;
	}
	
//	@GetMapping("/login")
//	public boolean login(@RequestParam String email,String password) {
//		try {
//			return service.login(email, password);
//		} catch (Exception e) {
//			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
//		}
//	}
	
	@GetMapping("/get/coupons/all")
	public List<Coupon> getAllCoupons(@RequestHeader String token) {
		try {
			if(!recieveToken(token))
				throw new Exception("token is not validated ");
			return service.getCustomerCoupons();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@GetMapping("/get/coupons/category")
	public List<Coupon> getAllCouponsByCategory(@RequestParam Category category,@RequestHeader String token) {
		try {
			if(!recieveToken(token))
				throw new Exception("token is not validated ");
			return service.getCustomerCoupons(category);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@GetMapping("/get/coupons/price")
	public List<Coupon> getAllCouponsByPrice(@RequestParam double maxPrice,@RequestHeader String token) {
		try {
			if(!recieveToken(token))
				throw new Exception("token is not validated ");
			return service.getCustomerCoupons(maxPrice);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	@GetMapping("/get/customer/details")
	public Customer getCustomerDetails(@RequestHeader String token) {
		try {
			if(!recieveToken(token))
				throw new Exception("token is not validated ");
			return service.getDetails();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@PutMapping("/purchase-coupon")
	public Coupon purchaseCoupon(@RequestBody Coupon coupon,@RequestHeader String token) {
		try {
			if(!recieveToken(token))
				throw new Exception("token is not validated ");
			return service.purchaseCoupon(coupon);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	public boolean recieveToken(String token) throws CouponSystemException {
		try {
			JwtUtil util = new JwtUtil();
			int id = util.extractUserId(token);
			service = ctx.getBean(CustomerService.class);
			service.setCustomerID(id);
			String userName = service.getDetails().getEmail();
			return util.validateToken(token, userName);
		} catch (CouponSystemException e) {
			throw new CouponSystemException("token not validated: " + e.getMessage());
		}
	}

	
}
