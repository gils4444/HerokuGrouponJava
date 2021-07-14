package app.core.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import app.core.entities.Company;
import app.core.entities.Customer;
import app.core.exception.CouponSystemException;
import app.core.service.AdminService;
import app.core.service.CustomerService;
import app.core.utilities.JwtUtil;
@RestController
@RequestMapping("/admin")
@CrossOrigin
public class AdminController {
	

	private AdminService service;
	
	@Autowired
	public AdminController(AdminService service) {
		this.service = service;
	}

//	@PostMapping("/login")
//	public boolean login(@RequestParam String email, String password) {
//		try {
//			return service.login(email, password);
//		} catch (Exception e) {
//			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
//		}
//	}

	@PostMapping("/add/company")
	public Company addCompany(@RequestBody Company company,@RequestHeader String token) {
		try {
			if(!recieveToken(token))
				throw new Exception("token is not validated ");
			return service.addCompany(company);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@DeleteMapping("/delete/company/{id}")
	public void deleteCompany(@PathVariable int id,@RequestHeader String token) {
		try {
			if(!recieveToken(token))
				throw new Exception("token is not validated ");
			service.deleteCompany(id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/get/company/all")
	public List<Company> getAllCompanies(@RequestHeader String token) {
		try {
			if(recieveToken(token))
				return service.FindAllCompanies();
			throw new Exception("token is not validated ");
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/get/copmpany/{id}")
	public Company getCompany(@PathVariable int id,@RequestHeader String token) {
		try {
			if(!recieveToken(token))
				throw new Exception("token is not validated ");
			return service.findCompanyById(id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PutMapping("/update/company")
	public Company updateCompany(@RequestBody Company company,@RequestHeader String token) {
		try {
			if(!recieveToken(token))
				throw new Exception("token is not validated ");
			return service.updateCompany(company);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PostMapping("/add/customer")
	public Customer addCustomer(@RequestBody Customer customer,@RequestHeader String token) {
		try {
			if(!recieveToken(token))
				throw new Exception("token is not validated ");
			return service.addCustomer(customer);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@DeleteMapping("/delete/customer/{id}")
	public void deleteCustomer(@PathVariable int id,@RequestHeader String token) {
		try {
			if(!recieveToken(token))
				throw new Exception("token is not validated ");
			service.deleteCustomer(id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/get/customer/all")
	public List<Customer> getAllCustomers(@RequestHeader String token) {
		try {
			if(!recieveToken(token))
				throw new Exception("token is not validated ");
			return service.findAllCustomers();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@GetMapping("/get/customer/{id}")
	public Customer getCustomer(@PathVariable int id,@RequestHeader String token) {
		try {
			if(!recieveToken(token))
				throw new Exception("token is not validated ");
			return service.findCustomerById(id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@PutMapping("/update/customer")
	public Customer updateCustomer(@RequestBody Customer customer,@RequestHeader String token) {
		try {
			if(!recieveToken(token))
				throw new Exception("token is not validated ");
			return service.updateCustomer(customer);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	public boolean recieveToken(String token) throws CouponSystemException {
		try {
			JwtUtil util = new JwtUtil();
			String userName = service.getAdminEmail();
			System.out.println("recieveToken : "+util.validateToken(token, userName));
			return util.validateToken(token, userName);
		} catch (Exception e) {
			throw new CouponSystemException("recieveToken: " + e.getMessage());
		}
	}
}
