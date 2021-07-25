package app.core.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.core.entities.Company;
import app.core.entities.Customer;
import app.core.exception.CouponSystemException;
import app.core.repositories.CompanyRepository;
import app.core.repositories.CouponRepository;
import app.core.repositories.CustomerRepository;

@Service
@Transactional
public class AdminService extends ClientService {

	private String adminEmail = "admin@admin.com";
	private String adminPassword = "admin";

	@Override
	public boolean login(String email, String password) {
		if (email.equals(adminEmail) && password.equals(adminPassword))
			return true;
		if (!email.equals(adminEmail))
			System.out.println("Wrong Email");
		if (!password.equals(adminPassword))
			System.out.println("Wrong Password");
		return false;
	}

	public String getAdminEmail() {
		return adminEmail;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	@Autowired
	public AdminService(CompanyRepository companyRepository, CustomerRepository customerRepository,
			CouponRepository couponRepository) {
		super(companyRepository, customerRepository, couponRepository);
	}

	public Company findCompanyById(int id) throws CouponSystemException {
		Optional<Company> opt = companyRepository.findById(id);
		if (opt.isPresent()) {
			return opt.get();
		} else {
			throw new CouponSystemException("company not found");
		}
	}

	public List<Company> FindAllCompanies() {
		return companyRepository.findAll();
	}

	public Company addCompany(Company newCompany) throws CouponSystemException {
		System.out.println("==================           addCompany        ===========");
		Company company = companyRepository.findByNameIgnoreCase(newCompany.getName());
		if (company != null)
			throw new CouponSystemException(
					"There is already a company with the same name");
		company = companyRepository.findByEmail(newCompany.getEmail());
		if (company != null)
			throw new CouponSystemException(
					"There is already a company with the same email");

		company = companyRepository.save(newCompany);
		System.out.println("=======================  AdminService add company:" + newCompany);

		return company;
	}

	public Company updateCompany(Company otherCompany) throws CouponSystemException {

		Company company = findCompanyById(otherCompany.getId());

		if (company.getName().equalsIgnoreCase(otherCompany.getName())) {

			if (company.getId() == otherCompany.getId()) {

				if ((companyRepository.findByEmailAndIdIsNot(otherCompany.getEmail(), otherCompany.getId())) == null) {

					company.setEmail(otherCompany.getEmail());
					company.setPassword(otherCompany.getPassword());
					company.setCoupons(otherCompany.getCoupons());
					return company;
				} else {
					company = companyRepository.findByEmailAndIdIsNot(otherCompany.getEmail(), otherCompany.getId());
					System.out.println(company.toString());
					throw new CouponSystemException("There is already a company with the same Email");
				}
			} else {
				throw new CouponSystemException("you cant change the company id");
			}
		} else {
			throw new CouponSystemException("you cant change the company name");
		}
	}

	public void deleteCompany(int companyId) throws CouponSystemException {
		Company company = findCompanyById(companyId);
		if (company != null) {
			companyRepository.delete(company);
			System.out.println("Company with id :" + companyId + " deleted");
		} else
			throw new CouponSystemException("Company with id :" + companyId + " not exsits");
	}

	public Customer addCustomer(Customer newCustomer) throws CouponSystemException {
		System.out.println("==================           addCustomer        ===========");
		Customer customer = customerRepository.findByEmailIgnoreCase(newCustomer.getEmail());
		if (customer != null)
			throw new CouponSystemException("There is already a customer with the same Email");
		customer = customerRepository.save(newCustomer);
		System.out.println(customer);
		return customer;
	}

	public Customer updateCustomer(Customer otherCustomer) throws CouponSystemException {
		try {
			Customer customer = findCustomerById(otherCustomer.getId());
			if(customerRepository.findByEmailIgnoreCaseAndIdIsNot(otherCustomer.getEmail(),
					otherCustomer.getId())!=null)
				throw new CouponSystemException("There is already a customer with the same Email");
			if (customer != null) {
				customer.setFirstName(otherCustomer.getFirstName());
				customer.setLastName(otherCustomer.getLastName());
				customer.setEmail(otherCustomer.getEmail());
				customer.setPassword(otherCustomer.getPassword());
				customer.setCoupons(otherCustomer.getCoupons());
				return customer;
			} else
				throw new CouponSystemException("Customer not found");
		} catch (Exception e) {
			throw new CouponSystemException(e.getMessage());
		}
	}

	public void deleteCustomer(int customerId) throws CouponSystemException {
		Customer customer = findCustomerById(customerId);
		if (customer != null)
			customerRepository.delete(customer);
		else
			throw new CouponSystemException("Customer with id :" + customerId + " not exsits");
	}

	public Customer findCustomerById(int customerId) {
		Optional<Customer> opt = customerRepository.findById(customerId);
		if (opt.isPresent())
			return opt.get();
		return null;
	}

	public List<Customer> findAllCustomers() {
		return customerRepository.findAll();
	}

}
