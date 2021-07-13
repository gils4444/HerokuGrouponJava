package app.core.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import app.core.entities.Coupon;
import app.core.entities.Customer;
import app.core.entities.Coupon.Category;
import app.core.service.GuestService;

@CrossOrigin
@RestController
@RequestMapping("/Guest")
public class GuestController {

	private GuestService service;

	@Autowired
	public GuestController(GuestService service) {
		this.service = service;
	}

	@PostMapping("/add/customer")
	public Customer addCustomer(@RequestBody Customer customer) {
		try {
			return service.addCustomer(customer);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/get/coupons/all")
	public List<Coupon> getAllSiteCoupons() {
		try {
			return service.getAllSiteCoupons();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/get/coupons/category/{category}")
	public List<Coupon> getAllSiteCouponsByCategory(@PathVariable Category category) {
		try {
			return service.getAllSiteCouponsByCategory(category);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/get/coupons/price{maxPrice}")
	public List<Coupon> getAllSiteCouponsByMaxPrice(@PathVariable double maxPrice) {
		try {
			return service.getAllSiteCouponsByMaxPrice(maxPrice);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

}
