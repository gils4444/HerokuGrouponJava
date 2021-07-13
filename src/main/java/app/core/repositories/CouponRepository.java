package app.core.repositories;

import java.time.LocalDate;
import java.util.List;


import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import app.core.entities.Coupon;
import app.core.entities.Coupon.Category;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {


	List<Coupon> findByCompanyId(int company_id);
	
	List<Coupon> findByCompanyIdAndCategory(int company_id,Category category);
	
	List<Coupon> findByCompanyIdAndPriceLessThanEqual(int company_id,double price);

	Coupon findByTitleAndCompanyId(String title,int company_id);
	
	/**
	 * @param title
	 * @param company_id
	 * @param id
	 * @return true if there is  a coupon with the same title to the same company but with different id
	 */
	Coupon findByTitleAndCompanyIdAndIdIsNot(String title,int company_id, int id);

	List<Coupon> findByCustomersIdAndPriceLessThanEqual(int customerID,double price);

	List<Coupon> findByCustomersIdAndCategory(int customerId,Category category);
	
	long deleteByEndDateIsBefore(LocalDate date);
	
	@Query(value = "select id from Coupon where amount > :amount and not exists (select coupon_id from Coupon_customer)" ,nativeQuery = true)
	List<Coupon> getAllCouponsCustomerCanPurchase(int amount);
	
	List<Coupon> findByAmountGreaterThanAndIdIsNot(int amount,int id);

	List<Coupon> findByCategory(Category category);

	List<Coupon> findByPriceLessThanEqual(double maxPrice);
	
	
}
