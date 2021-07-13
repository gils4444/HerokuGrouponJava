package app.core.service;

import org.springframework.stereotype.Service;

import app.core.repositories.CompanyRepository;
import app.core.repositories.CouponRepository;
import app.core.repositories.CustomerRepository;

@Service
public abstract class ClientService {

	protected CompanyRepository companyRepository;
	protected CustomerRepository customerRepository;
	protected CouponRepository couponRepository;
	
	public ClientService(CompanyRepository companyRepository, CustomerRepository customerRepository,CouponRepository couponRepository){
		this.companyRepository = companyRepository;
		this.couponRepository = couponRepository;
		this.customerRepository = customerRepository;
	}
	
	public abstract boolean login(String email, String password);
}
