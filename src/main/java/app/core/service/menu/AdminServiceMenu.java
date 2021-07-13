package app.core.service.menu;

import java.util.List;
import java.util.Scanner;


import app.core.entities.Company;
import app.core.entities.Customer;
import app.core.exception.CouponSystemException;
import app.core.service.AdminService;

public class AdminServiceMenu {
	 /* add new company
	 * 
	 * @param scanner
	 * @return Company
	 */
	public static Company addCompanyMenu(Scanner scanner) {

		System.out.println("please enter name for the company");
		String name = scanner.nextLine();
		System.out.println("please enter email for the company");
		String email = scanner.nextLine();
		System.out.println("please enter password for the company");
		String password = scanner.nextLine();
		Company company = new Company(name, email, password);
		return company;

	}

	/**
	 * update company
	 * 
	 * @param service
	 * @param scanner
	 * @throws CouponSystemException
	 */
	public static Company updateCompanyMenu(Scanner scanner) throws CouponSystemException {

		System.out.println("enter the company id number you want to update");
		int num = -1;
		try {
			num = Integer.parseInt(scanner.nextLine());
		} catch (NumberFormatException e) {
			throw new CouponSystemException("please enter company id number that you want to update");
		}

		Company company = new Company();
		company.setId(num);
		System.out.println("enter name");
		company.setName(scanner.nextLine());
		System.out.println("enter email");
		company.setEmail(scanner.nextLine());
		System.out.println("enter password ");
		company.setPassword(scanner.nextLine());
		return company;
	}

	/**
	 * delete one company
	 * 
	 * @param service
	 * @param scanner
	 * @throws CouponSystemException
	 */
	public static int deleteCompanyMenu(Scanner scanner) throws CouponSystemException {

		System.out.println("enter company id that you want to delete");

		int companyID = 0;

		try {
			companyID = Integer.parseInt(scanner.nextLine());
		} catch (NumberFormatException e) {
			throw new CouponSystemException("please enter company id number that you want to delete");
		}
		return companyID;
	}

	/**
	 * watch all companies
	 * 
	 * @param service
	 * @throws CouponSystemException
	 */
	public static void watchAllCompaniesMenu(AdminService service) throws CouponSystemException {
		List<Company> companies = service.FindAllCompanies();
		for (Company company : companies) {
			System.out.println(company.toString());
		}
	}

	/**
	 * watch the company data
	 * 
	 * @param service
	 * @param scanner
	 * @throws CouponSystemException
	 */
	public static int watchCompanyMenu(Scanner scanner) throws CouponSystemException {

		System.out.println("enter company id you want to watch");
		int compamyID = -1;
		try {
			compamyID = Integer.parseInt(scanner.nextLine());
		} catch (NumberFormatException e) {
			throw new CouponSystemException("please enter company id number that you want to watch");
		}

		return compamyID;
	}

	/**
	 * add new customer
	 * 
	 * @param service
	 * @param scanner
	 * @throws CouponSystemException
	 */
	public static Customer addCustomerMenu(Scanner scanner) throws CouponSystemException {

		System.out.println("please enter first name for the customer");
		String firstName = scanner.nextLine();
		System.out.println("please enter last name for the customer");
		String lastName = scanner.nextLine();
		System.out.println("please enter email for the customer");
		String email = scanner.nextLine();
		System.out.println("please enter password for the customer");
		String password = scanner.nextLine();
		return new Customer(firstName, lastName, email, password);
		

	}

	/**
	 * update customer
	 * 
	 * @param service
	 * @param scanner
	 * @return 
	 * @throws CouponSystemException
	 */
	public static Customer updateCustomerMenu(AdminService service, Scanner scanner) throws CouponSystemException {

		System.out.println("enter the customer id number you want to update");

		int num = -1;
		try {
			num = Integer.parseInt(scanner.nextLine());
		} catch (NumberFormatException e) {
			throw new CouponSystemException("please enter the customer id number you want to update");
		}
		Customer customer = new Customer();
		customer.setId(num);
		System.out.println("enter first name");
		customer.setFirstName(scanner.nextLine());
		System.out.println("enter last name");
		customer.setLastName(scanner.nextLine());
		System.out.println("enter email ");
//		customer.setEmail(scanner.nextLine());
		System.out.println("enter password ");
		customer.setPassword(scanner.nextLine());
		return customer;
	}

	/**
	 * delete customer
	 * 
	 * @param service
	 * @param scanner
	 * @throws CouponSystemException
	 */
	public static int deleteCustomerMenu(Scanner scanner) throws CouponSystemException {

		System.out.println("enter the customer id number you want to delete");

		int num = -1;
		try {
			num = Integer.parseInt(scanner.nextLine());
		} catch (NumberFormatException e) {
			throw new CouponSystemException("please enter the customer id number you want to delete");
		}
		
		return num;
		
	}

	/**
	 * watch all customers
	 * 
	 * @param service
	 * @throws CouponSystemException
	 */
	public static void watchAllCustomersMenu(AdminService service) throws CouponSystemException {

		List<Customer> customers = service.findAllCustomers();
		for (Customer customer : customers) {
			System.out.println(customer.toString());
		}
	}

	/**
	 * watch the customer data
	 * 
	 * @param service
	 * @param scanner
	 * @throws CouponSystemException
	 */
	public static int watchCustomerMenu(Scanner scanner) throws CouponSystemException {

		System.out.println("enter customer id you want to watch");
		int customerID = -1;
		try {
			customerID = Integer.parseInt(scanner.nextLine());
		} catch (NumberFormatException e) {
			throw new CouponSystemException("please enter the customer id number you want to watch");
		}
		return customerID;
	}

	/**
	 * this function is not visible to the admin client and used only for build
	 * "static" DB.
	 * 
	 * @throws CouponSystemException
	 * 
	 */

	/**
	 * show the admin menu to the customer
	 * 
	 * @param facade
	 * @param scanner
	 * @throws CouponSystemException
	 */

}
