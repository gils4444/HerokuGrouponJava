package app.core.service.menu;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import app.core.entities.Company;
import app.core.entities.Coupon;
import app.core.entities.Coupon.Category;
import app.core.exception.CouponSystemException;
import app.core.service.CompanyService;


public class CompanyServiceMenu {


	/**add coupon get data from the client for creating the coupon
	 * @param facade
	 * @param scanner
	 * @return Coupon
	 * @throws CouponSystemException
	 */
	public static Coupon addCouponMenu(Company company, Scanner scanner) throws CouponSystemException {

		int amount, price;
		String str;
		LocalDate startDate = null;
		LocalDate endDate = null;
		Category category = null;
		Coupon coupon = new Coupon();

		System.out.println("please enter amount of the coupon");
		try {
			amount = Integer.parseInt(scanner.nextLine());
			coupon.setAmount(amount);
		} catch (NumberFormatException e) {
			throw new CouponSystemException("please enter numbers bigger then 0");
		}
		
		System.out.println("please enter price of the coupon");
		try {
			price = Integer.parseInt(scanner.nextLine());
			coupon.setPrice(price);
		} catch (NumberFormatException e) {
			throw new CouponSystemException("please enter numbers bigger then 0");
		}
		System.out.println("please enter title for the coupon");
		String title = scanner.nextLine();
		coupon.setTitle(title);

		System.out.println("please enter description for the coupon");
		String description = scanner.nextLine();
		coupon.setDescription(description);

		System.out.println("please enter image for the coupon (for now string)");
		String image = scanner.nextLine();
		coupon.setImageName(image);

		System.out.println("please enter start date for the coupon in format of yyyy-mm-dd");
		try {
			str = scanner.nextLine();
			str += " 00:01";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			startDate = LocalDate.parse(str, formatter);
			coupon.setStartDate(startDate);
		} catch (Exception e) {
			throw new CouponSystemException("please enter start date  in format of yyyy-mm-dd");
		}
		System.out.println("please enter end date for the coupon in format of yyyy-mm-dd");
		try {
			str = scanner.nextLine();
			str += " 23:59";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			endDate = LocalDate.parse(str, formatter);
			coupon.setEndDate(endDate);
		} catch (DateTimeParseException e) {
			throw new CouponSystemException("please enter end date in format of yyyy-mm-dd");
		}

		System.out.println("enter category for the coupon: 1 - Food, 2 - Electricty, 3 - Restaurant, 4 - Vacation");
		try {
			int num = Integer.parseInt(scanner.nextLine());
			num--;
			category = Category.values()[num];
			coupon.setCategory(category);
		} catch (NumberFormatException e) {
			throw new CouponSystemException("please enter numbers between 1 - 4");
		}
		coupon.setCompany(company);

		return coupon;
		

	}

	/**update coupon get data from the client for updating the coupon
	 * @param facade
	 * @param scanner
	 * @return Coupon
	 * @throws CouponSystemException
	 */
	public static Coupon updateCouponMenu(CompanyService service,Scanner scanner) throws CouponSystemException {

		System.out.println("enter the coupon id number you want to update");
		int couponID = -1;
		try {
			couponID = Integer.parseInt(scanner.nextLine());
		} catch (NumberFormatException e) {
			throw new CouponSystemException("Please enter a coupon id number");
		}

		String str;
		Coupon coupon = new Coupon();
		Coupon coupon1 = new Coupon();
		try {
			coupon1 = service.findCouponById(couponID);
		} catch (Exception e) {
			throw new CouponSystemException(e.getMessage());
		}
		if (coupon1 == null)
			throw new CouponSystemException("the company dont have coupon with id: " + couponID);

		coupon.setId(coupon1.getId());
		coupon.setCompany(service.getCompanyDetails());
		System.out.println("please enter amount of the coupon");
		try {
			coupon.setAmount(Integer.parseInt(scanner.nextLine()));
		} catch (NumberFormatException e) {
			throw new CouponSystemException("please enter numbers bigger then 0");
		}

		System.out.println("please enter price of the coupon");
		try {
			coupon.setPrice(Integer.parseInt(scanner.nextLine()));
		} catch (NumberFormatException e) {
			throw new CouponSystemException("please enter numbers bigger then 0");
		}
		System.out.println("please enter title for the coupon");
		coupon.setTitle(scanner.nextLine());

		System.out.println("please enter description for the coupon");
		coupon.setDescription(scanner.nextLine());

		System.out.println("please enter image for the coupon (for now string)");
		coupon.setImageName(scanner.nextLine());

		System.out.println("please enter start date for the coupon in format of yyyy-mm-dd");
		try {
			str = scanner.nextLine();
			str += " 00:01";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			coupon.setStartDate(LocalDate.parse(str, formatter));
		} catch (Exception e) {
			throw new CouponSystemException("please enter start date  in format of yyyy-mm-dd");
		}
		System.out.println("please enter end date for the coupon in format of yyyy-mm-dd");
		try {
			str = scanner.nextLine();
			str += " 23:59";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			coupon.setEndDate(LocalDate.parse(str, formatter));
		} catch (DateTimeParseException e) {
			throw new CouponSystemException("please enter end date in format of yyyy-mm-dd");
		}

		System.out.println("enter category for the coupon: 1 - Food, 2 - Electricty, 3 - Restaurant, 4 - Vacation");
		try {
			int num = Integer.parseInt(scanner.nextLine());
			coupon.setCategory(Category.values()[--num]);
		} catch (NumberFormatException e) {
			throw new CouponSystemException("please enter numbers between 1 - 4");
		}

		return coupon;

	}

