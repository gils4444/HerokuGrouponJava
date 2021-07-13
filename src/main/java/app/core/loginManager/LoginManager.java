package app.core.loginManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import app.core.exception.CouponSystemException;
import app.core.service.AdminService;
import app.core.service.ClientService;
import app.core.service.CompanyService;
import app.core.service.CustomerService;
import app.core.utilities.JwtUtil;
import app.core.utilities.JwtUtil.UserDetails;
import loginManager.Enum.ClientType;

@Component
public class LoginManager {

	private ConfigurableApplicationContext ctx;

	public LoginManager() {
	}

	@Autowired
	public LoginManager(ConfigurableApplicationContext ctx) {
		this.ctx = ctx;
	}

	public ConfigurableApplicationContext getCtx() {
		return ctx;
	}

	public void setCtx(ConfigurableApplicationContext ctx) {
		this.ctx = ctx;
	}

	public UserDetails returnUserDetails(String password, String email, ClientType type, int id,String name) {
		JwtUtil util = new JwtUtil();
		UserDetails userDetails = new UserDetails(id, email, password, type,name);
		userDetails.setToken(util.generateToken(userDetails));
		return userDetails;
	}

	public UserDetails login(String email, String password, ClientType type) throws CouponSystemException {
		switch (type) {
		case ADMINISTRATOR: {
			AdminService service = ctx.getBean(AdminService.class);
			if (!service.login(email, password)) {
				service = null;
				throw new CouponSystemException("Wrong Details");
			}
			System.out.println("================================================= ");
			return returnUserDetails(password, email, type, 0,"Admin");
		}
		case COMPANY: {
			CompanyService service = ctx.getBean(CompanyService.class);
			if (!service.login(email, password)) {
				service = null;
				throw new CouponSystemException("Wrong Details");
			}
			// must log in to get the company id
			service.login(email, password);

			return returnUserDetails(password, email, type, service.getCompanyID(),service.getCompanyDetails().getName());
		}
		case CUSTOMER: {
			CustomerService service = ctx.getBean(CustomerService.class);
			System.out.println("email: "+email);
			System.out.println("pass: "+password);
			if (!service.login(email, password)) {
				System.out.println("email: "+email);
				System.out.println("pass: "+password);
				service = null;
				throw new CouponSystemException("Wrong Details");
			}
			// must log in to get the customer id
			service.login(email, password);

			return returnUserDetails(password, email, type, service.getCustomerID(),service.getDetails().getFirstName());
		}
		default:
			return null;
		}
	}

}