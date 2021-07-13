package app.core.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.core.entities.Coupon;
import app.core.entities.Customer;
import app.core.entities.Coupon.Category;
import app.core.exception.CouponSystemException;
import app.core.repositories.CouponRepository;
import app.core.repositories.CustomerRepository;

@Service
@Transactional
public class GuestService {

	private CouponRepository  couponRepository;
	private CustomerRepository customerRepository;

	@Autowired
	public GuestService(CouponRepository couponRepository,CustomerRepository customerRepository) {
		this.couponRepository = couponRepository;
		this.customerRepository = customerRepository;
		
	}
	
	public List<Coupon> getAllSiteCoupons() throws CouponSystemException {
		try {
			return couponRepository.findAll();
		} catch (Exception e) {
			throw new CouponSystemException("getAllSiteCoupons failed: "+ e.getMessage());
		}
	}
	
	public Customer addCustomer(Customer newCustomer) throws CouponSystemException {
		System.out.println("==================           addCustomer at guest service        ===========");
		Customer customer = customerRepository.findByEmailIgnoreCase(newCustomer.getEmail());
		if (customer != null)
			throw new CouponSystemException("Customer with id :" + customer.getId() + " has the same Email");
		customer = customerRepository.save(newCustomer);
		System.out.println(customer);
		return customer;
	}
	
	public List<Coupon> getAllSiteCouponsByCategory(Category category) throws CouponSystemException {
		try {
			return couponRepository.findByCategory(category);
		} catch (Exception e) {
			throw new CouponSystemException("getAllSiteCouponsByCategory failed: "+ e.getMessage());
		}
	}
	public List<Coupon> getAllSiteCouponsByMaxPrice(double maxPrice) throws CouponSystemException {
		try {
			return couponRepository.findByPriceLessThanEqual(maxPrice);
		} catch (Exception e) {
			throw new CouponSystemException("getAllSiteCouponsByMaxPrice failed: "+ e.getMessage());
		}
	}
}
