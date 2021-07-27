package app.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import app.core.loginManager.LoginManager;
import app.core.service.ClientService;
import app.core.utilities.JwtUtil.UserDetails;
import loginManager.Enum.ClientType;

@RestController
@CrossOrigin
public class LoginManagerController {

	
	private LoginManager manager;

	@Autowired
	public LoginManagerController(LoginManager manager) {
		this.manager = manager;
	}
	
	@PostMapping("/login")
	public UserDetails login(@RequestParam String email, @RequestParam String password, @RequestParam ClientType type) {
		try {
			return manager.login(email, password, type);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}
	
	
}