	/**ask the client which coupon need to delete
	 * @param facade
	 * @param scanner
	 * @return int
	 * @throws CouponSystemException
	 */
	public static int deleteCouponMenu(Scanner scanner) throws CouponSystemException {
		System.out.println("enter coupon id that you want to delete");
		int num = -1;
		try {
			num = Integer.parseInt(scanner.nextLine());
		} catch (NumberFormatException e) {
			throw new CouponSystemException("Please enter a coupon id number");
		}
		return num;
	}

	/**watch ask the company coupons
	 * @param service
	 * @throws CouponSystemException
	 */
	public static void watchAllCouponsMenu(CompanyService service) throws CouponSystemException {
		List<Coupon> coupons = service.getAllCoupons();
		for (Coupon coupon : coupons) {
			System.out.println(coupon.toString());
		}
		if (coupons.size() == 0)
			System.out.println("company dont have coupons");
		System.out.println();
	}

	/**watch specific coupon
	 * @param service
	 * @param scanner
	 * @throws CouponSystemException
	 */
	public static void watchCouponMenu(CompanyService service, Scanner scanner) throws CouponSystemException {

		System.out.println("enter coupon id you want to watch");
		int couponID = -1;
		try {
			couponID = Integer.parseInt(scanner.nextLine());
		} catch (NumberFormatException e) {
			throw new CouponSystemException("Please enter a coupon id number");
		}
		Coupon coupon = service.findCouponById(couponID);
		System.out.println(coupon.toString() + "\n");
	}

	/**watch coupons from specific category of the company
	 * @param service
	 * @param scanner
	 * @throws CouponSystemException
	 */
	public static void watchCouponByCategoryMenu(CompanyService service, Scanner scanner) throws CouponSystemException {

		int num = -1;
		System.out.println("enter category for the coupon:  1 - Food, 2 - Electricty, 3 - Restaurant, 4 - Vacation");
		try {
			num = Integer.parseInt(scanner.nextLine());
		} catch (NumberFormatException e) {
			throw new CouponSystemException("Please enter number between 1 - 4");
		}
		if (num < 1 | num > 4)
			throw new CouponSystemException("Please enter number between 1 - 4");

		Category category = Category.values()[--num];

		List<Coupon> coupons = service.getAllCouponsByCategory(category);

		if (coupons.size() == 0) {
			System.out.println("The company don't have coupons from category: " + category);
		} else {
			for (Coupon coupon : coupons) {
				System.out.println(coupon.toString());
			}
		}
		System.out.println();
	}

	/**watch all coupons of the company until max price
	 * @param service
	 * @param scanner
	 * @throws CouponSystemException
	 */
	public static void watchCouponByMaxPriceMenu(CompanyService service, Scanner scanner) throws CouponSystemException {

		System.out.println("enter maximum price for the coupon");
		double maxPrice = -1;
		try {
			maxPrice = Double.parseDouble(scanner.nextLine());
		} catch (NumberFormatException e) {
			throw new CouponSystemException("Please enter maximum price for the coupon");
		}

		List<Coupon> coupons = service.getAllCouponsByprice(maxPrice);

		if (coupons.size() == 0) {
			System.out.println("The company don't have coupons with lower price then: " + maxPrice);
		} else {
			for (Coupon coupon : coupons) {
				System.out.println(coupon.toString());
			}
			System.out.println();
		}
	}

	/**watch company data
	 * @param service
	 * @throws CouponSystemException
	 */
	public static void getCompanyDetails(CompanyService service) throws CouponSystemException {

		Company company = service.getCompanyDetails();
		System.out.println(company.toString() + "\n");

	}
	
	
}
