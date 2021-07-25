package app.core.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import app.core.entities.Coupon;
import app.core.entities.Coupon.Category;
import app.core.entities.Customer;
import app.core.exception.CouponSystemException;
import app.core.repositories.CompanyRepository;
import app.core.repositories.CouponRepository;
import app.core.repositories.CustomerRepository;

@Service
@Transactional
@Scope(value = "prototype")
public class CustomerService extends ClientService {

	private int customerID;

	@Autowired
	public CustomerService(CompanyRepository companyRepository, CustomerRepository customerRepository,
			CouponRepository couponRepository) {
		super(companyRepository, customerRepository, couponRepository);
	}

	@Override
	public boolean login(String email, String password) {
		Customer customer = customerRepository.findByEmailIgnoreCaseAndPassword(email, password);
		if (customer != null) {
			customerID = customer.getId();
			return true;
		}
		return false;

	}

	public Coupon purchaseCoupon(Coupon coupon) throws CouponSystemException {

//		if true the customer already bought this coupon
		Coupon couponFromDB = couponRepository.findByTitleAndCompanyId(coupon.getTitle(), coupon.getCompany().getId());
		if (getDetails().getCoupons().contains(couponFromDB))
			throw new CouponSystemException("The customer already bought this coupon");
		if (couponFromDB.getAmount() <= 0)
			throw new CouponSystemException("Can't buy coupon, the coupon amount is: " + couponFromDB.getAmount());
		if (!couponFromDB.getEndDate().isAfter(LocalDate.now()))// check if the date (day) has passed =
			throw new CouponSystemException("Can't buy coupon, the coupon has expired: " + couponFromDB.getEndDate());

		Customer customer = getDetails();
		customer.addCoupon(couponFromDB);
		couponFromDB.setAmount(couponFromDB.getAmount() - 1);

		System.out.println(couponFromDB + " successfully updated");
		return couponRepository.save(couponFromDB);

	}

	public List<Coupon> getCustomerCoupons() throws CouponSystemException {
		Customer customer = getDetails();
		List<Coupon> coupons = customer.getCoupons();
		if (coupons.isEmpty())
			return coupons;
		return coupons;

	}

	public List<Coupon> getCustomerCoupons(Category category) throws CouponSystemException {
		try {
			return couponRepository.findByCustomersIdAndCategory(customerID, category);
		} catch (Exception e) {
			throw new CouponSystemException("getCustomerCoupons(Category category) failed", e);
		}
	}

	public List<Coupon> getCustomerCoupons(double maxPrice) throws CouponSystemException {
		try {
			return couponRepository.findByCustomersIdAndPriceLessThanEqual(customerID, maxPrice);
		} catch (Exception e) {
			throw new CouponSystemException("getCustomerCoupons(double maxPrice) failed", e);
		}
	}

	public Customer getDetails() throws CouponSystemException {
		Optional<Customer> opt = customerRepository.findById(customerID);
		if (opt.isPresent()) {
			Customer customer = opt.get();
			return customer;
		}
		throw new CouponSystemException("getDetails failed - customer is null");
	}

	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	public List<Coupon> getAllCouponsCustomerCanPurchase() {
		int amount = 0;
		return couponRepository.findByAmountGreaterThanAndIdIsNot(amount, customerID);
	}

}
