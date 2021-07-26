package app.core.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import app.core.entities.Company;
import app.core.entities.Coupon;
import app.core.entities.CouponPayload;
import app.core.entities.Coupon.Category;
import app.core.exception.CouponSystemException;
import app.core.service.CompanyService;
import app.core.utilities.JwtUtil;

@RestController
@RequestMapping("/company")
@CrossOrigin
public class CompanyController {
	
	private CompanyService service;
	private ConfigurableApplicationContext ctx;

	@Autowired
	public CompanyController(CompanyService service, ConfigurableApplicationContext ctx) {
		this.service = service;
		this.ctx = ctx;
	}

	@PostMapping(path="/add/coupon",consumes = {"multipart/form-data" } )
	public Coupon addCoupon(@ModelAttribute CouponPayload couponPayload, BindingResult result,
			@RequestHeader String token) {
		try {
			if (!recieveToken(token))
				throw new Exception("token is not validated ");
			System.out.println("----------------------couponPayload "+couponPayload.toString());
			System.out.println("getImageName "+couponPayload.getImage());
			return service.addCoupon(couponPayload);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@DeleteMapping("/delete/coupon/{id}")
	public void deleteCoupon(@PathVariable int id, @RequestHeader String token) {
		try {
			if (!recieveToken(token))
				throw new Exception("token is not validated ");
			service.deleteCoupon(id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/getDetails")
	public Company getDetials(@RequestHeader String token) {
		try {
			if (!recieveToken(token))
				throw new Exception("token is not validated ");
			return service.getCompanyDetails();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/getCoupon/{id}")
	public Coupon getCoupon(@PathVariable int id, @RequestHeader String token) {
		try {
			if (!recieveToken(token))
				throw new Exception("token is not validated ");
			return service.findCouponById(id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/getCoupons")
	public List<Coupon> getAllCoupons(@RequestHeader String token) {
		try {
			if (!recieveToken(token))
				throw new Exception("token is not validated ");
			return service.getAllCoupons();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/getCoupons/{category}")
	public List<Coupon> getAllCoupons(@PathVariable Category category, @RequestHeader String token) {
		try {
			if (!recieveToken(token))
				throw new Exception("token is not validated ");
			return service.getAllCouponsByCategory(category);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/getCoupons/{maxPrice}")
	public List<Coupon> getAllCoupons(@PathVariable double maxPrice, @RequestHeader String token) {
		try {
			if (!recieveToken(token))
				throw new Exception("token is not validated ");
			return service.getAllCouponsByprice(maxPrice);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PutMapping(path="/update/coupon",consumes = {"multipart/form-data" })
	public Coupon updateCoupon(@ModelAttribute CouponPayload couponPayload, BindingResult result, @RequestHeader String token) {
		try {
			if (!recieveToken(token))
				throw new Exception("token is not validated ");
			return service.updateCoupon(couponPayload);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	public boolean recieveToken(String token) throws CouponSystemException {
		try {
			JwtUtil util = new JwtUtil();
			int id = util.extractUserId(token);
			service = ctx.getBean(CompanyService.class);
			service.setCompanyID(id);
			;
			String userName = service.getCompanyDetails().getEmail();
			return util.validateToken(token, userName);
		} catch (Exception e) {
			throw new CouponSystemException("token not validated: " + e.getMessage());
		}
	}

}
