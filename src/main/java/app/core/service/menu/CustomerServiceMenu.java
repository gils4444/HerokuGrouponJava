package app.core.service.menu;

import java.util.List;
import java.util.Scanner;

import app.core.entities.Coupon;
import app.core.entities.Coupon.Category;
import app.core.entities.Customer;
import app.core.exception.CouponSystemException;
import app.core.service.CustomerService;

public class CustomerServiceMenu {


	/**
	 * @param service
	 * @param scanner
	 * @throws CouponSystemException
	 */
	public static void purchaseCouponMenu(CustomerService service, Scanner scanner) throws CouponSystemException {

		boolean flag = true;
		int couponID = 0;

		System.out.println("these are all the available coupons ");
		List<Coupon> coupons = service.getAllCouponsCustomerCanPurchase();
		if(!coupons.isEmpty())
		for (Coupon coupon : coupons) {
			System.out.println(coupon.toString());
		}
		System.out.println();
		System.out.println("please enter coupon id to purchase");
		try {
			couponID = Integer.parseInt(scanner.nextLine());
		} catch (NumberFormatException e) {
			throw new CouponSystemException("please enter id numbers");
		}

		for (Coupon coupon : coupons) {
			if (coupon.getId() == couponID) {
				flag = false;
				service.purchaseCoupon(coupon);
				break;
			}
		}
		if (flag) {
			throw new CouponSystemException("no match for coupon id you enterd\n");
		}
	}

	/**watch all customer coupons
	 * @param service
	 * @throws CouponSystemException
	 */
	public static void watchCoupon(CustomerService service) throws CouponSystemException {
		List<Coupon> coupons = service.getCustomerCoupons();
		if (coupons.size() == 0)
			throw new CouponSystemException("There are no coupons for this customer");

		for (Coupon coupon : coupons) {
			System.out.println(coupon.toString());
		}
		System.out.println();
	}

	/**watch all customer coupons by category
	 * @param service
	 * @param scanner
	 * @return 
	 * @throws CouponSystemException
	 */
	public static Category watchCouponByCategoryMenu(Scanner scanner) throws CouponSystemException {

		System.out.println("enter category for the coupon:  1 - Food, 2 - Electricty, 3 - Restaurant, 4 - Vacation");
		Category category = null;

		try {
			int num = Integer.parseInt(scanner.nextLine());
			category = Category.values()[--num];
			System.out.println();
			return category;
		} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
			throw new CouponSystemException("enter numbers between 1 - 4");
		}
	}

	/**watch all customer coupons until given price from client choice 
	 * @param service
	 * @param scanner
	 * @return 
	 * @throws CouponSystemException
	 */
	public static double watchCouponByMaxPriceMenu(Scanner scanner) throws CouponSystemException {

		System.out.println("enter maximum price for the coupon");
		try {
		double maxPrice = Double.parseDouble(scanner.nextLine());
		return maxPrice;
		}catch (Exception e) {
			throw new CouponSystemException("please enter numbers");
		}
		
	}

	/**get customer data
	 * @param service
	 * @throws CouponSystemException
	 */
	public static Customer getCustomerDetails(CustomerService service) throws CouponSystemException {

		return service.getDetails();
	}


	
}
