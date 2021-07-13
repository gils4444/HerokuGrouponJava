//package app.core;
//
//import java.time.LocalDate;
//import java.util.Scanner;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.ConfigurableApplicationContext;
//
//import app.core.entities.Company;
//import app.core.entities.Coupon;
//import app.core.entities.Coupon.Category;
//import app.core.entities.Customer;
//import app.core.exception.CouponSystemException;
//import app.core.loginManager.LoginManager;
//import app.core.service.AdminService;
//import app.core.service.CompanyService;
//import app.core.service.CustomerService;
//import loginManager.Enum.ClientType;
//
//@SpringBootApplication
//public class GrouponPart3Application {
//
//	public static void initDB(ConfigurableApplicationContext ctx) throws Exception {
//
//		LoginManager manager = ctx.getBean(LoginManager.class);
//		manager.setCtx(ctx);
//
//		AdminService adminService = (AdminService) manager.login("admin@admin.com", "admin", ClientType.Administrator);
//
//		try {
//			Company company1 = new Company("company_name_1", "company_email_1", "pass1");
//			Company company2 = new Company("company_name_2", "company_email_2", "pass2");
//			Company company3 = new Company("company_name_3", "company_email_3", "pass3");
//
//			Customer customer1 = new Customer("first_name_1", "last_name_1", "customer_email_1", "pass1");
//			Customer customer2 = new Customer("first_name_2", "last_name_2", "customer_email_2", "pass2");
//			Customer customer3 = new Customer("first_name_3", "last_name_3", "customer_email_3", "pass3");
//
//			try {
//				customer1 = adminService.addCustomer(customer1);
//			} catch (CouponSystemException e) {
//				System.out.println("=======  " + e.getMessage());
//			}
//			try {
//				customer2 = adminService.addCustomer(customer2);
//			} catch (CouponSystemException e) {
//				System.out.println("=======  " + e.getMessage());
//			}
//			try {
//				customer3 = adminService.addCustomer(customer3);
//			} catch (CouponSystemException e) {
//				System.out.println("=======  " + e.getMessage());
//			}
//			try {
//				company1 = adminService.addCompany(company1);
//			} catch (CouponSystemException e) {
//				System.out.println("=======  " + e.getMessage());
//			}
//			try {
//				company2 = adminService.addCompany(company2);
//			} catch (CouponSystemException e) {
//				System.out.println("=======  " + e.getMessage());
//			}
//			try {
//				company3 = adminService.addCompany(company3);
//			} catch (CouponSystemException e) {
//				System.out.println("=======  " + e.getMessage());
//			}
//
//			CompanyService companyService1 = (CompanyService) manager.login("company_email_1", "pass1",
//					ClientType.Company);
//			CompanyService companyService2 = (CompanyService) manager.login("company_email_2", "pass2",
//					ClientType.Company);
//			CompanyService companyService3 = (CompanyService) manager.login("company_email_3", "pass3",
//					ClientType.Company);
//
//			System.out.println();
//
//			System.out.println(company1);
//			System.out.println(company2);
//			System.out.println(customer1);
//			System.out.println(customer2);
//
//			LocalDate startDate = LocalDate.now();
//			LocalDate endDate = LocalDate.now().plusDays(-1);
//			Coupon coupon1 = new Coupon(Category.ELECTRICITY, "coupon_title_1", "coupon_description_1", startDate,
//					endDate, 11, 11, company1);
//			endDate = LocalDate.now().plusDays(-2);
//			Coupon coupon2 = new Coupon(Category.FOOD, "coupon_title_2", "coupon_description_2", startDate, endDate, 22,
//					12, company1);
//			endDate = LocalDate.now().plusDays(3);
//			Coupon coupon3 = new Coupon(Category.RESTAURANT, "coupon_title_3", "coupon_description_3", startDate,
//					endDate, 33, 13, company1);
//			endDate = LocalDate.now().plusDays(4);
//			Coupon coupon4 = new Coupon(Category.VACATION, "coupon_title_4", "coupon_description_4", startDate, endDate,
//					44, 24, company2);
//			endDate = LocalDate.now().plusDays(5);
//			Coupon coupon5 = new Coupon(Category.VACATION, "coupon_title_5", "coupon_description_5", startDate, endDate,
//					55, 25, company2);
//			endDate = LocalDate.now().plusDays(6);
//			Coupon coupon6 = new Coupon(Category.RESTAURANT, "coupon_title_6", "coupon_description_6", startDate,
//					endDate, 66, 26, company2);
//			endDate = LocalDate.now().plusDays(7);
//			Coupon coupon7 = new Coupon(Category.FOOD, "coupon_title_7", "coupon_description_7", startDate, endDate, 77,
//					37, company3);
//			endDate = LocalDate.now().plusDays(8);
//			Coupon coupon8 = new Coupon(Category.ELECTRICITY, "coupon_title_8", "coupon_description_8", startDate,
//					endDate, 88, 38, company3);
//			endDate = LocalDate.now().plusDays(9);
//			Coupon coupon9 = new Coupon(Category.RESTAURANT, "coupon_title_9", "coupon_description_9", startDate,
//					endDate, 99, 39, company3);
//
//			System.out.println();
//			System.out.println("++++++++++++++++++companyService.addCoupon(coupon1)++++++++++++++++++");
//			System.out.println();
//			try {
//				coupon1 = companyService1.addCoupon(coupon1);
//				coupon2 = companyService1.addCoupon(coupon2);
//				coupon3 = companyService1.addCoupon(coupon3);
//
//				coupon4 = companyService2.addCoupon(coupon4);
//				coupon5 = companyService2.addCoupon(coupon5);
//				coupon6 = companyService2.addCoupon(coupon6);
//
//				coupon7 = companyService3.addCoupon(coupon7);
//				coupon8 = companyService3.addCoupon(coupon8);
//				coupon8 = companyService3.addCoupon(coupon9);
//
//			} catch (Exception e) {
//				System.out.println(e.getMessage());
//			}
//
//		} catch (Exception e) {
//			System.out.println("=======  " + e.getMessage());
//		}
//
//	}
//
//	public static void login(ConfigurableApplicationContext ctx) throws CouponSystemException {
//
//		try {
//			try (Scanner scanner = new Scanner(System.in);) {
//				LoginManager manager = ctx.getBean(LoginManager.class);
//				manager.setCtx(ctx);
//
//				boolean flag = true;
//
//				String email = null;
//				String password = null;
//
//				int opt = 0;
//
//				System.out.println("\nHello");
//
//				while (flag) {
//
//					System.out.println();
//					System.out.println("For admin press.......1");
//					System.out.println("For company press.....2");
//					System.out.println("For customer press....3");
//					System.out.println("For exit press........4");
//
//					try {
//						opt = Integer.parseInt(scanner.nextLine());
//					} catch (Exception e) {
//
//					}
//
//					if (opt == 1 | opt == 2 | opt == 3) {
//						System.out.println("please enter email");
//						email = scanner.nextLine();
//						System.out.println("please enter password");
//						password = scanner.nextLine();
//					}
//
//					System.out.println();
//
//					switch (opt) {
//					case 1: {
//						AdminService adminService = null;
//						try {
//							adminService = (AdminService) manager.login(email, password, ClientType.Administrator);
//							if (adminService != null)
//								AdminService.menu(adminService, scanner);
//						} catch (Exception e) {
//							System.out.println(e.getMessage());
//						}
//						break;
//					}
//					case 2: {
//						try {
//						CompanyService companyService = (CompanyService) manager.login(email, password,
//								ClientType.Company);
//						if(companyService!=null)
//							CompanyService.menu(companyService, scanner);
//						} catch (Exception e) {
//							System.out.println(e.getMessage());
//						}
//						break;
//
//					}
//					case 3: {
//						try {
//						CustomerService customerService = (CustomerService) manager.login(email, password, ClientType.Customer);
//						if(customerService!=null)
//							CustomerService.menu(customerService, scanner);
//						} catch (Exception e) {
//							System.out.println(e.getMessage());
//						}
//						break;
//					}
//					case 4: {
//						flag = false;
//						break;
//					}
//
//					default: {
//						System.out.println("worng insert please try again");
//						break;
//					}
//					}
//				}
//			}
//		}catch(
//
//	Exception e)
//	{
//		System.out.println("hey hey");
//		System.out.println(e.getMessage());
//	}
//	}
//
//	public static void main(String[] args) {
//		try (ConfigurableApplicationContext ctx = SpringApplication.run(GrouponPart3Application.class, args);) {
//			System.out.println();
//			System.out.println("============================start APP================================");
//			System.out.println();
//			initDB(ctx);
//			login(ctx);
//
//		} catch (Exception e) {
//			System.out.println("=======  " + e.getMessage());
//			e.printStackTrace();
//		}
//	}
//
//}
